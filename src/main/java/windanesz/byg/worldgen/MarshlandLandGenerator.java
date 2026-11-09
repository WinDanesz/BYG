package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MarshlandLandGenerator {
    private static final ResourceLocation MARSHLANDS = new ResourceLocation("byg", "byg_marshlands");
    /** Client update without observer propagation into an unloaded neighbor chunk. */
    private static final int WORLDGEN_FLAGS = 2 | 16;

    private MarshlandLandGenerator() {
    }

    public static void generate(Random random, int blockX, int blockZ, World world, int dimensionId) {
        if (!Config.isFeatureDimension(dimensionId) || world.isRemote) {
            return;
        }

        int minX = blockX - 8;
        int maxX = blockX + 7;
        int minZ = blockZ - 8;
        int maxZ = blockZ + 7;
        NoiseLayers noise = new NoiseLayers(world.getSeed());
        List<BlockPos> generatedSurfaces = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isMarshlands(world, x, z)) {
                    continue;
                }

                int waterY = findWaterSurface(world, x, z);
                if (waterY < 1) {
                    continue;
                }
                raiseAndCoverWaterFloor(world, new BlockPos(x, waterY, z), waterDepth(noise, x, z));
                if (!isLand(noise, x, z)) {
                    continue;
                }
                BlockPos surfacePos = new BlockPos(x, waterY, z);
                placeLandColumn(random, world, surfacePos);
                generatedSurfaces.add(surfacePos);
            }
        }

        decorateGeneratedLand(random, world, generatedSurfaces, minX, maxX, minZ, maxZ);
    }

    private static boolean isMarshlands(World world, int x, int z) {
        Biome biome = world.getBiome(new BlockPos(x, 128, z));
        return MARSHLANDS.equals(Biome.REGISTRY.getNameForObject(biome));
    }

    private static boolean isLand(NoiseLayers noise, int x, int z) {
        // The zero contours of this large-scale simplex layer form continuous,
        // winding channels wide enough to remain useful boat routes.
        double channel = Math.abs(noise.channels.getValue(x / 52.0D, z / 52.0D));
        if (channel < 0.085D) {
            return false;
        }

        double broad = noise.broad.getValue(x / 42.0D, z / 42.0D);
        double medium = noise.medium.getValue(x / 15.0D, z / 15.0D);
        double detail = noise.detail.getValue(x / 5.0D, z / 5.0D);
        double landValue = broad * 0.62D + medium * 0.28D + detail * 0.10D;

        // Break up otherwise-solid banks with fine, irregular water pockets.
        // This continuous simplex layer provides some of the old template's
        // water/grass texture without a literal checkerboard or hard clusters.
        double brokenShape = noise.breakup.getValue(x / 2.4D, z / 2.4D);
        double threshold = brokenShape * 0.075D;
        return landValue > threshold;
    }

    private static int waterDepth(NoiseLayers noise, int x, int z) {
        double depthValue = noise.depth.getValue(x / 18.0D, z / 18.0D);
        if (depthValue < -0.55D) {
            return 1;
        }
        if (depthValue > 0.70D) {
            return 4;
        }
        return depthValue > 0.30D ? 3 : 2;
    }

    private static final class NoiseLayers {
        private final NoiseGeneratorSimplex broad;
        private final NoiseGeneratorSimplex medium;
        private final NoiseGeneratorSimplex detail;
        private final NoiseGeneratorSimplex breakup;
        private final NoiseGeneratorSimplex channels;
        private final NoiseGeneratorSimplex depth;

        private NoiseLayers(long worldSeed) {
            broad = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0x632BE59BD9B4E019L));
            medium = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0x9E3779B97F4A7C15L));
            detail = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0xC2B2AE3D27D4EB4FL));
            breakup = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0x94D049BB133111EBL));
            channels = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0xDB4F0B9175AE2165L));
            depth = new NoiseGeneratorSimplex(new Random(worldSeed ^ 0xA24BAED4963EE407L));
        }
    }

    private static int findWaterSurface(World world, int x, int z) {
        int y = world.getActualHeight() - 1;
        BlockPos cursor = new BlockPos(x, y, z);
        while (y > 0 && world.isAirBlock(cursor)) {
            y--;
            cursor = cursor.down();
        }
        return isWater(world, cursor) ? y : -1;
    }

    /**
     * Templates notify their neighbours whatever flags they are placed with, which turns nearby water sources into
     * flowing water. Skipping those would punch holes in the land, or a checkerboard if placing land notifies too.
     */
    private static boolean isWater(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }

    private static void raiseAndCoverWaterFloor(World world, BlockPos waterSurface, int targetDepth) {
        BlockPos floorPos = waterSurface.down();
        while (floorPos.getY() > 0 && isWater(world, floorPos)) {
            floorPos = floorPos.down();
        }
        if (floorPos.getY() <= 0) {
            return;
        }

        world.setBlockState(floorPos, ModBlocks.mud_block.getDefaultState(), WORLDGEN_FLAGS);
        int highestMudY = waterSurface.getY() - targetDepth;
        for (int y = floorPos.getY() + 1; y <= highestMudY; y++) {
            world.setBlockState(new BlockPos(waterSurface.getX(), y, waterSurface.getZ()),
                    ModBlocks.mud_block.getDefaultState(), WORLDGEN_FLAGS);
        }
    }

    private static void placeLandColumn(Random random, World world, BlockPos surfacePos) {
        world.setBlockState(surfacePos, random.nextInt(10) < 3
                ? ModBlocks.mud_block.getDefaultState()
                : Blocks.GRASS.getDefaultState(), WORLDGEN_FLAGS);
        for (int depth = 1; depth <= 3; depth++) {
            BlockPos below = surfacePos.down(depth);
            if (!isWater(world, below)) {
                break;
            }
            world.setBlockState(below, Blocks.DIRT.getDefaultState(), WORLDGEN_FLAGS);
        }
    }

    private static void decorateGeneratedLand(Random random, World world, List<BlockPos> surfaces,
                                              int minX, int maxX, int minZ, int maxZ) {
        for (BlockPos surfacePos : surfaces) {
            BlockPos plantPos = surfacePos.up();
            if (!world.isAirBlock(plantPos)) {
                continue;
            }

            int waterDistance = horizontalDistanceToWater(world, surfacePos, 3, minX, maxX, minZ, maxZ);
            boolean placeShoreReed = waterDistance == 1 && random.nextInt(4) == 0;
            boolean placeInlandReed = waterDistance >= 2 && waterDistance <= 3 && random.nextInt(40) == 0;
            if (ModBlocks.cattails != null && waterDistance == 1 && random.nextInt(6) == 0) {
                world.setBlockState(plantPos, ModBlocks.cattails.getDefaultState(), WORLDGEN_FLAGS);
                continue;
            }
            if (placeShoreReed || placeInlandReed) {
                world.setBlockState(plantPos, ModBlocks.reed.getDefaultState(), WORLDGEN_FLAGS);
                continue;
            }

            if (world.getBlockState(surfacePos).getBlock() != Blocks.GRASS || random.nextInt(10) == 0) {
                continue;
            }

            if (random.nextInt(5) < 3 && world.isAirBlock(plantPos.up())) {
                world.setBlockState(plantPos, Blocks.DOUBLE_PLANT.getDefaultState()
                        .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                        .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), WORLDGEN_FLAGS);
                world.setBlockState(plantPos.up(), Blocks.DOUBLE_PLANT.getDefaultState()
                        .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                        .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), WORLDGEN_FLAGS);
            } else {
                world.setBlockState(plantPos, Blocks.TALLGRASS.getDefaultState()
                        .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), WORLDGEN_FLAGS);
            }
        }
    }

    private static int horizontalDistanceToWater(World world, BlockPos pos, int maxDistance,
                                                 int minX, int maxX, int minZ, int maxZ) {
        for (int distance = 1; distance <= maxDistance; distance++) {
            for (int dx = -distance; dx <= distance; dx++) {
                for (int dz = -distance; dz <= distance; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != distance) {
                        continue;
                    }
                    int x = pos.getX() + dx;
                    int z = pos.getZ() + dz;
                    if (x < minX || x > maxX || z < minZ || z > maxZ) {
                        continue;
                    }
                    if (isWater(world, new BlockPos(x, pos.getY(), z))) {
                        return distance;
                    }
                }
            }
        }
        return maxDistance + 1;
    }
}

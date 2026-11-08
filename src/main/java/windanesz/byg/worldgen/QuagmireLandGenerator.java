package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

/**
 * Creates the low, irregular peat islands found in quagmires.  This replaces
 * the old fixed land templates so that their size, outline, and vegetation do
 * not repeat from chunk to chunk.
 */
public final class QuagmireLandGenerator {
    private static final ResourceLocation QUAGMIRE = new ResourceLocation("byg", "byg_quagmire");
    private static final int WORLDGEN_FLAGS = 2 | 16;
    private static final int POOL_DEPTH = 2;

    private QuagmireLandGenerator() {
    }

    public static void generate(Random random, int blockX, int blockZ, World world, int dimensionId) {
        if (!Config.isFeatureDimension(dimensionId) || world.isRemote) {
            return;
        }

        int minX = blockX - 8;
        int maxX = blockX + 7;
        int minZ = blockZ - 8;
        int maxZ = blockZ + 7;
        long seed = world.getSeed();
        double slimeChance = Config.getQuagmireSlimeChance();
        boolean hasQuagmire = false;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isQuagmire(world, x, z)) {
                    continue;
                }
                hasQuagmire = true;
                BlockPos surface = findSurface(world, x, z);
                Block surfaceBlock = world.getBlockState(surface).getBlock();
                // A neighboring chunk can decorate this water before Quagmire shapes it.
                boolean hasFloatingPlant = surfaceBlock == ModBlocks.algae || surfaceBlock == ModBlocks.tiny_lilypad
                        || surfaceBlock == Blocks.WATERLILY;
                BlockPos water = hasFloatingPlant ? surface.down() : surface;
                if (water.getY() < 1 || !isWater(world, water)) {
                    continue;
                }
                if (!hasFloatingPlant && isPeatLand(seed, x, z)) {
                    placeColumn(random, world, water, slimeChance);
                } else {
                    fillWaterBelow(world, water.down(POOL_DEPTH));
                }
            }
        }
        if (!hasQuagmire) {
            return;
        }
        coverExposedMud(random, world, minX, maxX, minZ, maxZ);
        generateTrees(random, world, minX, maxX, minZ, maxZ);
    }

    private static boolean isQuagmire(World world, int x, int z) {
        Biome biome = world.getBiome(new BlockPos(x, 128, z));
        return QUAGMIRE.equals(Biome.REGISTRY.getNameForObject(biome));
    }

    private static boolean isPeatLand(long seed, int x, int z) {
        double broad = valueNoise(seed, x, z, 18);
        double medium = valueNoise(seed + 0x9E3779B97F4A7C15L, x, z, 7);
        double detail = valueNoise(seed + 0xC2B2AE3D27D4EB4FL, x, z, 3);
        return broad * 0.58D + medium * 0.30D + detail * 0.12D > 0.345D;
    }

    private static double valueNoise(long seed, int x, int z, int scale) {
        int cellX = Math.floorDiv(x, scale);
        int cellZ = Math.floorDiv(z, scale);
        double localX = (x - cellX * (double) scale) / scale;
        double localZ = (z - cellZ * (double) scale) / scale;
        localX = localX * localX * (3.0D - 2.0D * localX);
        localZ = localZ * localZ * (3.0D - 2.0D * localZ);
        double north = lerp(randomValue(seed, cellX, cellZ), randomValue(seed, cellX + 1, cellZ), localX);
        double south = lerp(randomValue(seed, cellX, cellZ + 1), randomValue(seed, cellX + 1, cellZ + 1), localX);
        return lerp(north, south, localZ);
    }

    private static double randomValue(long seed, int x, int z) {
        long value = seed ^ (x * 341873128712L) ^ (z * 132897987541L);
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        return (value >>> 11) * 0x1.0p-53;
    }

    private static double lerp(double start, double end, double amount) {
        return start + (end - start) * amount;
    }

    private static BlockPos findSurface(World world, int x, int z) {
        BlockPos cursor = new BlockPos(x, world.getActualHeight() - 1, z);
        while (cursor.getY() > 0 && world.isAirBlock(cursor)) {
            cursor = cursor.down();
        }
        return cursor;
    }

    private static boolean isWater(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }

    private static void fillWaterBelow(World world, BlockPos pos) {
        IBlockState mud = ModBlocks.mud_block.getDefaultState();
        while (pos.getY() > 0 && isWater(world, pos)) {
            world.setBlockState(pos, mud, WORLDGEN_FLAGS);
            pos = pos.down();
        }
    }

    private static void placeColumn(Random random, World world, BlockPos top, double slimeChance) {
        // Slime is a sunken bog pocket, not a plant: it replaces the surface
        // block and therefore never sits one block above the land.
        IBlockState surface = slimeChance > 0.0D && random.nextDouble() < slimeChance
                ? Blocks.SLIME_BLOCK.getDefaultState()
                : random.nextInt(4) == 0 ? ModBlocks.mud_block.getDefaultState()
                : ModBlocks.peat_grass.getDefaultState();
        world.setBlockState(top, surface, WORLDGEN_FLAGS);

        // Keep a peat layer under the surface and fill the remaining water down to the bed.
        BlockPos below = top.down();
        if (isWater(world, below)) {
            world.setBlockState(below, ModBlocks.peat_dirt.getDefaultState(), WORLDGEN_FLAGS);
            fillWaterBelow(world, below.down());
        }

        if ((surface.getBlock() != ModBlocks.peat_grass && surface.getBlock() != ModBlocks.mud_block)
                || random.nextInt(5) >= 3) {
            return;
        }
        BlockPos plantPos = top.up();
        if (!world.isAirBlock(plantPos)) {
            return;
        }
        if (random.nextInt(16) == 0) {
            world.setBlockState(plantPos, Blocks.DEADBUSH.getDefaultState(), WORLDGEN_FLAGS);
        } else {
            world.setBlockState(plantPos, Blocks.TALLGRASS.getDefaultState()
                    .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), WORLDGEN_FLAGS);
        }
    }

    private static void coverExposedMud(Random random, World world, int minX, int maxX, int minZ, int maxZ) {
        // The biome's base surface is mud.  The old land templates covered
        // that surface with vegetation, whereas the procedural hummocks only
        // touch water.  Restore a broken, mostly grassy cover to any exposed
        // mud so naturally generated flats do not read as sterile slabs.
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isQuagmire(world, x, z)) {
                    continue;
                }
                BlockPos ground = findTreeGround(world, x, z);
                if (world.getBlockState(ground).getBlock() != ModBlocks.mud_block
                        || !world.isAirBlock(ground.up()) || random.nextInt(8) == 0) {
                    continue;
                }
                world.setBlockState(ground, ModBlocks.peat_grass.getDefaultState(), WORLDGEN_FLAGS);
                if (random.nextInt(3) == 0) {
                    IBlockState plant = random.nextInt(12) == 0
                            ? Blocks.DEADBUSH.getDefaultState()
                            : Blocks.TALLGRASS.getDefaultState()
                                    .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
                    world.setBlockState(ground.up(), plant, WORLDGEN_FLAGS);
                }
            }
        }
    }

    private static void generateTrees(Random random, World world, int minX, int maxX, int minZ, int maxZ) {
        for (int attempt = 0; attempt < 1; attempt++) {
            if (random.nextInt(5) != 0) {
                continue;
            }
            int x = minX + 3 + random.nextInt(maxX - minX - 5);
            int z = minZ + 3 + random.nextInt(maxZ - minZ - 5);
            if (!isQuagmire(world, x, z)) {
                continue;
            }
            BlockPos ground = findTreeGround(world, x, z);
            if (world.getBlockState(ground).getBlock() != ModBlocks.peat_grass) {
                continue;
            }
            if (random.nextInt(3) == 0) {
                generateFallenTree(random, world, ground);
            } else {
                generateTree(random, world, ground);
            }
        }
    }

    private static void generateTree(Random random, World world, BlockPos ground) {
        // Quagmire trees are stunted, dying snags.  Keep their silhouette
        // deliberately open and asymmetric instead of growing a small oak.
        int height = 3 + random.nextInt(3);
        if (!hasTreeClearance(world, ground, height, 2)) {
            return;
        }

        IBlockState log = Blocks.LOG.getDefaultState();
        IBlockState leaves = Blocks.LEAVES.getDefaultState();
        BlockPos trunk = ground;
        for (int y = 1; y <= height; y++) {
            trunk = trunk.up();
            world.setBlockState(trunk, log, WORLDGEN_FLAGS);

            // A bent upper trunk stops every specimen from reading as a
            // perfectly vertical sapling.
            if (y >= 2 && y < height && random.nextInt(4) == 0) {
                EnumFacing bend = EnumFacing.Plane.HORIZONTAL.random(random);
                BlockPos bentTrunk = trunk.offset(bend);
                if (world.isAirBlock(bentTrunk)) {
                    world.setBlockState(bentTrunk, log, WORLDGEN_FLAGS);
                    trunk = bentTrunk;
                }
            }
        }

        // One or two short, uneven dead limbs are the whole crown.  A leaf
        // remnant is exceptional, and never forms a canopy.
        int branches = 1 + random.nextInt(2);
        for (int branchIndex = 0; branchIndex < branches; branchIndex++) {
            EnumFacing direction = EnumFacing.Plane.HORIZONTAL.random(random);
            int branchY = Math.max(2, height - random.nextInt(3));
            BlockPos branch = ground.up(branchY).offset(direction);
            if (!world.isAirBlock(branch)) {
                continue;
            }
            world.setBlockState(branch, log, WORLDGEN_FLAGS);
            if (random.nextBoolean()) {
                BlockPos tip = branch.offset(direction);
                if (world.isAirBlock(tip)) {
                    world.setBlockState(tip, log, WORLDGEN_FLAGS);
                    branch = tip;
                }
            }
        }
        if (random.nextInt(4) == 0) {
            BlockPos leaf = trunk.offset(EnumFacing.Plane.HORIZONTAL.random(random));
            if (world.isAirBlock(leaf)) {
                world.setBlockState(leaf, leaves, WORLDGEN_FLAGS);
            }
        }
    }

    private static void generateFallenTree(Random random, World world, BlockPos ground) {
        EnumFacing direction = EnumFacing.Plane.HORIZONTAL.random(random);
        int length = 2 + random.nextInt(3);
        for (int offset = 0; offset < length; offset++) {
            BlockPos position = ground.offset(direction, offset).up();
            if (!world.isAirBlock(position)) {
                return;
            }
        }

        IBlockState log = Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS,
                direction.getAxis() == EnumFacing.Axis.X ? BlockLog.EnumAxis.X : BlockLog.EnumAxis.Z);
        for (int offset = 0; offset < length; offset++) {
            world.setBlockState(ground.offset(direction, offset).up(), log, WORLDGEN_FLAGS);
        }
        if (random.nextBoolean()) {
            BlockPos brokenBranch = ground.offset(direction, length - 1).up().offset(EnumFacing.UP);
            if (world.isAirBlock(brokenBranch)) {
                world.setBlockState(brokenBranch, Blocks.LOG.getDefaultState(), WORLDGEN_FLAGS);
            }
        }
    }

    private static BlockPos findTreeGround(World world, int x, int z) {
        BlockPos ground = world.getHeight(new BlockPos(x, 0, z)).down();
        while (ground.getY() > 0 && (world.isAirBlock(ground)
                || world.getBlockState(ground).getBlock().isReplaceable(world, ground))) {
            ground = ground.down();
        }
        return ground;
    }

    private static boolean hasTreeClearance(World world, BlockPos ground, int height, int branchRadius) {
        for (int y = 1; y <= height + 1; y++) {
            int radius = y < 2 ? 0 : branchRadius;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos check = ground.add(dx, y, dz);
                    if (!world.isAirBlock(check)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}

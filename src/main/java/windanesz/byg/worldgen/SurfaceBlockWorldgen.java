package windanesz.byg.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

final class SurfaceBlockWorldgen {
    private SurfaceBlockWorldgen() {
    }

    static void generatePeatgrass(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_pine_mountains", "byg:byg_boreal_forest", "byg:byg_coniferous_forest", "byg:byg_pine_lowlands")) {
            return;
        }
        for (int i = 0; i < 32; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 32);
            int y = random.nextInt(252) + 2;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 32);
            new FastMinable(ModBlocks.peat_grass.getDefaultState(), 32, new Predicate<IBlockState>() {
                @Override
                public boolean apply(IBlockState blockAt) {
                    return blockAt.getBlock() == Blocks.GRASS;
                }
            }).generate(world, random, new BlockPos(x, y, z));
        }
    }

    static void generateRockyGrass(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_bluff_mountains", "byg:byg_stone_brushlands")) {
            return;
        }
        for (int i = 0; i < 32; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 32);
            int y = random.nextInt(138) + 70;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 32);
            new FastMinable(ModBlocks.rocky_grass.getDefaultState(), 32, new Predicate<IBlockState>() {
                @Override
                public boolean apply(IBlockState blockAt) {
                    return blockAt.getBlock() == Blocks.GRASS || blockAt.getBlock() == ModBlocks.peat_grass;
                }
            }).generate(world, random, new BlockPos(x, y, z));
        }
    }

    static void generateRockyGrassAlps(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ, "byg:byg_alps")) {
            return;
        }
        for (int i = 0; i < 32; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 32);
            int y = random.nextInt(10) + 153;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 32);
            new FastMinable(ModBlocks.rocky_grass.getDefaultState(), 32, new Predicate<IBlockState>() {
                @Override
                public boolean apply(IBlockState blockAt) {
                    return blockAt.getBlock() == Blocks.SNOW;
                }
            }).generate(world, random, new BlockPos(x, y, z));
        }
    }

    static void generateRockystone(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_snowy_pine_mountains", "byg:byg_pine_mountains")) {
            return;
        }
        generateRockystoneVeins(random, blockX, blockZ, world, 117, 137, false);
    }

    static void generateRockystoneInBluffs(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_bluff_mountains", "byg:byg_stone_brushlands")) {
            return;
        }
        generateRockystoneVeins(random, blockX, blockZ, world, 70, 138, true);
    }

    private static void generateRockystoneVeins(Random random, int blockX, int blockZ, World world,
                                                int minY, int yRange, boolean replaceRockyGrass) {
        Predicate<IBlockState> replaceable = blockAt -> blockAt.getBlock() == Blocks.GRASS
                || blockAt.getBlock() == ModBlocks.peat_grass
                || (replaceRockyGrass && blockAt.getBlock() == ModBlocks.rocky_grass);
        for (int i = 0; i < 32; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 32);
            int y = random.nextInt(yRange) + minY;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 32);
            new FastMinable(ModBlocks.rocky_stone.getDefaultState(), 32, replaceable)
                    .generate(world, random, new BlockPos(x, y, z));
        }
    }

    static void generateSandygrass(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_lush_desert", "byg:byg_outback")) {
            return;
        }
        for (int i = 0; i < 32; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 30);
            int y = random.nextInt(42) + 60;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 30);
            new FastMinable(ModBlocks.sandy_grass.getDefaultState(), 32, new Predicate<IBlockState>() {
                @Override
                public boolean apply(IBlockState blockAt) {
                    if (blockAt.getBlock() == Blocks.HARDENED_CLAY || blockAt.getBlock() == ModBlocks.hardened_dirt) {
                        return true;
                    }
                    return blockAt.getBlock() == Blocks.SAND;
                }
            }).generate(world, random, new BlockPos(x, y, z));
        }
    }

    static void generateSodalite(Random random, int blockX, int blockZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, blockX, blockZ,
                "byg:byg_sonoran_desert", "byg:byg_shrublands", "byg:byg_savanna_canopy", "byg:byg_dunes", "byg:byg_red_desert",
                "byg:byg_dead_sea", "byg:byg_baobab_savanna", "byg:byg_chaparral_lowlands", "byg:byg_outback",
                "byg:byg_lush_desert", "desert", "desert_hills", "savanna", "savanna_rock", "mesa", "mesa_rock",
                "mesa_clear_rock", "mutated_desert", "mutated_savanna", "mutated_mesa")) {
            return;
        }
        for (int i = 0; i < 30; ++i) {
            int x = BygWorldGenerator.safeMinableCoordinate(random, blockX, 30);
            int y = random.nextInt(135) + 45;
            int z = BygWorldGenerator.safeMinableCoordinate(random, blockZ, 30);
            new FastMinable(ModBlocks.sodalite.getDefaultState(), 30, new Predicate<IBlockState>() {
                @Override
                public boolean apply(IBlockState blockAt) {
                    return blockAt.getBlock() == Blocks.STONE;
                }
            }).generate(world, random, new BlockPos(x, y, z));
        }
    }
}



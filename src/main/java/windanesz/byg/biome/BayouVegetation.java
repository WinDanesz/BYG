package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

final class BayouVegetation {
    private BayouVegetation() {
    }

    static void decorate(World world, Random random, BlockPos chunkPos, Biome biome) {
        for (int attempt = 0; attempt < 180; attempt++) {
            BlockPos plantPos = world.getHeight(chunkPos.add(random.nextInt(16) + 8, 0,
                    random.nextInt(16) + 8));
            if (world.getBiome(plantPos) != biome || !world.isAirBlock(plantPos)) {
                continue;
            }
            BlockPos groundPos = plantPos.down();
            Block ground = world.getBlockState(groundPos).getBlock();
            if (ground != Blocks.GRASS && ground != Blocks.DIRT && ground != ModBlocks.mud_block) {
                continue;
            }

            boolean shoreline = false;
            for (int dx = -2; dx <= 2 && !shoreline; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos waterPos = groundPos.add(dx, 0, dz);
                    if (world.getBlockState(waterPos).getMaterial() == Material.WATER
                            || world.getBlockState(waterPos.down()).getMaterial() == Material.WATER) {
                        shoreline = true;
                        break;
                    }
                }
            }

            if (shoreline && random.nextInt(4) != 0) {
                Block plant = random.nextInt(3) == 0 ? ModBlocks.cattails : ModBlocks.reed;
                if (plant == null) {
                    plant = ModBlocks.cattails != null ? ModBlocks.cattails : ModBlocks.reed;
                }
                if (plant != null) {
                    world.setBlockState(plantPos, plant.getDefaultState(), 2);
                }
            } else if (ground == Blocks.GRASS && world.isAirBlock(plantPos.up())
                    && random.nextBoolean()) {
                world.setBlockState(plantPos, Blocks.DOUBLE_PLANT.getDefaultState()
                        .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                        .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 2);
                world.setBlockState(plantPos.up(), Blocks.DOUBLE_PLANT.getDefaultState()
                        .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                        .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
            }
        }
    }
}

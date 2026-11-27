package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public class WorldGenDeadSeaRocks extends WorldGenerator {
    private static final ResourceLocation DEAD_SEA = new ResourceLocation("byg", "byg_dead_sea");
    private static final IBlockState ROCK = ModBlocks.rocky_stone.getDefaultState();
    private static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private static final IBlockState MOSSY = Blocks.MOSSY_COBBLESTONE.getDefaultState();

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        int halfSpan = 9 + rand.nextInt(5);
        int height = 11 + rand.nextInt(5);
        boolean alongX = rand.nextBoolean();
        int floor = findFloor(world, position);
        if (floor < 0 || floor + height >= world.getHeight()) return false;

        int length = halfSpan + 2;
        int clearanceWidth = 2;
        if (!world.isAreaLoaded(position.add(alongX ? -length : -clearanceWidth, 1,
                        alongX ? -clearanceWidth : -length),
                position.add(alongX ? length : clearanceWidth, world.getSeaLevel() + height + 3,
                        alongX ? clearanceWidth : length))) return false;

        for (int d = -halfSpan; d <= halfSpan; d++) {
            BlockPos column = position.add(alongX ? d : 0, 0, alongX ? 0 : d);
            int localFloor = findFloor(world, column);
            if (localFloor < 0 || Math.abs(localFloor - floor) > 4) return false;
        }

        // Only the two ends are solid from the seabed up; the middle stays open.
        for (int d = -halfSpan; d <= halfSpan; d++) {
            int rise = (int) Math.round(height * Math.sqrt(Math.max(0,
                    1.0 - (double) (d * d) / (halfSpan * halfSpan))));
            boolean support = Math.abs(d) >= halfSpan - 2;
            int bottom = support ? 0 : Math.max(3, rise - 2);
            int width = support ? 2 : 1;
            for (int side = -width + 1; side < width; side++) {
                for (int y = bottom; y <= rise; y++) {
                    BlockPos target = new BlockPos(position.getX() + (alongX ? d : side),
                            floor + y, position.getZ() + (alongX ? side : d));
                    placeRock(world, rand, target);
                }
                // The ends are measured from the floor at the middle, so carry them down to the seabed where it dips
                if (support) {
                    fillDown(world, rand, new BlockPos(position.getX() + (alongX ? d : side), floor - 1,
                            position.getZ() + (alongX ? side : d)));
                }
            }
        }

        // Some arches stand alone; others have small outcrops away from the opening.
        int outcrops = rand.nextInt(4);
        for (int i = 0; i < outcrops; i++) {
            int distance = halfSpan + 7 + rand.nextInt(9);
            int lateral = 6 + rand.nextInt(9);
            int longOffset = rand.nextBoolean() ? distance : -distance;
            int sideOffset = rand.nextBoolean() ? lateral : -lateral;
            BlockPos outcrop = position.add(alongX ? longOffset : sideOffset, 0,
                    alongX ? sideOffset : longOffset);
            if (!world.isAreaLoaded(outcrop.add(-2, 1, -2),
                    outcrop.add(2, world.getSeaLevel() + 5, 2))) continue;
            int outcropFloor = findFloor(world, outcrop);
            if (outcropFloor < 0) continue;
            int outcropHeight = 2 + rand.nextInt(4);
            for (int y = 0; y < outcropHeight; y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (Math.abs(x) + Math.abs(z) > (y == 0 ? 1 : 0)) continue;
                        placeRock(world, rand, new BlockPos(outcrop.getX() + x, outcropFloor + y,
                                outcrop.getZ() + z));
                    }
                }
            }
        }
        return true;
    }

    static int findFloor(World world, BlockPos column) {
        BlockPos bed = new BlockPos(column.getX(), world.getSeaLevel(), column.getZ());
        if (!DEAD_SEA.equals(world.getBiome(bed).getRegistryName())) return -1;
        for (int i = 0; i < 3 && world.isAirBlock(bed); i++) bed = bed.down();
        if (world.getBlockState(bed).getBlock() != Blocks.WATER) return -1;
        while (bed.getY() > 1 && world.getBlockState(bed.down()).getBlock() == Blocks.WATER) bed = bed.down();
        return world.getBlockState(bed.down()).getBlock() == ModBlocks.black_sand ? bed.getY() : -1;
    }

    /** Fills water from {@code top} down to the seabed so the block above it rests on the ground. */
    static void fillDown(World world, Random rand, BlockPos top) {
        for (BlockPos pos = top; pos.getY() > 0 && world.getBlockState(pos).getBlock() == Blocks.WATER; pos = pos.down()) {
            world.setBlockState(pos, pickRock(rand), 2);
        }
    }

    static IBlockState pickRock(Random rand) {
        int palette = rand.nextInt(10);
        return palette < 6 ? ROCK : palette < 9 ? STONE : MOSSY;
    }

    private void placeRock(World world, Random rand, BlockPos target) {
        if (world.getBlockState(target).getBlock() != Blocks.WATER && !world.isAirBlock(target)) return;
        world.setBlockState(target, pickRock(rand), 2);
    }
}

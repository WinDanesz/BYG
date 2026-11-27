package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDeadSeaPillar extends WorldGenerator {

    // Writes stay inside the two-chunk window that decorate() may touch, minus a 1-block margin because
    // setBlockState notifies neighbours (observers), which would read into an unpopulated chunk.
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        int originX = (position.getX() - 8) & ~15;
        int originZ = (position.getZ() - 8) & ~15;
        minX = originX + 9;
        maxX = originX + 22;
        minZ = originZ + 9;
        maxZ = originZ + 22;
        if (position.getX() < minX || position.getX() > maxX || position.getZ() < minZ || position.getZ() > maxZ) return false;
        if (!world.isAreaLoaded(position.add(-9, 1, -9), position.add(9, world.getSeaLevel() + 6, 9))) return false;
        int floor = WorldGenDeadSeaRocks.findFloor(world, position);
        if (floor < 0) return false;
        for (int x = -2; x <= 2; x += 4) {
            for (int z = -2; z <= 2; z += 4) {
                int local = WorldGenDeadSeaRocks.findFloor(world, position.add(x, 0, z));
                if (local < 0 || Math.abs(local - floor) > 3) return false;
            }
        }

        int height = Math.min(world.getSeaLevel() - floor + 3, 10 + rand.nextInt(10));
        if (height < 6) return false;
        buildSpire(world, rand, position, floor, height, true);

        int satellites = rand.nextInt(3);
        for (int i = 0; i < satellites; i++) {
            BlockPos spot = position.add((rand.nextBoolean() ? 1 : -1) * (4 + rand.nextInt(4)), 0,
                    (rand.nextBoolean() ? 1 : -1) * (4 + rand.nextInt(4)));
            int spotFloor = WorldGenDeadSeaRocks.findFloor(world, spot);
            if (spotFloor >= 0 && Math.abs(spotFloor - floor) <= 3) {
                buildSpire(world, rand, spot, spotFloor, 4 + rand.nextInt(5), false);
            }
        }
        return true;
    }

    private void buildSpire(World world, Random rand, BlockPos base, int floor, int height, boolean cap) {
        int footHeight = Math.max(2, height / 3);
        int lean = 0;
        int leanX = 0;
        int leanZ = 0;
        for (int y = -1; y < height; y++) {
            int radius = y < footHeight ? (y < footHeight / 2 ? 3 : 2) : 1;
            if (!cap && y >= footHeight) radius = y >= height - 2 ? 0 : 1;
            // The neck drifts a block sideways now and then, like real eroded stone.
            if (y > footHeight && ++lean % 4 == 0 && rand.nextInt(3) == 0) {
                leanX += rand.nextInt(3) - 1;
                leanZ += rand.nextInt(3) - 1;
                leanX = Math.max(-1, Math.min(1, leanX));
                leanZ = Math.max(-1, Math.min(1, leanZ));
            }
            disc(world, rand, base.getX() + (y > footHeight ? leanX : 0), floor + y,
                    base.getZ() + (y > footHeight ? leanZ : 0), radius, y == -1);
        }
        if (cap) {
            int capY = floor + height;
            int cx = base.getX() + leanX;
            int cz = base.getZ() + leanZ;
            if (rand.nextInt(3) != 0) {
                // Wider caprock overhanging the neck.
                disc(world, rand, cx, capY, cz, 3, false);
                disc(world, rand, cx, capY + 1, cz, 2, false);
                if (rand.nextBoolean()) disc(world, rand, cx, capY + 2, cz, 1, false);
            } else {
                disc(world, rand, cx, capY, cz, 1, false);
                disc(world, rand, cx, capY + 1, cz, 0, false);
            }
        }
    }

    /** Fills a rough disc; edge blocks are randomly dropped so outlines are never perfect. */
    private void disc(World world, Random rand, int cx, int y, int cz, int radius, boolean settle) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int d2 = x * x + z * z;
                if (d2 > radius * radius + (radius > 1 ? 1 : 0)) continue;
                if (radius > 1 && d2 >= radius * radius - 1 && rand.nextInt(3) == 0) continue;
                BlockPos pos = new BlockPos(cx + x, y, cz + z);
                place(world, pos, WorldGenDeadSeaRocks.pickRock(rand));
                // The foot is laid at the floor under the spire's centre; reach down where the seabed falls away
                if (settle && inWindow(pos)) WorldGenDeadSeaRocks.fillDown(world, rand, pos.down());
            }
        }
    }

    private boolean inWindow(BlockPos pos) {
        return pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    private void place(World world, BlockPos pos, IBlockState state) {
        if (!inWindow(pos)) return;
        if (world.getBlockState(pos).getBlock() == Blocks.WATER || world.isAirBlock(pos)) {
            world.setBlockState(pos, state, 2);
        }
    }
}

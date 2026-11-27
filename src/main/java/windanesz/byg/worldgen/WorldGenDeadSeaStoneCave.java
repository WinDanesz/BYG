package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDeadSeaStoneCave extends WorldGenerator {
    private static final ResourceLocation LOOT = new ResourceLocation("byg", "chests/dead_sea_shipwreck");

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        int radius = 4 + rand.nextInt(2);
        if (!world.isAreaLoaded(position.add(-radius - 2, 1, -radius - 2),
                position.add(radius + 2, world.getSeaLevel() + 2, radius + 2))) return false;

        int floor = WorldGenDeadSeaRocks.findFloor(world, position);
        if (floor < 0 || floor + radius + 2 >= world.getSeaLevel() + 1) return false;
        for (int x = -radius; x <= radius; x += radius) {
            for (int z = -radius; z <= radius; z += radius) {
                int local = WorldGenDeadSeaRocks.findFloor(world, position.add(x, 0, z));
                if (local < 0 || Math.abs(local - floor) > 2) return false;
            }
        }

        EnumFacing entrance = EnumFacing.Plane.HORIZONTAL.random(rand);
        double inner = radius - 2.0;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -1; dy <= radius; dy++) {
                    double dist = Math.sqrt(dx * dx + dz * dz + (dy + 0.5) * (dy + 0.5) * 1.2);
                    if (dist > radius + 0.4 || dist <= inner) continue;
                    if (isEntrance(dx, dz, dy, entrance, radius)) continue;
                    BlockPos pos = position.add(dx, floor - position.getY() + dy, dz);
                    place(world, pos, WorldGenDeadSeaRocks.pickRock(rand));
                    // The dome is laid at the floor under its centre; reach down where the seabed falls away
                    if (dy == -1) WorldGenDeadSeaRocks.fillDown(world, rand, pos.down());
                }
            }
        }

        // Ceiling light.
        for (int y = 1; y <= radius + 1; y++) {
            BlockPos ceiling = new BlockPos(position.getX(), floor + y, position.getZ());
            if (world.getBlockState(ceiling).getBlock() != Blocks.WATER) {
                world.setBlockState(ceiling, Blocks.SEA_LANTERN.getDefaultState(), 2);
                break;
            }
        }
        // Occasionally the cave holds a chest against the back wall.
        if (rand.nextInt(4) == 0) {
            BlockPos chest = new BlockPos(position.getX() - entrance.getXOffset(), floor,
                    position.getZ() - entrance.getZOffset());
            if (world.getBlockState(chest).getBlock() == Blocks.WATER) {
                world.setBlockState(chest, Blocks.CHEST.getDefaultState(), 2);
                TileEntity tile = world.getTileEntity(chest);
                if (tile instanceof TileEntityChest) ((TileEntityChest) tile).setLootTable(LOOT, rand.nextLong());
            }
        }
        return true;
    }

    /** A two-wide, two-tall opening on one side, at seabed level. */
    private boolean isEntrance(int dx, int dz, int dy, EnumFacing facing, int radius) {
        if (dy < 0 || dy > 1) return false;
        int along = dx * facing.getXOffset() + dz * facing.getZOffset();
        int side = facing.getAxis() == EnumFacing.Axis.X ? dz : dx;
        return along > 0 && side >= 0 && side <= 1;
    }

    private void place(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == Blocks.WATER || world.isAirBlock(pos)) {
            world.setBlockState(pos, state, 2);
        }
    }
}

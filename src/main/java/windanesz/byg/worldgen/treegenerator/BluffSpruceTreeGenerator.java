package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class BluffSpruceTreeGenerator extends WoodlandTreeGenerator {
    private static final int BARE = 5;
    private static final int TIER_HEIGHT = 4;

    @Override
    protected boolean canGrowOn(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == ModBlocks.rocky_grass;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int tiers = 3 + random.nextInt(2);
        int spire = BARE + tiers * TIER_HEIGHT;
        // The trunk stops two blocks into the last tier so the spire leaf is not overwritten by wood.
        int trunkTop = spire - 2;
        if (!this.canStandAt(world, origin, trunkTop + 1, 3, spire + 1)) {
            return false;
        }
        IBlockState log = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE)
                .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
        IBlockState leaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
                .withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false);

        for (int tier = 0; tier < tiers; tier++) {
            int y = BARE + tier * TIER_HEIGHT;
            // Skirt: 5x5 without corners, rim thinned so the edge is ragged.
            ring(world, random, origin.up(y), 2, false, 0.85, leaves);
            ring(world, random, origin.up(y + 1), 1, true, 1.0, leaves);
            ring(world, random, origin.up(y + 2), 1, false, 0.8, leaves);
            ring(world, random, origin.up(y + 3), 1, true, 1.0, leaves);
        }
        this.placeLeaf(world, origin.up(spire), leaves);
        for (int y = 0; y <= trunkTop; y++) {
            this.placeLog(world, origin.up(y), log);
        }
        return true;
    }

    /** One leaf layer: a plus when {@code plus}, otherwise a corner-clipped square of the given radius. */
    private void ring(World world, Random random, BlockPos centre, int radius, boolean plus, double density, IBlockState leaves) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                boolean core = dx == 0 && dz == 0;
                int i = Math.abs(dx) + Math.abs(dz);
                if (plus ? (dx != 0 && dz != 0) : i > radius + (1)) {
                    continue;
                }
                if (core || i <= 1 || random.nextDouble() < density) {
                    this.placeLeaf(world, centre.add(dx, 0, dz), leaves);
                }
            }
        }
    }
}

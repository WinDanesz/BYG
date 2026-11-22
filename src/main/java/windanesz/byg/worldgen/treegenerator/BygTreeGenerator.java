package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.function.BooleanSupplier;

/**
 * Base for BYG trees that both biomes and saplings place. Worldgen writes skip observer updates, which read the
 * neighbouring block: on the edge of the population window that loads the next chunk and cascades worldgen.
 * Sapling growth keeps the usual updates so observers still notice the tree.
 */
public abstract class BygTreeGenerator extends WorldGenAbstractTree {
    private static final int WORLDGEN_FLAGS = 2 | 16;
    private static final ThreadLocal<Boolean> GROWING = new ThreadLocal<>();

    protected BygTreeGenerator() {
        super(false);
    }

    protected static boolean grow(BooleanSupplier growth) {
        GROWING.set(Boolean.TRUE);
        try {
            return growth.getAsBoolean();
        } finally {
            GROWING.remove();
        }
    }

    protected static boolean isGrowing() {
        return GROWING.get() != null;
    }

    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        if (isGrowing()) {
            super.setBlockAndNotifyAdequately(world, pos, state);
        } else {
            world.setBlockState(pos, state, WORLDGEN_FLAGS);
        }
    }
}

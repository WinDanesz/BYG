package windanesz.byg.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockDirectionalLogBaseTest {
    private static BlockDirectionalLogBase log;

    @BeforeAll
    static void bootstrap() {
        Bootstrap.register();
        log = new BlockDirectionalLogBase("axis_test_log", 0, false);
    }

    @Test
    void logsSavedWithAFacingKeepTheirAxis() {
        // The facings the logs were saved with before they were vanilla-style logs
        assertEquals(BlockLog.EnumAxis.X, axis(0));
        assertEquals(BlockLog.EnumAxis.X, axis(1));
        assertEquals(BlockLog.EnumAxis.Y, axis(2));
        assertEquals(BlockLog.EnumAxis.Y, axis(3));
        assertEquals(BlockLog.EnumAxis.Z, axis(4));
        assertEquals(BlockLog.EnumAxis.Z, axis(5));
    }

    @Test
    void everyAxisSurvivesSavingAndLoading() {
        for (BlockLog.EnumAxis axis : BlockLog.EnumAxis.values()) {
            IBlockState state = log.getDefaultState().withProperty(BlockLog.LOG_AXIS, axis);
            assertEquals(state, log.getStateFromMeta(log.getMetaFromState(state)), axis.getName());
        }
    }

    @Test
    void newLogsStandUpright() {
        assertEquals(BlockLog.EnumAxis.Y, log.getDefaultState().getValue(BlockLog.LOG_AXIS));
        assertTrue(log.isWood(null, null));
    }

    @Test
    void slabsUseVanillasHalf() {
        BlockWoodSlabBase slab = new BlockWoodSlabBase("half_test_slab", 0);
        IBlockState top = slab.getStateFromMeta(1);
        assertEquals(BlockSlab.EnumBlockHalf.TOP, top.getValue(BlockSlab.HALF));
        assertEquals(1, slab.getMetaFromState(top));
        assertEquals(BlockSlab.EnumBlockHalf.BOTTOM, slab.getDefaultState().getValue(BlockSlab.HALF));
    }

    private static BlockLog.EnumAxis axis(int meta) {
        return log.getStateFromMeta(meta).getValue(BlockLog.LOG_AXIS);
    }
}

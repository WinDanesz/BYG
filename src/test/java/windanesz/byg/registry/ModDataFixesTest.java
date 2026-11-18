package windanesz.byg.registry;

import net.minecraft.block.BlockLog;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Test;
import windanesz.byg.blocks.BlockDirectionalLogBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModDataFixesTest {

    @Test
    void structuresAndWorldsReadAnOldLogAsTheSameAxis() {
        Bootstrap.register();
        BlockDirectionalLogBase log = new BlockDirectionalLogBase("fix_test_log", 0, false);
        for (EnumFacing facing : EnumFacing.values()) {
            String fromWorld = log.getStateFromMeta(facing.getIndex()).getValue(BlockLog.LOG_AXIS).getName();
            assertEquals(fromWorld, ModDataFixes.LogAxisFix.axisOf(facing.getName()), facing.getName());
        }
    }
}

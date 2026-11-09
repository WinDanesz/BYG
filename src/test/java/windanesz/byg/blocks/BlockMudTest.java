package windanesz.byg.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

class BlockMudTest {

    private static IBlockState mud;

    @BeforeAll
    static void bootstrap() {
        Bootstrap.register();
        mud = new BlockMud().getDefaultState();
    }

    @Test
    void isAnOrdinarySolidBlock() {
        assertTrue(mud.isOpaqueCube());
        assertTrue(mud.isFullCube());
        assertTrue(mud.isTopSolid());
        assertEquals(Block.FULL_BLOCK_AABB, mud.getBoundingBox(null, BlockPos.ORIGIN));
        for (EnumFacing face : EnumFacing.values()) {
            assertEquals(BlockFaceShape.SOLID, mud.getBlockFaceShape(null, BlockPos.ORIGIN, face), face.getName());
        }
    }

    @Test
    void entitiesSinkInSoTheSlowdownStillApplies() {
        assertEquals(0.9D, mud.getCollisionBoundingBox(null, BlockPos.ORIGIN).maxY, 1.0E-9D);
    }
}

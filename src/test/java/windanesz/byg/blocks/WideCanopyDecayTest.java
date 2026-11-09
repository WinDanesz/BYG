package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator.TreeStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WideCanopyDecayTest {
    private static final BlockPos ROOT = new BlockPos(0, 64, 0);
    private static Block log;
    private static Block strippedLog;
    private static BlockBygLeaves leaves;

    @BeforeAll
    static void bootstrap() {
        Bootstrap.register();
        log = new BlockDirectionalLogBase("canopy_test_log", 0, false, true);
        strippedLog = new BlockDirectionalLogBase("canopy_test_stripped_log", 0, false, true);
        leaves = new BlockFruitLeavesBase("canopy_test_leaves", 0, () -> ItemStack.EMPTY) {
            @Override
            public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
                // The test world tracks blocks without spawning entities.
            }
        }.withCanopySupport(block -> block == log || block == strippedLog);
    }

    @Test
    void intactCrownsSurviveDecayChecks() {
        checkCrowns(TreeStyle.HOLLY, 12, 2);
        checkCrowns(TreeStyle.HOLLY, 16, 3);
        checkCrowns(TreeStyle.MAPLE, 7, 3);
        checkCrowns(TreeStyle.CHERRY_BLOSSOM, 5, 2);
        checkCrowns(TreeStyle.CHERRY_UMBRELLA, 10, 1);
        checkCrowns(TreeStyle.CHERRY_SPREADING, 6, 1);
        checkCrowns(TreeStyle.CHERRY_GRAND, 18, 2);
        checkCrowns(TreeStyle.CHERRY_ANCIENT, 23, 2);
    }

    private static void checkCrowns(TreeStyle style, int height, int extraHeight) {
        SaplingTreeGenerator tree = new SaplingTreeGenerator(log::getDefaultState, leaves::getDefaultState,
                style, height, extraHeight);
        Random seeds = new Random(15);
        for (int i = 0; i < 8; i++) {
            TestWorld world = new TestWorld();
            for (BlockPos soil : BlockPos.getAllInBox(ROOT.down(), ROOT.add(1, -1, 1))) {
                world.setBlockState(soil, Blocks.GRASS.getDefaultState(), 2);
            }
            assertTrue(tree.generate(world, new Random(seeds.nextLong()), ROOT), style.name());
            for (Map.Entry<BlockPos, IBlockState> entry : new HashMap<>(world.blocks).entrySet()) {
                if (entry.getValue().getBlock() == leaves) {
                    BlockPos pos = entry.getKey();
                    leaves.beginLeavesDecay(entry.getValue(), world, pos);
                    leaves.updateTick(world, pos, world.getBlockState(pos), world.rand);
                    assertSame(leaves.getDefaultState(), world.getBlockState(pos), style + " at " + pos);
                }
            }
        }
    }

    @Test
    void removingTheLastLogDecaysNaturalLeavesButKeepsPlacedLeaves() {
        TestWorld world = new TestWorld();
        BlockPos natural = ROOT.east(8);
        BlockPos placed = ROOT.west(8);
        world.setBlockState(ROOT, log.getDefaultState(), 2);
        world.setBlockState(natural, leaves.getDefaultState(), 2);
        IBlockState placedState = leaves.getStateForPlacement(world, placed, EnumFacing.UP, 0, 0, 0, 0, null);
        world.setBlockState(placed, placedState, 2);

        world.setBlockState(ROOT, strippedLog.getDefaultState(), 2);
        assertFalse(world.getBlockState(natural).getValue(BlockLeaves.CHECK_DECAY));
        world.setBlockToAir(ROOT);
        assertTrue(world.getBlockState(natural).getValue(BlockLeaves.CHECK_DECAY));

        world.areaLoaded = false;
        leaves.updateTick(world, natural, world.getBlockState(natural), world.rand);
        assertTrue(world.getBlockState(natural).getValue(BlockLeaves.CHECK_DECAY));
        world.areaLoaded = true;
        leaves.updateTick(world, natural, world.getBlockState(natural), world.rand);
        assertSame(Blocks.AIR, world.getBlockState(natural).getBlock());
        assertEquals(placedState, world.getBlockState(placed));
    }

    private static final class TestWorld extends World {
        private final Map<BlockPos, IBlockState> blocks = new HashMap<>();
        private boolean areaLoaded = true;

        TestWorld() {
            super(null, null, new WorldProviderSurface(), new Profiler(), false);
        }

        @Override
        protected IChunkProvider createChunkProvider() {
            return null;
        }

        @Override
        protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
            return areaLoaded;
        }

        @Override
        public IBlockState getBlockState(BlockPos pos) {
            return blocks.getOrDefault(pos, Blocks.AIR.getDefaultState());
        }

        /** Tree placement reads the heightmap; this world has no chunks, so derive it from the stored blocks. */
        @Override
        public int getHeight(int x, int z) {
            int height = 0;
            for (Map.Entry<BlockPos, IBlockState> entry : blocks.entrySet()) {
                BlockPos pos = entry.getKey();
                if (pos.getX() == x && pos.getZ() == z && entry.getValue().getBlock() != Blocks.AIR) {
                    height = Math.max(height, pos.getY() + 1);
                }
            }
            return height;
        }

        @Override
        public boolean setBlockState(BlockPos pos, IBlockState state, int flags) {
            IBlockState previous = getBlockState(pos);
            blocks.put(pos.toImmutable(), state);
            if (previous.getBlock() != state.getBlock()) {
                previous.getBlock().breakBlock(this, pos, previous);
            }
            return true;
        }
    }
}

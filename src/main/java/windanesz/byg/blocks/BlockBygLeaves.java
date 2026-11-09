package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Base for BYG leaves. Extends vanilla {@link BlockLeaves} so vanilla leaf decay applies and mods that detect
 * leaves by class (RLFoliage/BetterFoliage, tree fellers, leaf decay mods) recognise BYG leaves.
 */
public abstract class BlockBygLeaves extends BlockLeaves {
    private static final int CANOPY_SUPPORT_RANGE = 8;
    private final BlockRenderLayer renderLayer;
    private Predicate<Block> canopyLog;

    protected BlockBygLeaves(String name, int harvestLevel, float lightLevel, BlockRenderLayer renderLayer) {
        this.renderLayer = renderLayer;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(0.2f);
        this.setResistance(5.0f);
        this.setLightLevel(lightLevel);
        this.setLightOpacity(1);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    // Bit 2 marks non-decaying leaves, so leaves saved before DECAYABLE existed (meta 0/1) still decay.
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CHECK_DECAY, (meta & 1) != 0).withProperty(DECAYABLE, (meta & 2) == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(CHECK_DECAY) ? 1 : 0) | (state.getValue(DECAYABLE) ? 0 : 2);
    }

    /** Player-placed leaves never decay, like vanilla leaves. */
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(DECAYABLE, false);
    }

    /** Wide crowns and sparse foliage can stay supported by their own logs across gaps. */
    public BlockBygLeaves withCanopySupport(Predicate<Block> canopyLog) {
        this.canopyLog = canopyLog;
        return this;
    }

    static void checkCanopyAfterLogBreak(World world, BlockPos pos, Block removedLog) {
        Block replacement = world.getBlockState(pos).getBlock();
        for (BlockPos.MutableBlockPos nearby : canopyPositions(pos)) {
            if (!withinCanopyRange(pos, nearby) || !world.isBlockLoaded(nearby)) {
                continue;
            }
            IBlockState state = world.getBlockState(nearby);
            if (state.getBlock() instanceof BlockBygLeaves) {
                BlockBygLeaves leaves = (BlockBygLeaves) state.getBlock();
                if (leaves.canopyLog != null && leaves.canopyLog.test(removedLog)
                        && !leaves.canopyLog.test(replacement) && state.getValue(DECAYABLE)) {
                    leaves.beginLeavesDecay(state, world, nearby.toImmutable());
                }
            }
        }
    }

    private static Iterable<BlockPos.MutableBlockPos> canopyPositions(BlockPos pos) {
        int range = CANOPY_SUPPORT_RANGE;
        return BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range), pos.add(range, range, range));
    }

    private static boolean withinCanopyRange(BlockPos center, BlockPos pos) {
        return Math.abs(center.getX() - pos.getX()) + Math.abs(center.getY() - pos.getY())
                + Math.abs(center.getZ() - pos.getZ()) <= CANOPY_SUPPORT_RANGE;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote || !state.getValue(CHECK_DECAY) || !state.getValue(DECAYABLE)) {
            return;
        }
        int range = Config.leafDecayRange();
        // Skip the check while part of the search area is unloaded, rather than deciding from missing blocks.
        int loadedRange = this.canopyLog == null ? range + 1 : Math.max(range + 1, CANOPY_SUPPORT_RANGE);
        if (!world.isAreaLoaded(pos, loadedRange)) {
            return;
        }
        if (hasCanopyLogNearby(world, pos) || isSupported(world, pos, range)) {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
        } else {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    /** Wide crowns and sparse foliage count as supported by any of their own logs in range, even across gaps. */
    private boolean hasCanopyLogNearby(World world, BlockPos pos) {
        if (this.canopyLog == null) {
            return false;
        }
        for (BlockPos.MutableBlockPos nearby : canopyPositions(pos)) {
            if (withinCanopyRange(pos, nearby) && this.canopyLog.test(world.getBlockState(nearby).getBlock())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSupported(World world, BlockPos origin, int range) {
        Set<Long> visited = new HashSet<>();
        visited.add(origin.toLong());
        List<BlockPos> frontier = new ArrayList<>();
        frontier.add(origin);
        for (int depth = 1; depth <= range && !frontier.isEmpty(); depth++) {
            List<BlockPos> next = new ArrayList<>();
            for (BlockPos current : frontier) {
                for (EnumFacing facing : EnumFacing.values()) {
                    BlockPos neighbour = current.offset(facing);
                    IBlockState neighbourState = world.getBlockState(neighbour);
                    Block block = neighbourState.getBlock();
                    if (block.canSustainLeaves(neighbourState, world, neighbour)) {
                        return true;
                    }
                    if (depth < range && block.isLeaves(neighbourState, world, neighbour) && visited.add(neighbour.toLong())) {
                        next.add(neighbour);
                    }
                }
            }
            frontier = next;
        }
        return false;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return LeavesGraphics.renderLayer(this.renderLayer);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return LeavesGraphics.isOpaque(this.renderLayer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        // The client only updates the graphics level on vanilla leaves; sync it before vanilla's face culling.
        this.leavesFancy = !LeavesGraphics.isOpaque(this.renderLayer);
        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }
}

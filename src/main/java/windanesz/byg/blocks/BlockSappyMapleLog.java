package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

/**
 * A standing maple log that weeps sap out of one side. A {@link BlockMapleTap} can only be hung on that
 * side, and the log turns back into a plain maple log once tapped often enough.
 */
public class BlockSappyMapleLog extends Block {

    public static final PropertyDirection SAP_SIDE = PropertyDirection.create("sap_side", EnumFacing.Plane.HORIZONTAL);

    public BlockSappyMapleLog() {
        super(Material.WOOD);
        setRegistryName("sappy_maple_log");
        setTranslationKey("sappy_maple_log");
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
        setHardness(2.0f);
        setResistance(10.0f);
        setLightOpacity(255);
        setCreativeTab(BYGTab.tab);
        setDefaultState(blockState.getBaseState().withProperty(SAP_SIDE, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SAP_SIDE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SAP_SIDE, EnumFacing.byHorizontalIndex(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SAP_SIDE).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(SAP_SIDE, placer.getHorizontalFacing().getOpposite());
    }

    // The sap is only a property of the placed log, so it breaks down into a plain maple log.
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.maple_log);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    // It always stands upright, so it can't be a vanilla log, but other mods should still treat it as one
    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        BlockBygLeaves.checkCanopyAfterLogBreak(world, pos, this);
    }
}

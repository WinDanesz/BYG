package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

public class BlockDirectionalLogBase extends Block {
    private static final PropertyDirection FACING = BlockDirectional.FACING;
    private final boolean silkHarvest;
    private final boolean supportsWideCanopy;

    public BlockDirectionalLogBase(String name, int harvestLevel, boolean silkHarvest) {
        this(name, harvestLevel, silkHarvest, false);
    }

    public BlockDirectionalLogBase(String name, int harvestLevel, boolean silkHarvest, boolean supportsWideCanopy) {
        super(Material.WOOD);
        this.silkHarvest = silkHarvest;
        this.supportsWideCanopy = supportsWideCanopy;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(255);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        facing = facing == EnumFacing.WEST || facing == EnumFacing.EAST ? EnumFacing.UP : (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? EnumFacing.EAST : EnumFacing.SOUTH);
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.silkHarvest;
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (this.supportsWideCanopy) {
            BlockBygLeaves.checkCanopyAfterLogBreak(world, pos, this);
        }
    }
}


package windanesz.byg.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

public class BlockDirectionalLogBase extends BlockLog {
    private final boolean silkHarvest;
    private final boolean supportsWideCanopy;

    public BlockDirectionalLogBase(String name, int harvestLevel, boolean silkHarvest) {
        this(name, harvestLevel, silkHarvest, false);
    }

    public BlockDirectionalLogBase(String name, int harvestLevel, boolean silkHarvest, boolean supportsWideCanopy) {
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    // The logs used to store a facing, which still has to read back as the same axis in existing worlds:
    // 0-1 (down, up) lay along x, 2-3 (north, south) stood upright, 4-5 (west, east) lay along z
    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
            case 1:
                return this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
            case 4:
            case 5:
                return this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
            case 6:
                return this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
            default:
                return this.getDefaultState();
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(LOG_AXIS)) {
            case X:
                return 1;
            case Z:
                return 5;
            case NONE:
                return 6;
            default:
                return 3;
        }
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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (this.supportsWideCanopy) {
            BlockBygLeaves.checkCanopyAfterLogBreak(world, pos, this);
        }
    }
}

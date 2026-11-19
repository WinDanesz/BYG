package windanesz.byg.blocks;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

public class BlockFenceGateBase extends BlockFenceGate {
    /**
     * Vanilla gate metadata, indexed by stored metadata. BYG gates used to be separate blocks named {@code <wood>_gate}
     * that stored their facing index (2 to 5) as metadata. Saves remap those ids onto these gates, so stored 2 to 5
     * must still read as a closed gate facing north, south, west and east; the vanilla values they displace swap in.
     */
    private static final int[] VANILLA_META = {4, 5, 2, 0, 1, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final int[] STORED_META = new int[16];

    static {
        for (int stored = 0; stored < 16; stored++) {
            STORED_META[VANILLA_META[stored]] = stored;
        }
    }

    public BlockFenceGateBase(String name, int harvestLevel, float hardness, float resistance) {
        super(BlockPlanks.EnumType.OAK);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(BYGTab.tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(VANILLA_META[meta & 15]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return STORED_META[super.getMetaFromState(state)];
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }
}

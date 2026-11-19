package windanesz.byg.blocks;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import java.util.Random;

public class BlockSonoranCactus extends BlockCactus {
    public BlockSonoranCactus(String name) {
        this.setHardness(0.4f);
        this.setSoundType(SoundType.CLOTH);
        this.setHarvestLevel("axe", 0);
        this.setTickRandomly(false);
        this.setCreativeTab(BYGTab.tab);
        this.setTranslationKey(name);
        this.setRegistryName(name);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos) {
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.attackEntityFrom(DamageSource.CACTUS, Config.getCactusDamage());
    }
}

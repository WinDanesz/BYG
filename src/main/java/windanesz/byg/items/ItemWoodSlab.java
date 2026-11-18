package windanesz.byg.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.blocks.BlockWoodSlabBase;

public class ItemWoodSlab extends ItemBlock {
    public ItemWoodSlab(BlockWoodSlabBase block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (tryMerge(world, pos, player, heldItem)) {
            return EnumActionResult.SUCCESS;
        }
        if (tryMerge(world, pos.offset(facing), player, heldItem)) {
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    private boolean tryMerge(World world, BlockPos pos, EntityPlayer player, ItemStack heldItem) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this.block || state.getValue(BlockWoodSlabBase.DOUBLE)) {
            return false;
        }
        if (!player.canPlayerEdit(pos, EnumFacing.UP, heldItem) || !world.checkNoEntityCollision(Block.FULL_BLOCK_AABB.offset(pos))) {
            return false;
        }

        IBlockState mergedState = state.withProperty(BlockWoodSlabBase.DOUBLE, true).withProperty(BlockWoodSlabBase.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        SoundType soundType = this.block.getSoundType(mergedState, world, pos, player);
        if (!world.isRemote) {
            world.setBlockState(pos, mergedState, 11);
        }
        world.playSound(player, pos, soundType.getPlaceSound(), net.minecraft.util.SoundCategory.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        if (!player.capabilities.isCreativeMode) {
            heldItem.shrink(1);
        }
        return true;
    }
}

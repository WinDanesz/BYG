package windanesz.byg.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

import java.util.function.Supplier;

public class ItemDoorBase extends Item {
    private final Supplier<Block> doorBlock;

    public ItemDoorBase(String registryName, Supplier<Block> doorBlock) {
        this.doorBlock = doorBlock;
        this.setTranslationKey(registryName);
        this.setRegistryName(registryName);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block door = this.doorBlock.get();
        if (door == null || facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!block.isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, stack) || !door.canPlaceBlockAt(world, pos)) {
            return EnumActionResult.FAIL;
        }

        EnumFacing playerFacing = EnumFacing.fromAngle(player.rotationYaw);
        int offsetX = playerFacing.getXOffset();
        int offsetZ = playerFacing.getZOffset();
        boolean hingeRight = offsetX < 0 && hitZ < 0.5F || offsetX > 0 && hitZ > 0.5F || offsetZ < 0 && hitX > 0.5F || offsetZ > 0 && hitX < 0.5F;
        ItemDoor.placeDoor(world, pos, playerFacing, door, hingeRight);
        IBlockState placed = world.getBlockState(pos);
        world.playSound(player, pos, door.getSoundType(placed, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, (door.getSoundType(placed, world, pos, player).getVolume() + 1.0F) / 2.0F, door.getSoundType(placed, world, pos, player).getPitch() * 0.8F);
        stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}

package windanesz.byg.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.client.BYGBiomeTab;
import windanesz.byg.command.BiomeTeleportation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creative-only testing aid: right-click to teleport to the nearest instance of the item's biome, sneak + right-click
 * to jump to a different area of it.
 */
public class ItemBiomeTeleporter extends Item {
    private static final int COOLDOWN_TICKS = 20;
    /** A new-area search starts from a random point this many blocks away, so it does not always find the same spot. */
    private static final int NEW_AREA_MIN_OFFSET = 1024;
    private static final int NEW_AREA_MAX_OFFSET = 4096;
    /** How far around the player (in cells) to look for the area being left, as they may be right at its edge. */
    private static final int CURRENT_AREA_CELLS = 4;
    /** Limits the flood fill of one area: 250,000 cells is about 2,000 x 2,000 blocks. */
    private static final int MAX_AREA_CELLS = 250000;

    private static final String TAG_BIOME = "biome";
    /** Visited areas as block x, z pairs, oldest first. */
    private static final String TAG_VISITED = "visited";

    public ItemBiomeTeleporter() {
        setRegistryName(BiomesYouGo.MODID, "biome_teleporter");
        setTranslationKey("biome_teleporter");
        setCreativeTab(BYGBiomeTab.tab);
        setMaxStackSize(1);
    }

    public static ItemStack createStack(Item item, ResourceLocation biomeId) {
        ItemStack stack = new ItemStack(item);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString(TAG_BIOME, biomeId.toString());
        return stack;
    }

    @Nullable
    public static ResourceLocation getBiomeId(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(TAG_BIOME, 8) ? new ResourceLocation(tag.getString(TAG_BIOME)) : null;
    }

    @Nullable
    public static Biome getBiome(ItemStack stack) {
        ResourceLocation id = getBiomeId(stack);
        return id == null ? null : Biome.REGISTRY.getObject(id);
    }

    /** Populated when the creative tab is opened, so biomes registered by any mod are included. */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) {
            return;
        }
        List<ResourceLocation> ids = new ArrayList<>(Biome.REGISTRY.getKeys());
        ids.sort(Comparator.comparing((ResourceLocation id) -> !BiomesYouGo.MODID.equals(id.getNamespace()))
                .thenComparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath));
        for (ResourceLocation id : ids) {
            items.add(createStack(this, id));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Biome biome = getBiome(stack);
        ResourceLocation id = getBiomeId(stack);
        ITextComponent name = biome != null ? new TextComponentString(biome.getBiomeName())
                : id != null ? new TextComponentString(id.toString())
                : new TextComponentTranslation("item.byg.biome_teleporter.no_biome");
        return new TextComponentTranslation("item.byg.biome_teleporter.named", name).getUnformattedText();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.biome_teleporter"));
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.biome_teleporter.sneak"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote || !(player instanceof EntityPlayerMP)) {
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        if (player.getCooldownTracker().hasCooldown(this)) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        if (!player.capabilities.isCreativeMode) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.creative_only"));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        Biome biome = getBiome(stack);
        if (biome == null) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.invalid_biome", String.valueOf(getBiomeId(stack))));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        player.getCooldownTracker().setCooldown(this, COOLDOWN_TICKS);
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        if (!player.isSneaking()) {
            boolean teleported = BiomeTeleportation.teleport(playerMP, biome, player);
            return new ActionResult<>(teleported ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
        }

        player.sendMessage(msg(TextFormatting.GRAY, "message.byg.biome_teleporter.searching_new", biome.getBiomeName()));
        BlockPos target = findNewArea(world, biome, player.getPosition(), stack);
        if (target == null) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.not_found_new",
                    biome.getBiomeName(), BiomeTeleportation.MAX_SEARCH_BLOCKS));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        BiomeTeleportation.teleportTo(playerMP, biome, target, player);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private static ITextComponent msg(TextFormatting color, String key, Object... args) {
        return BiomeTeleportation.msg(color, key, args);
    }

    /**
     * Looks for an area of the biome other than the one the player is in and the ones this stack remembers, starting
     * from a random nearby point, and updates the stack's memory. If there is none, the memory is cleared so that the
     * next attempt can go back to those areas.
     */
    @Nullable
    private static BlockPos findNewArea(World world, Biome wanted, BlockPos origin, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        int[] visited = tag.getIntArray(TAG_VISITED);
        VisitedBiomeAreas.CellTest isWanted = wantedCellTest(world, wanted);

        Set<Long> skipped = new HashSet<>();
        for (int i = 0; i + 1 < visited.length; i += 2) {
            VisitedBiomeAreas.fillArea(visited[i] >> 2, visited[i + 1] >> 2, isWanted, skipped, MAX_AREA_CELLS);
        }
        // The area being left also counts as visited, in case the player got here on foot or with a plain right-click.
        int originCellX = origin.getX() >> 2;
        int originCellZ = origin.getZ() >> 2;
        for (int x = originCellX - CURRENT_AREA_CELLS; x <= originCellX + CURRENT_AREA_CELLS; x++) {
            for (int z = originCellZ - CURRENT_AREA_CELLS; z <= originCellZ + CURRENT_AREA_CELLS; z++) {
                if (!skipped.contains(VisitedBiomeAreas.pack(x, z)) && isWanted.matches(x, z)) {
                    visited = VisitedBiomeAreas.remember(visited, x * 4 + 2, z * 4 + 2);
                    VisitedBiomeAreas.fillArea(x, z, isWanted, skipped, MAX_AREA_CELLS);
                }
            }
        }

        double angle = world.rand.nextDouble() * Math.PI * 2;
        int offset = NEW_AREA_MIN_OFFSET + world.rand.nextInt(NEW_AREA_MAX_OFFSET - NEW_AREA_MIN_OFFSET + 1);
        BlockPos searchFrom = origin.add(Math.cos(angle) * offset, 0, Math.sin(angle) * offset);

        BlockPos target = BiomeTeleportation.findBiome(world, wanted, searchFrom,
                (cellX, cellZ) -> !skipped.contains(VisitedBiomeAreas.pack(cellX, cellZ)));
        tag.setIntArray(TAG_VISITED, target == null ? new int[0]
                : VisitedBiomeAreas.remember(visited, target.getX(), target.getZ()));
        return target;
    }

    /** Tests cells against the wanted biome, keeping the sampled tiles as a flood fill keeps asking about the same ones. */
    private static VisitedBiomeAreas.CellTest wantedCellTest(World world, Biome wanted) {
        final int size = BiomeTeleportation.TILE_CELLS;
        Map<Long, Biome[]> tiles = new HashMap<>();
        return (cellX, cellZ) -> {
            int tileX = Math.floorDiv(cellX, size);
            int tileZ = Math.floorDiv(cellZ, size);
            Biome[] tile = tiles.computeIfAbsent(VisitedBiomeAreas.pack(tileX, tileZ), key -> world.getBiomeProvider()
                    .getBiomesForGeneration(null, tileX * size, tileZ * size, size, size));
            return tile[Math.floorMod(cellX, size) + Math.floorMod(cellZ, size) * size] == wanted;
        };
    }
}

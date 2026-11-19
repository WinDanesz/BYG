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
import java.util.function.BooleanSupplier;

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
        boolean started;
        if (player.isSneaking()) {
            BlockPos origin = player.getPosition();
            double angle = world.rand.nextDouble() * Math.PI * 2;
            int offset = NEW_AREA_MIN_OFFSET + world.rand.nextInt(NEW_AREA_MAX_OFFSET - NEW_AREA_MIN_OFFSET + 1);
            BlockPos searchFrom = origin.add(Math.cos(angle) * offset, 0, Math.sin(angle) * offset);
            started = BiomeTeleportation.start(playerMP, biome, player, searchFrom, new NewArea(world, biome, origin, stack));
            if (started) {
                player.sendMessage(msg(TextFormatting.GRAY, "message.byg.biome_teleporter.searching_new", biome.getBiomeName()));
            }
        } else {
            started = BiomeTeleportation.teleport(playerMP, biome, player);
        }
        return new ActionResult<>(started ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
    }

    private static ITextComponent msg(TextFormatting color, String key, Object... args) {
        return BiomeTeleportation.msg(color, key, args);
    }

    /**
     * Limits a search to areas of the biome other than the one the player is in and the ones the stack remembers,
     * and updates the stack's memory when it ends. If there is no such area, the memory is cleared so that the next
     * attempt can go back to those areas.
     */
    private static final class NewArea implements BiomeTeleportation.Rules {
        private final Biome wanted;
        private final ItemStack stack;
        private final VisitedBiomeAreas.CellTest isWanted;
        private final Set<Long> skipped = new HashSet<>();
        /** Cells to fill areas from, as x, z pairs: the remembered ones first, then those around the player. */
        private final int[] seeds;
        private final int remembered;
        private int[] visited;
        private int nextSeed;
        @Nullable
        private VisitedBiomeAreas.Fill fill;

        NewArea(World world, Biome wanted, BlockPos origin, ItemStack stack) {
            this.wanted = wanted;
            this.stack = stack;
            isWanted = wantedCellTest(world, wanted);
            visited = stack.getTagCompound().getIntArray(TAG_VISITED);
            remembered = visited.length / 2;
            int side = CURRENT_AREA_CELLS * 2 + 1;
            seeds = new int[(remembered + side * side) * 2];
            for (int i = 0; i < remembered * 2; i++) {
                seeds[i] = visited[i] >> 2;
            }
            for (int i = 0; i < side * side; i++) {
                seeds[(remembered + i) * 2] = (origin.getX() >> 2) - CURRENT_AREA_CELLS + i / side;
                seeds[(remembered + i) * 2 + 1] = (origin.getZ() >> 2) - CURRENT_AREA_CELLS + i % side;
            }
        }

        @Override
        public boolean prepare(BooleanSupplier outOfTime) {
            while (fill != null || nextSeed < seeds.length / 2) {
                if (fill == null) {
                    int x = seeds[nextSeed * 2];
                    int z = seeds[nextSeed * 2 + 1];
                    // The area being left also counts as visited, in case the player got here on foot or with a plain
                    // right-click.
                    if (nextSeed >= remembered && !skipped.contains(VisitedBiomeAreas.pack(x, z)) && isWanted.matches(x, z)) {
                        visited = VisitedBiomeAreas.remember(visited, x * 4 + 2, z * 4 + 2);
                    }
                    fill = new VisitedBiomeAreas.Fill(x, z, isWanted, skipped, MAX_AREA_CELLS);
                    nextSeed++;
                }
                if (!fill.run(outOfTime)) {
                    return false;
                }
                fill = null;
            }
            return true;
        }

        @Override
        public boolean accepts(int cellX, int cellZ) {
            return !skipped.contains(VisitedBiomeAreas.pack(cellX, cellZ));
        }

        @Override
        public void finished(EntityPlayerMP player, @Nullable BlockPos target) {
            stack.getTagCompound().setIntArray(TAG_VISITED, target == null ? new int[0]
                    : VisitedBiomeAreas.remember(visited, target.getX(), target.getZ()));
            if (target == null) {
                player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.not_found_new",
                        wanted.getBiomeName(), BiomeTeleportation.MAX_SEARCH_BLOCKS));
            }
        }
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

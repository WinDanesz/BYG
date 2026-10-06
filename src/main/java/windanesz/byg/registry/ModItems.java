package windanesz.byg.registry;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.blocks.BlockAlgae;
import windanesz.byg.blocks.BlockTinyLilypad;
import windanesz.byg.client.BYGTab;
import windanesz.byg.items.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ObjectHolder(BiomesYouGo.MODID)
@Mod.EventBusSubscriber
public final class ModItems {
    private static final Set<Block> AXE_EFFECTIVE_BLOCKS = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    public static final Item aspen_door = placeholder();
    public static final Item baobab_door = placeholder();
    public static final Item baobab_fruit = placeholder();
    public static final Item baobab_powder = placeholder();
    public static final Item berry_juice = placeholder();
    public static final Item maple_pancakes = placeholder();
    public static final Item maple_sap = placeholder();
    public static final Item maple_syrup = placeholder();
    public static final Item biome_teleporter = placeholder();
    public static final Item blue_enchanted_door = placeholder();
    public static final Item blueberry = placeholder();
    public static final Item blueberry_pie = placeholder();
    public static final Item blue_glowshroom = placeholder();
    public static final Item byg_logo = placeholder();
    public static final Item carrot_soup = placeholder();
    public static final Item cattail = placeholder();
    public static final Item cattail_rhizome = placeholder();
    public static final Item cherry_door = placeholder();
    public static final Item cika_door = placeholder();
    public static final Item cooked_carrot = placeholder();
    public static final Item cooked_cattail_rhizome = placeholder();
    public static final Item cooked_pufferfish = placeholder();
    public static final Item cooked_pumpkin_seeds = placeholder();
    public static final Item cooked_spider_eye = placeholder();
    public static final Item cooked_tropical_fish = placeholder();
    public static final Item cypress_door = placeholder();
    public static final Item ebony_door = placeholder();
    public static final Item enchanted_stick = placeholder();
    public static final Item eucalyptus_door = placeholder();
    public static final Item fir_door = placeholder();
    public static final Item frozen_oak_door = placeholder();
    public static final Item glowcane_dust_blue = placeholder();
    public static final Item glowcane_dust_pink = placeholder();
    public static final Item glowcane_dust_purple = placeholder();
    public static final Item glowcane_dust_red = placeholder();
    public static final Item glowcane_stalk_blue = placeholder();
    public static final Item glowcane_stalk_pink = placeholder();
    public static final Item glowcane_stalk_purple = placeholder();
    public static final Item glowcane_stalk_red = placeholder();
    public static final Item glowshroom_soup_blue = placeholder();
    public static final Item glowshroom_soup_purple = placeholder();
    public static final Item golden_beetroot = placeholder();
    public static final Item great_oak_door = placeholder();
    public static final Item green_enchanted_door = placeholder();
    public static final Item green_glowshroom_stew = placeholder();
    public static final Item green_apple = placeholder();
    public static final Item green_apple_pie = placeholder();
    public static final Item green_glowshroom = placeholder();
    public static final Item hawthorn_door = placeholder();
    public static final Item hawthorn_berries = placeholder();
    public static final Item holly_door = placeholder();
    public static final Item holly_berries = placeholder();
    public static final Item ironwood_door = placeholder();
    public static final Item jacaranda_door = placeholder();
    public static final Item kasai_chain_plating = placeholder();
    public static final Item kasai_ingot = placeholder();
    public static final Item kasai_pickaxe = placeholder();
    public static final Item kasai_shovel = placeholder();
    public static final Item kasai_sword = placeholder();
    public static final Item latharium_axe = placeholder();
    public static final Item latharium_armour_helmet = placeholder();
    public static final Item latharium_armour_body = placeholder();
    public static final Item latharium_armour_legs = placeholder();
    public static final Item latharium_armour_boots = placeholder();
    public static final Item latharium_battleaxe = placeholder();
    public static final Item latharium_gem = placeholder();
    public static final Item latharium_hoe = placeholder();
    public static final Item latharium_pickaxe = placeholder();
    public static final Item latharium_shovel = placeholder();
    public static final Item latharium_sword = placeholder();
    public static final Item light_blue_crystals = placeholder();
    public static final Item mahogany_door = placeholder();
    public static final Item mangrove_door = placeholder();
    public static final Item maple_door = placeholder();
    public static final Item mud_balls = placeholder();
    public static final Item palm_door = placeholder();
    public static final Item pendorite_axe = placeholder();
    public static final Item pendorite_battleaxe = placeholder();
    public static final Item pendorite_gem = placeholder();
    public static final Item pendorite_hoe = placeholder();
    public static final Item pendorite_pickaxe = placeholder();
    public static final Item pendorite_shovel = placeholder();
    public static final Item pendorite_sword = placeholder();
    public static final Item pine_door = placeholder();
    public static final Item pumpkin_bread = placeholder();
    public static final Item pumpkin_mash = placeholder();
    public static final Item purple_crystals = placeholder();
    public static final Item purple_glowshroom = placeholder();
    public static final Item red_crystals = placeholder();
    public static final Item reeds = placeholder();
    public static final Item rowan_door = placeholder();
    public static final Item rowan_berries = placeholder();
    public static final Item rudo_beans = placeholder();
    public static final Item rudo_beans_roasted = placeholder();
    public static final Item salal_berry = placeholder();
    public static final Item silver_apple = placeholder();
    public static final Item skyris_door = placeholder();
    public static final Item spider_eye_soup = placeholder();
    public static final Item stone_stick = placeholder();
    public static final Item strawberry = placeholder();
    public static final Item strawberry_pie = placeholder();
    public static final Item tamrelite_axe = placeholder();
    public static final Item tamrelite_battleaxe = placeholder();
    public static final Item tamrelite_gem = placeholder();
    public static final Item tamrelite_hoe = placeholder();
    public static final Item tamrelite_pickaxe = placeholder();
    public static final Item tamrelite_shovel = placeholder();
    public static final Item tamrelite_sword = placeholder();
    public static final Item tropical_fish_soup = placeholder();
    public static final Item white_crystals = placeholder();
    public static final Item willow_door = placeholder();
    public static final Item witch_hazel_door = placeholder();
    public static final Item wooden_mortar = placeholder();
    public static final Item worm = placeholder();
    public static final Item zelkova_door = placeholder();

    private ModItems() {
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder() {
        return null;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registerItem(registry, createDoorItem("aspen_door", () -> ModBlocks.aspen_door_bottom));
        if (Config.isBaobabContentEnabled()) {
            registerItem(registry, createDoorItem("baobab_door", () -> ModBlocks.baobab_door_bottom));
            registerItem(registry, new ItemBaobabfruit());
            registerItem(registry, createBaobabPowderItem());
        }
        registerItem(registry, new ItemBerryJuice());
        registerItem(registry, createBasicItem("maple_sap", 16, BYGTab.tab));
        registerItem(registry, new windanesz.byg.items.ItemMapleSyrup());
        registerItem(registry, createFoodItem("maple_pancakes", 8, 0.8f, false, 16, false));
        if (Config.isEnchantedTreeContentEnabled()) {
            registerItem(registry, createDoorItem("blue_enchanted_door", () -> ModBlocks.blue_enchanted_door_bottom));
        }
        registerItem(registry, new ItemBlueberry());
        registerItem(registry, createFoodItem("blueberry_pie", 8, 0.3f, false, 16, false));
        registerItem(registry, createGlowshroomItem("blue_glowshroom", () -> ModBlocks.small_blue_glowshroom));
        registerItem(registry, createBasicItem("byg_logo", 1, null));
        registerItem(registry, createSoupItem("carrot_soup", 9));
        registerItem(registry, createGroundPlantItem("cattail", () -> windanesz.byg.registry.ModBlocks.cattails));
        registerItem(registry, createBasicItem("cattail_rhizome"));
        registerItem(registry, createDoorItem("cherry_door", () -> ModBlocks.cherry_door_bottom));
        if (Config.isCikaContentEnabled()) {
            registerItem(registry, createDoorItem("cika_door", () -> ModBlocks.cika_door_bottom));
        }
        registerItem(registry, createFoodItem("cooked_carrot", 4, 0.3f, false, 64, false));
        registerItem(registry, createFoodItem("cooked_cattail_rhizome", 4, 0.1f, false, 64, false));
        registerItem(registry, createFoodItem("cooked_pufferfish", 6, 0.3f, false, 64, false));
        registerItem(registry, createFoodItem("cooked_pumpkin_seeds", 3, 0.3f, false, 64, false));
        registerItem(registry, createFoodItem("cooked_spider_eye", 5, 0.3f, true, 64, false));
        registerItem(registry, createFoodItem("cooked_tropical_fish", 5, 0.3f, false, 64, false));
        if (Config.isCypressContentEnabled()) {
            registerItem(registry, createDoorItem("cypress_door", () -> ModBlocks.cypress_door_bottom));
        }
        if (Config.isEbonyContentEnabled()) {
            registerItem(registry, createDoorItem("ebony_door", () -> ModBlocks.ebony_door_bottom));
        }
        registerItem(registry, createDoorItem("eucalyptus_door", () -> ModBlocks.eucalyptus_door_bottom));
        registerItem(registry, createDoorItem("fir_door", () -> ModBlocks.fir_door_bottom));
        registerItem(registry, createDoorItem("frozen_oak_door", () -> ModBlocks.frozen_oak_door_bottom));
        registerItem(registry, createBasicItem("glowcane_dust_blue"));
        registerItem(registry, createBasicItem("glowcane_dust_pink"));
        registerItem(registry, createBasicItem("glowcane_dust_purple"));
        registerItem(registry, createBasicItem("glowcane_dust_red"));
        registerItem(registry, createGlowcaneStalkItem("glowcane_stalk_blue", () -> ModBlocks.glowcane_blue));
        registerItem(registry, createGlowcaneStalkItem("glowcane_stalk_pink", () -> ModBlocks.glowcane_pink));
        registerItem(registry, createGlowcaneStalkItem("glowcane_stalk_purple", () -> ModBlocks.glowcane_purple));
        registerItem(registry, createGlowcaneStalkItem("glowcane_stalk_red", () -> ModBlocks.glowcane_red));
        registerItem(registry, createSoupItem("glowshroom_soup_blue", 8));
        registerItem(registry, createSoupItem("glowshroom_soup_purple", 8));
        registerItem(registry, createGoldenBeetrootItem());
        if (Config.isGreatOakContentEnabled()) {
            registerItem(registry, createDoorItem("great_oak_door", () -> ModBlocks.great_oak_door_bottom));
        }
        if (Config.isEnchantedTreeContentEnabled()) {
            registerItem(registry, createDoorItem("green_enchanted_door", () -> ModBlocks.green_enchanted_door_bottom));
        }
        registerItem(registry, createSoupItem("green_glowshroom_stew", 8));
        if (Config.isSkyrisContentEnabled()) {
            registerItem(registry, createFoodItem("green_apple", 6, 0.3f, false, 64, true));
            registerItem(registry, createFoodItem("green_apple_pie", 8, 0.3f, false, 16, false));
        }
        registerItem(registry, createGlowshroomItem("green_glowshroom", () -> ModBlocks.small_green_glowshroom));
        registerItem(registry, createDoorItem("hawthorn_door", () -> ModBlocks.hawthorn_door_bottom));
        registerItem(registry, createFoodItem("hawthorn_berries", 2, 0.3f, false, 64, false));
        registerItem(registry, createDoorItem("holly_door", () -> ModBlocks.holly_door_bottom));
        registerItem(registry, createFoodItem("holly_berries", 2, 0.3f, false, 64, true));
        registerItem(registry, createDoorItem("ironwood_door", () -> ModBlocks.ironwood_door_bottom));
        registerItem(registry, createDoorItem("jacaranda_door", () -> ModBlocks.jacaranda_door_bottom));
        if (Config.isOreContentEnabled("kasai")) {
            registerItem(registry, createBasicItem("kasai_chain_plating"));
            registerItem(registry, createBasicItem("kasai_ingot"));
        }
        if (Config.isOreContentEnabled("latharium")) {
            registerItem(registry, createBasicItem("enchanted_stick"));
            registerItem(registry, createBasicItem("latharium_gem"));
        }
        registerItem(registry, createBasicItem("light_blue_crystals"));
        registerItem(registry, createDoorItem("mahogany_door", () -> ModBlocks.mahogany_door_bottom));
        if (Config.isMangroveContentEnabled()) {
            registerItem(registry, createDoorItem("mangrove_door", () -> ModBlocks.mangrove_door_bottom));
        }
        registerItem(registry, createDoorItem("maple_door", () -> ModBlocks.maple_door_bottom));
        registerItem(registry, new ItemMudballs());
        if (Config.isPalmContentEnabled()) {
            registerItem(registry, createDoorItem("palm_door", () -> ModBlocks.palm_door_bottom));
        }
        if (Config.isOreContentEnabled("pendorite")) {
            registerItem(registry, createBasicItem("pendorite_gem"));
        }
        registerItem(registry, createDoorItem("pine_door", () -> ModBlocks.pine_door_bottom));
        registerItem(registry, createFoodItem("pumpkin_bread", 7, 0.3f, false, 64, false));
        registerItem(registry, createSoupItem("pumpkin_mash", 3));
        registerItem(registry, createBasicItem("purple_crystals"));
        registerItem(registry, createGlowshroomItem("purple_glowshroom", () -> ModBlocks.small_purple_glowshroom));
        registerItem(registry, createBasicItem("red_crystals"));
        registerItem(registry, createGroundPlantItem("reeds", () -> windanesz.byg.registry.ModBlocks.reed));
        registerItem(registry, createDoorItem("rowan_door", () -> ModBlocks.rowan_door_bottom));
        registerItem(registry, createFoodItem("rowan_berries", 2, 0.3f, false, 64, false));
        registerItem(registry, createSeedFoodItem("rudo_beans", 1, 0.3f, () -> windanesz.byg.registry.ModBlocks.rudo_stalk));
        registerItem(registry, createFoodItem("rudo_beans_roasted", 3, 0.3f, false, 64, false));
        registerItem(registry, createFoodItem("salal_berry", 2, 0.3f, false, 64, false));
        if (Config.isSkyrisContentEnabled()) {
            registerItem(registry, createFoodItem("silver_apple", 6, 0.3f, false, 64, true));
            registerItem(registry, createDoorItem("skyris_door", () -> ModBlocks.skyris_door_bottom));
        }
        registerItem(registry, createSoupItem("spider_eye_soup", 10));
        if (Config.isOreContentEnabled("kasai") || Config.isOreContentEnabled("pendorite")) {
            registerItem(registry, createBasicItem("stone_stick"));
        }
        registerItem(registry, createSeedFoodItem("strawberry", 2, 0.3f, () -> windanesz.byg.registry.ModBlocks.strawberry_bush));
        registerItem(registry, createFoodItem("strawberry_pie", 8, 0.3f, false, 16, false));
        if (Config.isOreContentEnabled("tamrelite")) {
            registerItem(registry, createBasicItem("tamrelite_gem"));
        }
        registerItem(registry, createSoupItem("tropical_fish_soup", 10));
        registerItem(registry, createBasicItem("white_crystals"));
        registerItem(registry, createDoorItem("willow_door", () -> ModBlocks.willow_door_bottom));
        registerItem(registry, createDoorItem("witch_hazel_door", () -> ModBlocks.witch_hazel_door));
        registerItem(registry, createWoodenMortarItem());
        registerItem(registry, createWormItem());
        registerItem(registry, createDoorItem("zelkova_door", () -> ModBlocks.zelkova_door_bottom));

        if (Config.areBiomeTeleporterItemsEnabled()) {
            registerItem(registry, new ItemBiomeTeleporter());
        }

        registerKasaiEquipment(registry);
        registerLathariumEquipment(registry);
        registerPendoriteEquipment(registry);
        registerTamreliteEquipment(registry);

        for (Block block : Block.REGISTRY) {
            if (block == null || block.getRegistryName() == null || !BiomesYouGo.MODID.equals(block.getRegistryName().getNamespace())
                    || block instanceof BlockFluidClassic || block.getCreativeTab() == null || registry.containsKey(block.getRegistryName())) {
                continue;
            }
            if (block instanceof windanesz.byg.blocks.BlockWoodSlabBase) {
                registerItem(registry, new ItemWoodSlab((windanesz.byg.blocks.BlockWoodSlabBase) block).setRegistryName(block.getRegistryName()));
                continue;
            }
            if (block instanceof BlockAlgae || block instanceof BlockTinyLilypad) {
                registerItem(registry, new ItemAlgae(block).setRegistryName(block.getRegistryName()));
                continue;
            }
            registerItem(registry, new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    private static Item createBasicItem(String registryName) {
        return createBasicItem(registryName, 64, BYGTab.tab);
    }

    private static Item createBasicItem(String registryName, int maxStackSize, @Nullable CreativeTabs creativeTab) {
        Item item = new Item();
        configureItem(item, registryName, creativeTab);
        item.setMaxStackSize(maxStackSize);
        return item;
    }

    private static Item createBaobabPowderItem() {
        Item item = new Item() {
            @Override
            public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
                tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.baobab_powder"));
            }
        };
        configureItem(item, "baobab_powder", BYGTab.tab);
        return item;
    }

    private static Item createWoodenMortarItem() {
        Item item = new Item() {
            @Override
            public int getMaxItemUseDuration(ItemStack stack) {
                return 100;
            }
        };
        configureItem(item, "wooden_mortar", BYGTab.tab);
        item.setMaxStackSize(1);
        item.setContainerItem(item);
        return item;
    }

    private static Item createWormItem() {
        Item item = new Item() {
            @Override
            public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
                int lureLevel = Config.getWormLureLevel();
                String tip = I18n.format("tooltip.byg.worm");
                if (lureLevel > 0) tip += " (Lure " + toRoman(lureLevel) + ")";
                tooltip.add(TextFormatting.GRAY + tip);
            }
        };
        configureItem(item, "worm", BYGTab.tab);
        return item;
    }

    private static String toRoman(int n) {
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds  = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens      = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones      = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        if (n <= 0 || n >= 4000) return String.valueOf(n);
        return thousands[n / 1000] + hundreds[(n % 1000) / 100] + tens[(n % 100) / 10] + ones[n % 10];
    }

    private static Item createFoodItem(String registryName, int hunger, float saturation, boolean wolfFood, int maxStackSize, boolean alwaysEdible) {
        Config.FoodValues foodValues = Config.getFoodValues(registryName, hunger, saturation);
        ItemFood item = new ItemFood(foodValues.foodLevel, foodValues.saturation, wolfFood);
        if (alwaysEdible) {
            item.setAlwaysEdible();
        }
        configureItem(item, registryName, BYGTab.tab);
        item.setMaxStackSize(maxStackSize);
        return item;
    }

    private static Item createGoldenBeetrootItem() {
        Config.FoodValues foodValues = Config.getFoodValues("golden_beetroot", 6, 0.3f);
        ItemFood item = new ItemFood(foodValues.foodLevel, foodValues.saturation, false) {
            @Override
            public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
                int duration = Config.getGoldenBeetrootRegenDuration();
                if (duration > 0) {
                    tooltip.add(TextFormatting.GRAY + net.minecraft.client.resources.I18n.format("tooltip.byg.golden_beetroot.player",
                            net.minecraft.util.StringUtils.ticksToElapsedTime(duration)));
                }
                tooltip.add(TextFormatting.GRAY + net.minecraft.client.resources.I18n.format("tooltip.byg.golden_beetroot.pet"));
            }

            @Override
            protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
                super.onFoodEaten(stack, world, player);
                int duration = Config.getGoldenBeetrootRegenDuration();
                if (!world.isRemote && duration > 0) {
                    player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.init.MobEffects.REGENERATION, duration, 0));
                }
            }
        };
        item.setAlwaysEdible();
        configureItem(item, "golden_beetroot", BYGTab.tab);
        item.setMaxStackSize(64);
        return item;
    }

    private static Item createSoupItem(String registryName, int hunger) {
        Config.FoodValues foodValues = Config.getFoodValues(registryName, hunger, 0.6f);
        ItemFood item = new ItemFood(foodValues.foodLevel, foodValues.saturation, false) {
            @Override
            public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
                super.onItemUseFinish(stack, world, entityLiving);
                return new ItemStack(Items.BOWL);
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        item.setMaxStackSize(1);
        return item;
    }

    private static Item createSeedFoodItem(String registryName, int hunger, float saturation, Supplier<Block> cropSupplier) {
        Config.FoodValues foodValues = Config.getFoodValues(registryName, hunger, saturation);
        ItemSeedFood item = new ItemSeedFood(foodValues.foodLevel, foodValues.saturation, cropSupplier.get(), Blocks.FARMLAND);
        configureItem(item, registryName, BYGTab.tab);
        item.setMaxStackSize(64);
        return item;
    }

    private static Item createDoorItem(String registryName, Supplier<Block> doorBlock) {
        return new ItemDoorBase(registryName, doorBlock);
    }

    private static Item createGlowcaneStalkItem(String registryName, Supplier<Block> placedBlock) {
        return new ItemGlowcaneStalkBase(registryName, placedBlock);
    }

    private static Item createGlowshroomItem(String registryName, Supplier<Block> placedBlock) {
        Item item = new Item() {
            @Override
            public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
                ItemStack stack = player.getHeldItem(hand);
                BlockPos placePos = pos.offset(side);
                Block block = placedBlock.get();
                if (side != EnumFacing.UP || !world.isAirBlock(placePos)
                        || !player.canPlayerEdit(placePos, side, stack)
                        || !world.mayPlace(block, placePos, false, side, player)
                        || !block.canPlaceBlockAt(world, placePos)) {
                    return EnumActionResult.FAIL;
                }
                if (!world.isRemote) {
                    if (!world.setBlockState(placePos, block.getDefaultState(), 3)) {
                        return EnumActionResult.FAIL;
                    }
                    world.playSound(null, placePos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.slime.place")), SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        item.setMaxStackSize(64);
        return item;
    }

    private static Item createGroundPlantItem(String registryName, Supplier<Block> placedBlock) {
        Item item = new Item() {
            @Override
            public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
                Block groundBlock = world.getBlockState(pos).getBlock();
                if (groundBlock != Blocks.GRASS && groundBlock != windanesz.byg.registry.ModBlocks.mud_block) {
                    return EnumActionResult.PASS;
                }
                ItemStack stack = player.getHeldItem(hand);
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.grass.place")), SoundCategory.NEUTRAL, 1.0f, 1.0f);
                if (player instanceof EntityPlayer) {
                    player.inventory.clearMatchingItems(stack.getItem(), -1, 1, null);
                }
                world.setBlockState(pos.up(), placedBlock.get().getDefaultState(), 3);
                return EnumActionResult.PASS;
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        item.setMaxStackSize(64);
        return item;
    }

    private static Item createPickaxeItem(String registryName, String materialName, int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability) {
        ItemPickaxe item = new ItemPickaxe(EnumHelper.addToolMaterial(materialName, harvestLevel, maxUses, efficiency, attackDamage, enchantability)) {
            @Override
            public Set<String> getToolClasses(ItemStack stack) {
                return Collections.singleton("pickaxe");
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        return item;
    }

    private static Item createShovelItem(String registryName, String materialName, int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability) {
        ItemSpade item = new ItemSpade(EnumHelper.addToolMaterial(materialName, harvestLevel, maxUses, efficiency, attackDamage, enchantability)) {
            @Override
            public Set<String> getToolClasses(ItemStack stack) {
                return Collections.singleton("spade");
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        return item;
    }

    private static Item createSwordItem(String registryName, String materialName, int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, @Nullable Consumer<EntityLivingBase> hitEffect) {
        ItemSword item = new ItemSword(EnumHelper.addToolMaterial(materialName, harvestLevel, maxUses, efficiency, attackDamage, enchantability)) {
            @Override
            public Set<String> getToolClasses(ItemStack stack) {
                return Collections.singleton("sword");
            }

            @Override
            public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
                super.hitEntity(stack, target, attacker);
                if (hitEffect != null) {
                    hitEffect.accept(target);
                }
                return true;
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        return item;
    }

    private static Item createHoeItem(String registryName, String materialName, int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability) {
        ItemHoe item = new ItemHoe(EnumHelper.addToolMaterial(materialName, harvestLevel, maxUses, efficiency, attackDamage, enchantability)) {
            @Override
            public Set<String> getToolClasses(ItemStack stack) {
                return Collections.singleton("hoe");
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        return item;
    }

    private static Item createAxeLikeItem(String registryName, String materialName, int harvestLevel, int maxUses, float efficiency, float attackDamageIn, int enchantability, float attackDamage, float attackSpeed, @Nullable Consumer<EntityLivingBase> hitEffect) {
        ItemTool item = new ItemTool(Objects.requireNonNull(EnumHelper.addToolMaterial(materialName, harvestLevel, maxUses, efficiency, attackDamageIn, enchantability)), AXE_EFFECTIVE_BLOCKS) {

            @Override
            public float getDestroySpeed(ItemStack stack, IBlockState state) {
                Material material = state.getMaterial();
                return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
            }

            @Override
            public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
                super.hitEntity(stack, target, attacker);
                if (hitEffect != null) {
                    hitEffect.accept(target);
                }
                return true;
            }
        };
        configureItem(item, registryName, BYGTab.tab);
        return item;
    }

    private static void configureItem(Item item, String registryName, @Nullable CreativeTabs creativeTab) {
        item.setTranslationKey(registryName);
        item.setRegistryName(registryName);
        item.setCreativeTab(creativeTab);
    }

    private static void registerItem(IForgeRegistry<Item> registry, Item item) {
        if (item.getRegistryName() != null && !Config.isContentRegistered(item.getRegistryName().getPath())) {
            return;
        }
        registry.register(item);
    }

    private static void registerArmorSet(IForgeRegistry<Item> registry, ItemArmor.ArmorMaterial material, String baseName) {
        registerArmorSet(registry, material, baseName, null);
    }

    private static void registerArmorSet(IForgeRegistry<Item> registry, ItemArmor.ArmorMaterial material, String baseName,
                                         @Nullable Supplier<String> tooltipLine) {
        registerItem(registry, createArmorPiece(material, EntityEquipmentSlot.HEAD, baseName + "helmet", tooltipLine));
        registerItem(registry, createArmorPiece(material, EntityEquipmentSlot.CHEST, baseName + "body", tooltipLine));
        registerItem(registry, createArmorPiece(material, EntityEquipmentSlot.LEGS, baseName + "legs", tooltipLine));
        registerItem(registry, createArmorPiece(material, EntityEquipmentSlot.FEET, baseName + "boots", tooltipLine));
    }

    private static ItemArmor.ArmorMaterial createArmorMaterial(String materialName, String texturePrefix, String setName,
                                                               int defaultDurabilityMultiplier, int defaultBootsProtection,
                                                               int defaultLeggingsProtection, int defaultChestplateProtection,
                                                               int defaultHelmetProtection, int defaultEnchantability,
                                                               float defaultToughness) {
        Config.ArmorMaterialStats stats = Config.getArmorMaterialStats(setName, defaultDurabilityMultiplier, defaultBootsProtection,
                defaultLeggingsProtection, defaultChestplateProtection, defaultHelmetProtection, defaultEnchantability, defaultToughness);
        return EnumHelper.addArmorMaterial(materialName, texturePrefix, stats.getDurabilityMultiplier(),
                stats.getDamageReductionAmounts(), stats.getEnchantability(), null, stats.getToughness());
    }

    private static void registerKasaiEquipment(IForgeRegistry<Item> registry) {
        if (!Config.isEquipmentSetEnabled("kasai")) {
            return;
        }
        registerItem(registry, createPickaxeItem("kasai_pickaxe", "KASAIPICKAXE", 6, 1800, 10.0f, 2.0f, 10));
        registerItem(registry, createShovelItem("kasai_shovel", "KASAISHOVEL", 6, 1800, 12.0f, 2.0f, 10));
        registerItem(registry, createSwordItem("kasai_sword", "KASAISWORD", 1, 1800, 5.0f, 3.0f, 10, ToolHitEffects::applyKasai));
        registerArmorSet(registry, createArmorMaterial("kasai_armour_", "byg:kasai_chainmail_", "kasai", 30, 4, 6, 6, 4, 11, 3.5f), "kasai_armour_",
                () -> I18n.format("tooltip.byg.kasai_armor", formatSeconds(Config.getKasaiFireWardDuration()),
                        formatSeconds(Config.getKasaiFireWardCooldown())));
    }

    private static String formatSeconds(int ticks) {
        int seconds = (ticks + 19) / 20;
        return seconds >= 60 ? String.format("%d:%02d", seconds / 60, seconds % 60) : seconds + "s";
    }

    private static void registerLathariumEquipment(IForgeRegistry<Item> registry) {
        if (!Config.isEquipmentSetEnabled("latharium")) {
            return;
        }
        registerItem(registry, createAxeLikeItem("latharium_axe", "LATHARIUMAXE", 2, 950, 9.0f, 4.0f, 8, 4.0f, -3.1f, null));
        registerItem(registry, createAxeLikeItem("latharium_battleaxe", "LATHARIUMBATTLEAXE", 2, 950, 9.0f, 5.0f, 8, 5.0f, -3.1f, ToolHitEffects::applyLatharium));
        registerItem(registry, createHoeItem("latharium_hoe", "LATHARIUMHOE", 1, 950, 4.0f, 5.0f, 2));
        registerItem(registry, createPickaxeItem("latharium_pickaxe", "LATHARIUMPICKAXE", 7, 950, 9.0f, 2.0f, 8));
        registerItem(registry, createShovelItem("latharium_shovel", "LATHARIUMSHOVEL", 2, 950, 9.0f, 2.0f, 8));
        registerItem(registry, createSwordItem("latharium_sword", "LATHARIUMSWORD", 1, 950, 4.0f, 3.0f, 8, null));
        registerArmorSet(registry, createArmorMaterial("latharium_armour_", "byg:latharium_", "latharium", 24, 3, 5, 5, 3, 16, 2.0f),
                "latharium_armour_", () -> I18n.format("tooltip.byg.latharium_armor"));
    }

    private static void registerPendoriteEquipment(IForgeRegistry<Item> registry) {
        if (!Config.isEquipmentSetEnabled("pendorite")) {
            return;
        }
        registerItem(registry, createAxeLikeItem("pendorite_axe", "PENDORITEAXE", 1, 1200, 12.0f, 6.0f, 15, 6.0f, -3.1f, null));
        registerItem(registry, createAxeLikeItem("pendorite_battleaxe", "PENDORITEBATTLEAXE", 1, 1200, 12.0f, 9.0f, 15, 9.0f, -3.1f, null));
        registerItem(registry, createHoeItem("pendorite_hoe", "PENDORITEHOE", 1, 1200, 4.0f, 5.0f, 2));
        registerItem(registry, createPickaxeItem("pendorite_pickaxe", "PENDORITEPICKAXE", 5, 1200, 12.0f, 3.0f, 15));
        registerItem(registry, createShovelItem("pendorite_shovel", "PENDORITESHOVEL", 3, 1200, 12.0f, 2.0f, 15));
        registerItem(registry, createSwordItem("pendorite_sword", "PENDORITESWORD", 1, 1200, 4.0f, 5.0f, 15, null));
        registerArmorSet(registry, createArmorMaterial("PENDORITEARMOUR", "byg:pandorite_", "pendorite", 25, 2, 2, 4, 2, 15, 1.0f), "pendorite_armour_");
    }

    private static void registerTamreliteEquipment(IForgeRegistry<Item> registry) {
        if (!Config.isEquipmentSetEnabled("tamrelite")) {
            return;
        }
        registerItem(registry, createAxeLikeItem("tamrelite_axe", "TAMRELITEAXE", 1, 750, 5.0f, 3.0f, 8, 3.0f, -3.1f, null));
        registerItem(registry, createAxeLikeItem("tamrelite_battleaxe", "TAMRELITEBATTLEAXE", 1, 750, 5.0f, 5.0f, 8, 5.0f, -3.1f, null));
        registerItem(registry, createHoeItem("tamrelite_hoe", "TAMRELITEHOE", 1, 750, 4.0f, 5.0f, 2));
        registerItem(registry, createPickaxeItem("tamrelite_pickaxe", "TAMRELITEPICKAXE", 4, 750, 5.0f, 1.0f, 8));
        registerItem(registry, createShovelItem("tamrelite_shovel", "TAMRELITESHOVEL", 2, 750, 6.0f, 1.0f, 8));
        registerItem(registry, createSwordItem("tamrelite_sword", "TAMRELITESWORD", 1, 750, 4.0f, 1.0f, 8, null));
        registerArmorSet(registry, createArmorMaterial("tamrelite_armour_", "byg:tamralite_", "tamrelite", 44, 4, 7, 8, 4, 10, 2.5f), "tamrelite_armour_");
    }

    private static Item createArmorPiece(ItemArmor.ArmorMaterial material, EntityEquipmentSlot slot, String registryName,
                                         @Nullable Supplier<String> tooltipLine) {
        return new ItemArmor(material, 0, slot) {
            @Override
            public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
                super.addInformation(stack, worldIn, tooltip, flagIn);
                if (tooltipLine != null) {
                    tooltip.add(TextFormatting.AQUA + tooltipLine.get());
                }
            }
        }.setTranslationKey(registryName).setRegistryName(registryName).setCreativeTab(BYGTab.tab);
    }
}

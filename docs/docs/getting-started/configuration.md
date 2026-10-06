---
title: Configuration
sidebar_position: 2
description: "Every BYG config option: biomes, content toggles, food, items, blocks, creatures, equipment and world generation."
---

# Configuration

BYG saves its settings to `config/OhTheBiomesYoullGo.cfg`, which is created the first time you start the game with the mod. Options are grouped into categories; the sections below use the same names as the config file.

:::warning
Options marked **Restart** only take effect after you restart Minecraft. Anything that removes blocks, items or biomes, such as the content toggles and the enabled-biome list, should only be changed **before you create a world**. Removing blocks from an existing world causes missing-mapping errors.
:::

## Biome Settings

Controls which BYG biomes exist and how often they are chosen.

| Setting | Default | Range | Restart | Description |
|---|---|---|:-:|---|
| `enabledBiomes` | all 76 BYG biomes | - | ✔ | List of biome IDs. Only biomes in this list are registered and added to world generation. Remove an entry to disable that biome. IDs can be written as `byg_alps` or `byg:byg_alps`. The IDs are listed on the [Biomes overview](../biomes/overview.md). |
| `biomeWeightOverrides` | empty | - | ✔ | Entries in the form `byg_alps=8`. Weights are relative inside a biome's climate group: a biome with weight 8 is chosen about twice as often as one with weight 4. A weight of 0 stops a biome from being chosen. |
| `globalBiomeWeightMultiplier` | `1.0` | 0.0 to 10.0 | ✔ | Multiplies every default or overridden weight. 0.5 halves them; 0.0 stops all BYG biomes from being selected. |
| `addBiomesToSpawnList` | `true` | - | ✔ | Whether enabled BYG biomes are added to Forge's spawn-biome list for creature spawning. |

## Content Settings

Each option turns a whole content set on or off. Turning a set off removes its blocks, items, recipes and world generation, and it also disables any biome that depends on it. All of these need a restart and should be changed before creating a world. Turning off a biome in Biome Settings only removes the biome and keeps its content.

**All options in this category default to `true`.**

### Ore sets and equipment

| Setting | What turning it off does |
|---|---|
| `kasaiContentEnabled` | Disable Kasai ore, storage block, ingot, chain plating, equipment, recipes, and generation. |
| `lathariumContentEnabled` | Disable Latharium ore, storage block, gem, equipment, recipes, and generation. |
| `pendoriteContentEnabled` | Disable Pendorite ore, storage block, gem, equipment, recipes, and generation. |
| `tamreliteContentEnabled` | Disable Tamrelite ore, storage block, gem, equipment, recipes, and generation. |
| `kasaiEquipmentEnabled` | Set to false to disable the Kasai armour, weapon and tool set only; Kasai ore and materials stay. |
| `lathariumEquipmentEnabled` | Set to false to disable the Latharium armour, weapon and tool set only; Latharium ore and materials stay. |
| `pendoriteEquipmentEnabled` | Set to false to disable the Pendorite armour, weapon and tool set only; Pendorite ore and materials stay. |
| `tamreliteEquipmentEnabled` | Set to false to disable the Tamrelite armour, weapon and tool set only; Tamrelite ore and materials stay. |

### Trees and their biomes

| Setting | What turning it off does |
|---|---|
| `redwoodContentEnabled` | Set to false to disable Redwood Tropics and all redwood blocks and recipes. Removing byg_redwood_tropics from Enabled Biomes disables only the biome; redwood content remains available when this is true. |
| `skyrisContentEnabled` | Set to false to disable Skyris Highlands and all skyris blocks, fruit items, and recipes. Removing byg_skyris_highlands from Enabled Biomes disables only the biome; skyris content remains available when this is true. |
| `cikaContentEnabled` | Set to false to disable Cika Forest and all cika blocks, items, and recipes. Removing byg_cika_forest from Enabled Biomes disables only the biome; cika content remains available when this is true. |
| `enchantedTreeContentEnabled` | Set to false to disable Enchanted Forest and its blue, pink, purple, and green enchanted tree blocks and recipes. The enchanted stick item remains registered for recipes that use it, but its crafting recipe is disabled with the trees. Removing byg_enchanted_forest from Enabled Biomes disables only the biome; enchanted tree content remains available when this is true. |
| `cypressContentEnabled` | Set to false to disable Cypress Swamplands and all cypress blocks, items, and recipes. Removing byg_cypress_swamplands from Enabled Biomes disables only the biome; cypress content remains available when this is true. When disabled, floating logs in Marshlands use willow instead of cypress. |
| `ebonyContentEnabled` | Set to false to disable Ebony Woods and all ebony blocks, items, and recipes. Removing byg_ebony_woods from Enabled Biomes disables only the biome; ebony content remains available when this is true. |
| `greatOakContentEnabled` | Set to false to disable Great Oak Lowlands and all great oak blocks, items, and recipes. Removing byg_great_oak_lowlands from Enabled Biomes disables only the biome; great oak content remains available when this is true. |
| `baobabContentEnabled` | Set to false to disable Baobab Savanna and all baobab blocks, items, crafting recipes (including 1 fruit to 1 powder), and Clarity brewing with baobab powder. Removing byg_baobab_savanna from Enabled Biomes disables only the biome; baobab content remains available when this is true. |
| `mangroveContentEnabled` | Set to false to disable Mangrove Marshes and all mangrove blocks, items, and recipes. Removing byg_mangrove_marshes from Enabled Biomes disables only the biome; mangrove content remains available when this is true. |
| `palmContentEnabled` | Set to false to disable palm trees: all palm blocks, the palm door, recipes, the palm sapling, and palm tree generation (Tropical Islands and beaches). Palm has no biome of its own, so no biome is affected. |
| `cherryContentEnabled` | Set to false to disable Cherry Grove and all cherry blocks, items, and recipes. Removing byg_cherry_grove from Enabled Biomes disables only the biome; cherry content remains available when this is true. |
| `firContentEnabled` | Set to false to disable fir blocks, items, and recipes, and the Coniferous Forest and Snowy Coniferous Forest biomes that grow them. Removing those biomes from Enabled Biomes disables only the biomes; fir content remains available when this is true. |
| `mapleContentEnabled` | Set to false to disable Maple Forest and all maple blocks, the maple tap, sappy maple logs, maple sap, maple syrup, maple pancakes, and recipes. Maple sap, maple syrup, and maple pancakes stay registered, but sap can no longer be collected without a tap. Removing byg_maple_taiga from Enabled Biomes disables only the biome; maple content remains available when this is true. |
| `pineContentEnabled` | Set to false to disable pine blocks, items, and recipes, the pine house and campsite, and the Pine Lowlands, Pine Mountains, and Snowy Pine Mountains biomes. Removing those biomes from Enabled Biomes disables only the biomes; pine content remains available when this is true. |
| `zelkovaContentEnabled` | Set to false to disable Zelkova Forest and all zelkova blocks, items, and recipes. Removing byg_zelkova_forest from Enabled Biomes disables only the biome; zelkova content remains available when this is true. |
| `mahoganyContentEnabled` | Set to false to disable mahogany blocks, items, recipes, and mahogany tree generation. Tropical Rainforest and Tropical Mountains keep their other trees. Mahogany has no biome of its own, so no biome is disabled. |
| `jacarandaContentEnabled` | Set to false to disable Jacaranda Forest and all jacaranda blocks, items, and recipes. Amaranth Fields and Allium Fields lose their jacaranda trees. Removing byg_jacaranda_forest from Enabled Biomes disables only that biome. |
| `hollyContentEnabled` | Set to false to disable holly blocks, items (including holly berries), recipes, and holly tree generation. Evergreen Taiga and Snowy Evergreen Taiga lose their holly trees. Holly has no biome of its own, so no biome is disabled. |
| `frozenOakContentEnabled` | Set to false to disable frozen oak blocks, items, recipes, and frozen oak tree generation. Frosty Forest and Northern Forest lose their frozen oaks. Frozen oak has no biome of its own, so no biome is disabled. |
| `rowanContentEnabled` | Set to false to disable rowan blocks, items (including rowan berries), recipes, and rowan tree generation. Also disables Whispering Woods, which grows both rowan and hawthorn. |
| `hawthornContentEnabled` | Set to false to disable hawthorn blocks, items (including hawthorn berries), recipes, and hawthorn tree generation. Also disables Whispering Woods, which grows both rowan and hawthorn. |
| `witchHazelContentEnabled` | Set to false to disable witch hazel blocks, items, recipes, and witch hazel tree generation. Also disables Weeping Witch Forest, whose trees and witch hut depend on it. |
| `aspenContentEnabled` | Set to false to disable aspen blocks, items, recipes, and aspen tree generation. Also disables Aspen Forest. |
| `willowContentEnabled` | Set to false to disable willow blocks, items, recipes, and willow tree generation. Also disables Bayou and Glowshroom Bayou, whose trees and structures are built from willow. Marshlands floating logs fall back to cypress. |
| `ironwoodContentEnabled` | Set to false to disable ironwood blocks, items, recipes, and ironwood tree generation. Lush Desert and the canyons keep their other features but lose their ironwood trees. |
| `rainbowEucalyptusContentEnabled` | Set to false to disable rainbow eucalyptus blocks, items (including the eucalyptus door), recipes, and tree generation. No biome is disabled. |
| `glowcaneContentEnabled` | Set to false to disable glowcane, glowcelium, their items and recipes, and glowcane structures (including the enchanted village). Also disables Glowshroom Bayou. |
| `oakVariantsContentEnabled` | Set to false to disable the red, orange, dry brown and dry green oak variants: their saplings, leaves, recipes, and tree generation. Also disables Red Oak Forest, Seasonal Forest, Seasonal Deciduous and Stone Pillar Savanna. Great oak and frozen oak have their own toggles. |
| `paloVerdeContentEnabled` | Set to false to disable palo verde blocks, recipes, and palo verde tree generation. No biome is disabled. |
| `orchardContentEnabled` | Set to false to disable orchard (apple tree) blocks, recipes, and orchard tree generation. Also disables Orchard. |
| `stellataContentEnabled` | Set to false to disable stellata tree blocks, recipes, and tree generation. Also disables Stellata Pasture. |
| `birchVariantsContentEnabled` | Set to false to disable the brown, orange, red and yellow birch variants: saplings, leaves, and tree generation. Also disables Seasonal Birch Forest. |
| `spruceVariantsContentEnabled` | Set to false to disable the blue, orange, red and yellow spruce variants: saplings, leaves, and tree generation. Also disables Blue Taiga, Giant Blue Spruce Taiga, Seasonal Taiga and Giant Seasonal Spruce Taiga. |

### Stone, sand and crystals

| Setting | What turning it off does |
|---|---|
| `crystalContentEnabled` | Set to false to disable the Crystal Canyons biome and all crystal blocks, crystal items, recipes, and crystal canyon generation. Crystal crawlers only spawn in the Crystal Canyons, so they no longer appear naturally. |
| `coloredSandContentEnabled` | Set to false to disable the light blue, pink, purple and white sands and their sandstone, chiseled and smooth variants, plus their recipes and sand deposits. Also disables Mangrove Marshes (white sand) and Crystal Canyons (white sandstone), whose terrain is made of them. Black sand has its own toggle. |
| `blackSandContentEnabled` | Set to false to disable black sand and the black sandstone, chiseled and smooth variants, plus their recipes and the black sand to glass smelting. Also disables Dead Sea, whose terrain is made of black sand. Leave this true to keep Dead Sea while disabling the other coloured sands. |
| `sepiniteContentEnabled` | Set to false to disable sepinite stone blocks, recipes, and sepinite deposit generation. No biome is disabled. |
| `soapstoneContentEnabled` | Set to false to disable soapstone blocks, recipes, and soapstone deposit generation. No biome is disabled. |
| `sodaliteContentEnabled` | Set to false to disable sodalite blocks, recipes, and sodalite deposit generation. No biome is disabled. |
| `scoriaContentEnabled` | Set to false to disable scoria blocks, recipes, and scoria deposit generation. No biome is disabled. |

### Plants, flowers and fruit

| Setting | What turning it off does |
|---|---|
| `cactiContentEnabled` | Set to false to disable the golden spined cactus, mini cactus, prickly pear and Sonoran cacti, their recipes, and their generation. No biome is disabled; Stone Pillar Savanna simply grows no cacti. |
| `mushroomDecorContentEnabled` | Set to false to disable the black puff, shelf fungi, weeping milk cap and wood blewit mushrooms and their generation. Glowshrooms have their own toggle. No biome is disabled. |
| `groundCoverContentEnabled` | Set to false to disable small ground-cover plants and debris: algae, blanket weed, ivy, poison ivy, tiny lilypads, stone pebbles and spikes, thorns, clover, dead grass and leaf piles, and their generation. Reeds, prairie grass and overgrown stone are not included because biomes are built from them. No biome is disabled. |
| `glowshroomContentEnabled` | Set to false to disable glowshroom plants, glowshroom blocks, soups and stew, and their generation. Ancient Forest keeps its ordinary giant mushrooms; Fungal Zombies and Fungal Skeletons stop dropping glowshrooms. |
| `strawberryContentEnabled` | Set to false to disable strawberries, strawberry bushes, wild strawberries, strawberry pie, and their generation. No biome is disabled. |
| `blueberryContentEnabled` | Set to false to disable blueberries, blueberry bushes, blueberry pie, and their generation. No biome is disabled. |
| `rudoContentEnabled` | Set to false to disable rudo beans, rudo stalks, wild rudo, roasted beans, and their generation. No biome is disabled. |
| `cattailContentEnabled` | Set to false to disable cattails, cattail rhizomes, cooked rhizomes, and cattail generation in marshes. No biome is disabled. |
| `salalContentEnabled` | Set to false to disable salal bushes and salal berries, and their generation. Kiwi birds stop being tempted by salal berries. No biome is disabled. |
| `flowersContentEnabled` | Set to false to disable all BYG flowers and petal blocks and their flower generation (salal, strawberry, blueberry and rudo plants have their own toggles). No biome is disabled. |

### Creatures

| Setting | What turning it off does |
|---|---|
| `fungalZombieContentEnabled` | Set to false to disable Fungal Zombies: the mob is not registered, has no spawn egg, and never spawns. Existing mobs of this type disappear from loaded worlds. No blocks or biomes are affected. |
| `fungalSkeletonContentEnabled` | Set to false to disable Fungal Skeletons: the mob and its mushroom blob are not registered, it has no spawn egg, and never spawns. Existing mobs of this type disappear from loaded worlds. No blocks or biomes are affected. |
| `kiwiBirdContentEnabled` | Set to false to disable Kiwi Birds: the mob is not registered, has no spawn egg, and never spawns. Existing mobs of this type disappear from loaded worlds. No blocks or biomes are affected. |
| `crystalCrawlerContentEnabled` | Set to false to disable Crystal Crawlers: the mob is not registered, has no spawn egg, and never spawns. Existing mobs of this type disappear from loaded worlds. No blocks or biomes are affected. |

### Food, structures and other blocks

| Setting | What turning it off does |
|---|---|
| `foodContentEnabled` | Set to false to disable BYG prepared foods (soups, cooked items, berry juice, golden beetroot, pumpkin foods, carved melon and jack-o-melon), their recipes, and their smelting. Foods that belong to a plant set, such as strawberry pie, follow that set instead. No biome is disabled. |
| `structuresContentEnabled` | Set to false to disable BYG villages, huts, houses, camps, outposts, towers and mob structures (template-based structures only). Trees, plants and natural features are controlled by their own toggles. Structures built in code, such as the Dead Sea shipwrecks, are not affected. No biome is disabled. Restart is not required for this option, but it only affects newly generated chunks. |
| `netherFurnaceContentEnabled` | Set to false to disable the nether furnace blocks and recipes. No biome is disabled. |

## Food Settings

Hunger restored (in hunger points, where 2 is one drumstick) and saturation for each BYG food. Restart required after changes.

| Setting prefix | Hunger | Saturation |
|---|:-:|:-:|
| `baobabfruit` | 4 | 0.3 |
| `berryJuice` | 2 | 0.6 |
| `blueberry` | 2 | 0.3 |
| `blueberrypie` | 8 | 0.3 |
| `carrotsoup` | 9 | 0.6 |
| `cookedcarrot` | 4 | 0.3 |
| `cookedpufferfish` | 6 | 0.3 |
| `cookedpumpkinseeds` | 3 | 0.3 |
| `cookedspidereye` | 5 | 0.3 |
| `cookedtropicalfish` | 5 | 0.3 |
| `glowshroomsoupblue` | 8 | 0.6 |
| `glowshroomsouppurple` | 8 | 0.6 |
| `goldenbeetroot` | 6 | 0.3 |
| `greenGlowshroomStew` | 8 | 0.6 |
| `greenapple` | 6 | 0.3 |
| `greenapplepie` | 8 | 0.3 |
| `hawthornberries` | 2 | 0.3 |
| `hollyberries` | 2 | 0.3 |
| `pumpkinbread` | 7 | 0.3 |
| `pumpkinmash` | 3 | 0.6 |
| `rowanberries` | 2 | 0.3 |
| `rudobeans` | 1 | 0.3 |
| `rudobeansroasted` | 3 | 0.3 |
| `kiwiCooked` | 6 | 0.8 |
| `kiwiRaw` | 2 | 0.3 |
| `salalBerry` | 2 | 0.3 |
| `silverApple` | 6 | 0.3 |
| `spidereyesoup` | 10 | 0.6 |
| `strawberry` | 2 | 0.3 |
| `strawberrypie` | 8 | 0.3 |
| `tropicalfishsoup` | 10 | 0.6 |

Each row stands for two options: the prefix followed by `FoodLevel` and by `Saturation`, for example `blueberryFoodLevel`.

## Item Settings

| Setting | Default | Range | Restart | Description |
|---|---|---|:-:|---|
| `baobabfruitAlwaysEdible` | `true` | - |  | - |
| `baobabfruitClearPotionEffects` | `true` | - |  | - |
| `baobabfruitPlantingEnabled` | `true` | - |  | - |
| `baobabfruitPlantingConsumesItem` | `true` | - |  | - |
| `clarityPotionBrewingEnabled` | `true` | - | ✔ | Enable brewing the Potion of Clarity with Baobab Powder. |
| `blueberryPlantingEnabled` | `true` | - |  | - |
| `blueberryPlantingConsumesItem` | `true` | - |  | - |
| `mudBallDamage` | `1.0` | ≥ 0.0 |  | - |
| `berryJuiceHealAmount` | `4.0` | ≥ 0.0 |  | Health restored instantly when drinking Berry Juice, in half-hearts. Set to 0.0 to disable the instant heal. |
| `berryJuiceRegenerationDuration` | `100` | ≥ 0 |  | Duration in ticks of the Regeneration effect applied by Berry Juice. Set to 0 to disable the effect. |
| `mapleSyrupSpeedDuration` | `800` | ≥ 0 |  | Duration in ticks of the Speed I effect applied by Maple Syrup. Set to 0 to disable the effect. 800 ticks is 40 seconds. |
| `mapleSyrupSips` | `3` | 1 to 32 | ✔ | How many sips a bottle of Maple Syrup gives before it is empty. |
| `biomeTeleporterItemsEnabled` | `true` | - | ✔ | Register the creative-only Biome Teleporter item (one stack per vanilla or modded biome, used for testing). |
| `goldenBeetrootRegenDuration` | `2400` | ≥ 0 |  | Duration in ticks of the Regeneration I effect a player gets from eating Golden Beetroot. Set to 0 to disable the effect. 2400 ticks is 2 minutes. |
| `goldenBeetrootPetRegenEnabled` | `true` | - |  | Allow feeding Golden Beetroot to a tamed creature (wolf, cat, parrot, horse...) to give it permanent passive health regeneration. Saved with the creature. |
| `goldenBeetrootPetRegenInterval` | `100` | ≥ 1 |  | Ticks between regeneration pulses for a creature fed Golden Beetroot. Regeneration I heals every 50 ticks, so the default of 100 is half that rate. |
| `goldenBeetrootPetRegenAmount` | `1.0` | ≥ 0.0 |  | Health restored per pulse, in half-hearts. |
| `wormFishingEnhancementEnabled` | `true` | - |  | - |
| `wormLureLevel` | `2` | ≥ 0 |  | Lure level bonus applied to the fishing hook when a worm is consumed on cast. Stacks with the rod's Lure enchantment. |
| `wormFishingJunkRerollChance` | `0.25` | 0.0 to 1.0 |  | - |
| `wormFishingQualityBoostChance` | `0.15` | 0.0 to 1.0 |  | - |
| `wormFishingExtraFishChance` | `0.10` | 0.0 to 1.0 |  | - |

## Block Settings

| Setting | Default | Range | Restart | Description |
|---|---|---|:-:|---|
| `blueberryBushBerryCount` | `2` | - |  | - |
| `strawberryBushBerryCount` | `2` | - |  | - |
| `baobabFruitHarvestCount` | `1` | - |  | - |
| `baobabFruitBreakDropCount` | `3` | - |  | - |
| `blueberryBushStageGrowthChance` | `0.5` | - |  | - |
| `blueberryBushBonemealGrowthChance` | `0.4` | - |  | - |
| `strawberryBushStageGrowthChance` | `0.5` | - |  | - |
| `strawberryBushBonemealGrowthChance` | `0.4` | - |  | - |
| `baobabFruitStageGrowthChance` | `0.3` | - |  | - |
| `baobabFruitBonemealGrowthChance` | `0.4` | - |  | - |
| `baobabFruitStageTickRate` | `1800` | - |  | - |
| `mapleTapStageTickRate` | `200` | ≥ 1 |  | Ticks a Maple Tap takes to fill one of its 3 stages, so a full tap takes three times this. 200 ticks is 10 seconds per stage. |
| `mapleTapLogDepletionChance` | `0.2` | 0.0 to 1.0 |  | Chance (0 to 1) that a Sappy Maple Log turns back into a plain Maple Log each time sap is taken from its tap. |
| `thornblockDamage` | `1.0` | - |  | - |
| `thornBranchesDamage` | `1.0` | - |  | - |
| `cactusDamage` | `1.0` | - |  | - |
| `damagingPlantDamage` | `1.0` | - |  | - |
| `mudMovementMultiplier` | `0.85` | 0.0 to 1.0 |  | Horizontal movement retained while walking through mud. 1.0 disables slowdown; 0.85 is a 15% slowdown. |
| `quagmireSlimeChance` | `0.004` | 0.0 to 0.05 |  | Chance for an eligible quagmire land surface to be a slime pocket. Set to 0.0 to disable slime generation. |
| `springwaterBubbleChance` | `0.4` | - |  | - |
| `springwaterBubbleParticleCount` | `2` | - |  | - |
| `springwaterAppliesRegeneration` | `true` | - |  | Whether Springwater grants Regeneration I while an entity remains in it. |
| `netherFurnaceIgnitesFromFireBelow` | `true` | - |  | - |
| `netherFurnaceTickRate` | `20` | 1 or more |  | Ticks between an unlit Nether Furnace's checks for fire directly beneath it. 20 ticks is about 1 second. |
| `netherFurnaceLitTickRate` | `10` | - |  | - |
| `netherFurnaceAmbientEffectChance` | `0.01` | - |  | - |
| `netherFurnaceAmbientParticleCount` | `5` | - |  | - |
| `poisonIvyAppliesPoison` | `true` | - |  | - |
| `poisonIvyPoisonDuration` | `300` | - |  | - |
| `poisonIvyPoisonAmplifier` | `1` | - |  | - |
| `structureSpawnBlockTickRate` | `2` | - |  | - |
| `coloredCanyonMiddleTemplateChance` | `0.2` | - |  | - |
| `coloredCanyonPrimaryTopTemplateChance` | `0.5` | - |  | - |
| `crystalCanyonBlueTemplateChance` | `0.25` | - |  | - |
| `crystalCanyonPurpleTemplateChance` | `0.25` | - |  | - |
| `crystalCanyonRedTemplateChance` | `0.25` | - |  | - |
| `crystalCanyonWhiteTemplateChance` | `0.25` | - |  | - |

## Creature Settings

| Setting | Default | Range | Restart | Description |
|---|---|---|:-:|---|
| `daytimeWitchesInWeepingWitchForest` | `true` | - | ✔ | Allow witches to spawn naturally on the surface of Weeping Witch Forest during the day. When disabled, vanilla witch spawning rules and weight apply. |
| `kiwiSpawnBiomes` | `{"byg_woodlands", "byg_great_oak_lowlands", "byg_deciduous_forest"}` | - | ✔ | Biome registry IDs where kiwis spawn naturally. Use 'registry_name' for BYG biomes or 'modid:registry_name' for other mods. An empty list disables natural kiwi spawning. |
| `kiwiSpawnWeight` | `25` | ≥ 0 | ✔ | Relative spawn weight for kiwis in the configured biomes. Higher values make them more common; 25 is the default. Set to 0 to disable natural kiwi spawning. |
| `fungalZombieSpawnWeight` | `20` | ≥ 0 | ✔ | Relative spawn weight for fungal zombies in Fungal Jungle. 20 is the default; 0 disables natural spawning. |
| `fungalSkeletonSpawnWeight` | `20` | ≥ 0 | ✔ | Relative spawn weight for fungal skeletons in Fungal Jungle. 20 is the default; 0 disables natural spawning. |
| `fungalZombiePoisonAmplifier` | `0` | 0 to 127 |  | Amplifier of the Poison effect a fungal zombie's hit inflicts. 0 is Poison I, 1 is Poison II, and so on. |
| `fungalZombiePoisonDuration` | `160` | ≥ 0 |  | Duration in ticks of the Poison effect a fungal zombie's hit inflicts. Set to 0 to disable the poison. 160 ticks is 8 seconds. |
| `kiwiForageAttemptInterval` | `120` | ≥ 1 |  | Average interval in ticks between kiwi forage attempts at night. Lower values make kiwis dig for worms more often. |
| `kiwiWormFindChance` | `0.3333333333333333` | 0.0 to 1.0 |  | Chance for a kiwi to find a worm when a forage action completes. |
| `kiwiEggLayInterval` | `12000` | ≥ 1 |  | Minimum ticks between eggs laid by an adult kiwi. A random extra delay of up to the same amount is added, so 12000 means one egg every 10 to 20 minutes. Set very high to make eggs effectively rare. |
| `kiwiMeatDropCount` | `1` | ≥ 0 |  | Number of Raw Kiwi Meat a kiwi drops when killed. Each level of Looting adds up to one extra. Set to 0 to stop kiwis dropping meat. |

## Equipment Settings

Armour stats for each material, and two special armour abilities. Stat changes need a Minecraft restart.

| Material | Durability multiplier | Boots | Leggings | Chestplate | Helmet | Enchantability | Toughness |
|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| Kasai | 30 | 4 | 6 | 6 | 4 | 11 | 3.5 |
| Latharium | 24 | 3 | 5 | 5 | 3 | 16 | 2.0 |
| Pendorite | 25 | 2 | 2 | 4 | 2 | 15 | 1.0 |
| Tamrelite | 44 | 4 | 7 | 8 | 4 | 10 | 2.5 |

| Setting | Default |
|---|---|
| `lathariumBootsLevitationEnabled` | `true` |
| `lathariumBootsLevitationDuration` | `40` |
| `lathariumBootsLevitationAmplifier` | `2` |
| `lathariumBootsParticleChance` | `0.1` |
| `lathariumBootsParticleCount` | `5` |
| `kasaiFireWardEnabled` | `true` |
| `kasaiFireWardDuration` | `600` |
| `kasaiFireWardCooldown` | `6000` |

## Worldgen Settings

Tuning for world generation in new chunks. Attempt multipliers change how many tries the generator makes; the template multiplier changes spawn chances and the vein size multiplier changes deposit size. A value of 1.0 is the default. The `generate…` switches turn individual features on or off.

| Setting | Default | Range | Restart | Description |
|---|---|---|:-:|---|
| `featureDimensions` | `[0]` |  |  | Dimension IDs where BYG's surface and underground world features generate (structures, trees, plants, ground patches and overworld ores). Features still only appear in their matching biomes, so add a dimension only if it uses BYG or vanilla overworld biomes. Kasai ore always generates in the Nether. |
| `templateChanceMultiplier` | `1.0` | 0.0 to 10.0 |  | Multiplies the per-chunk spawn chance of BYG template-based and related rare features. 0 skips these chance checks; higher values cannot raise a chance above 100%. |
| `flowerAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Multiplies BYG flower patch placement attempts in matching biomes when generating new chunks. 0 skips these attempts. Increase gradually: higher values add generation work but do not guarantee proportionally more flowers because placement needs valid spots. |
| `clusterPlantAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Multiplies BYG clustered plant and ground-cover patch attempts when generating new chunks, including reeds, shrubs, and forest floor plants. 0 skips most scaled attempts. Increase gradually: this affects many features and adds generation work, while valid placement spots still limit results. |
| `sandAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Multiplies attempts to replace vanilla sand with BYG coloured sand deposits in eligible biomes. 0 skips these attempts. More attempts add generation work and need suitable sand to replace. |
| `undergroundDepositAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Multiplies attempts for BYG underground deposits, including ores, mud, peat, scoria, sepinite, and soapstone. 0 skips these attempts. Ore-specific attempt multipliers also apply. |
| `undergroundDepositVeinSizeMultiplier` | `1.0` | 0.1 to 10.0 |  | Multiplies the size of each BYG underground deposit vein, including ores, mud, peat, scoria, sepinite, and soapstone. This changes vein size, not the number of attempts. |
| `kasaiOreAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Kasai ore generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `lathariumOreAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Latharium ore generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `pendoriteOreAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Pendorite ore generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `tamreliteOreAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Tamrelite ore generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `scoriaAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Scoria generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `scoriaMinY` | `1` | 1 to 255 |  | Lowest Y level where scoria deposits can start. |
| `scoriaMaxY` | `14` | 1 to 255 |  | Highest Y level where scoria deposits can start. Must be at least the minimum Y. |
| `sepiniteAttemptMultiplier` | `1.0` | 0.0 to 10.0 |  | Sepinite generation attempts relative to default. Multiplies the global underground deposit attempt setting; 0 disables attempts in new chunks. |
| `sepiniteMinY` | `60` | 1 to 255 |  | Lowest Y level where sepinite deposits can start. |
| `scoriaBiomes` | `{}` | - |  | Biome registry IDs where scoria deposits may generate. Use 'registry_name' for BYG biomes or 'modid:registry_name' for other mods. An empty list (the default) lets scoria generate in every biome. |
| `sepiniteMaxY` | `119` | 1 to 255 |  | Highest Y level where sepinite deposits can start. Must be at least the minimum Y. |

### Feature switches
| `soapstoneBiomes` | `{}` | - |  | Biome registry IDs where soapstone deposits may generate. Use 'registry_name' for BYG biomes or 'modid:registry_name' for other mods. An empty list (the default) lets soapstone generate in every biome. |

All of these default to `true`.

`generateEnchantedVillage`, `generatePaloTrees`, `generateRockySurfaceFeatures`, `generateDeadGrassClusters`, `generateShortDeadGrassClusters`, `generateAlgaePatches`, `generateGlowcaneBlue`, `generateGlowcanePink`, `generateGlowcanePurple`, `generateGlowcaneRed`, `generateGoldenSpinedCactus`, `generateMinicactus`, `generatePricklyPear`, `generateKasaiOre`, `generateLathariumOre`, `generateMudDeposits`, `generatePeatDeposits`, `generatePendoriteOre`, `generateScoriaDeposits`, `generateSepiniteDeposits`, `generateSoapstoneDeposits`, `generateTamreliteOre`, `generateLightBlueSand`, `generatePinkSand`, `generatePurpleSand`, `generatePeatgrass`, `generateRockyGrass`, `generateRockyGrassAlps`, `generateRockystone`, `generateRockystone2`, `generateSandygrass`, `generateSodalite`

`generateRockystone` controls Rocky Stone in Pine Mountains and Snowy Pine Mountains. `generateRockystone2` controls Rocky Stone in Bluff Mountains and Stone Brushlands. Both also require `generateRockySurfaceFeatures` to be enabled.

## Client Settings

| Setting | Default | Description |
|---|---|---|
| `enableBiomeFog` | `true` | Enable BYG fog in the Bayou, Cypress Swamplands, Mangrove Marshes, Bog, Glowshroom Bayou, and Dead Sea. Vanilla fog is unaffected. |

:::note
Descriptions are the mod's own config comments where they exist; options without a comment show a dash.
:::

---
title: Misc Items
sidebar_position: 3
description: The Biome Teleporter, mud balls, worms and fishing, the wooden mortar, sticks, crystals, glowcane and other BYG items.
---

# Misc Items

Food is on the [Food](food.md) page and ore, armour and tool items are on the [Equipment](equipment.md) page.

## Biome Teleporter

<ItemIcon id="biome_teleporter" name="Biome Teleporter" size={96} />

A **creative-only** testing item, added when `biomeTeleporterItemsEnabled` is on. The creative tab has one teleporter for every biome, including vanilla and other mods' biomes, with BYG's biomes listed first.

Right-click a teleporter, and it teleports you to the **nearest instance of that biome**, searching up to about 25,000 blocks.

## Mud balls

<ItemIcon id="mud_balls" name="Mud Balls" size={96} />

Mud Balls come from **Mud** blocks (a mud block drops 4). Right-click to **throw** one like a snowball. It deals **1 damage** to what it hits (configurable with `mudBallDamage`) and breaks into a puff of particles. Four mud balls craft into a Mud Bricks block.

## Worms and fishing

<ItemIcon id="worm" name="Worm" size={96} />

A **Worm** makes fishing faster. Kiwi birds dig them up (see [Kiwi Bird](../creatures/kiwi-bird.md)).

- **Keep a worm in your inventory** when you cast a fishing rod. One worm is used up and the hook gets a **Lure bonus of +2** (configurable with `wormLureLevel`), which stacks with the rod's Lure enchantment and shortens the wait for a bite.
- If you **reel in without catching anything, you get the worm back**. A worm is only used up when the cast catches something.
- The whole effect can be switched off with `wormFishingEnhancementEnabled`.

## Kiwi Egg

<ItemIcon id="kiwi_egg" name="Kiwi Egg" size={96} />

Adult kiwi birds lay a **Kiwi Egg** every so often (see [Kiwi Bird](../creatures/kiwi-bird.md)). It stacks to 16 and counts as an egg in recipes, so it can replace the egg in blueberry, strawberry and green apple pies and in maple pancakes.

Like a chicken egg, it can also be **thrown** (right-click, or fire it from a dispenser). A thrown egg does no damage, but has a 1 in 8 chance of hatching a baby kiwi where it lands, and a small chance of hatching four.

## Wooden Mortar

<ItemIcon id="wooden_mortar" name="Wooden Mortar" size={96} />

The mortar is a **tool that is not used up**. It stays in the crafting grid when you craft with it, so one mortar lasts forever. It is used to:

- make [Berry Juice](food.md#berry-juice) (the mortar, 5 berries and a glass bottle), and
- grind **one flower into one vanilla dye**. The dye depends on the flower's color group, for example a red flower gives rose red and a purple flower gives purple dye. Several color groups (cyan/blue, white, pink/light pink) give the same dye.

No crafting recipe for the mortar exists in the mod files, so it comes from the creative tab.

## Sticks

| Icon | Item | How to get it | Used for |
|:-:|---|---|---|
| <ItemIcon id="stone_stick" name="Stone Stick" size={32} inline /> | Stone Stick | Stone and cobblestone (shapeless) gives 4. | Kasai and Pendorite tools |
| <ItemIcon id="enchanted_stick" name="Enchanted Stick" size={32} inline /> | Enchanted Stick | 2 enchanted planks gives 4. | Latharium tools |

## Crystals

<ItemIcon id="light_blue_crystals" name="Light Blue Crystals" size={96} />

The four crystal blocks (light blue, purple, red and white) are glowing blocks found in the Crystal Canyons. Breaking one drops its crystals: **3** for light blue, purple and white, and **1** for red, with a 50% chance of one extra red crystal. Four crystals of one color craft back into the matching crystal block.

| <ItemIcon id="light_blue_crystals" name="Light Blue Crystals" size={40} inline /> | <ItemIcon id="purple_crystals" name="Purple Crystals" size={40} inline /> | <ItemIcon id="red_crystals" name="Red Crystals" size={40} inline /> | <ItemIcon id="white_crystals" name="White Crystals" size={40} inline /> |
|:-:|:-:|:-:|:-:|
| Light Blue | Purple | Red | White |

## Glowcane

<ItemIcon id="glowcane_stalk_blue" name="Glowcane Stalk (blue)" size={96} />

Glowcane comes in blue, pink, purple and red. A **Glowcane Stalk** can only be planted on **Glowcelium**, and a stalk crafts into one **Glowcane Dust** of the same color. Four dust craft into a glowcane block. Glowcane generates in the Glowshroom Bayou.

| Stalk | | Dust | |
|:-:|---|:-:|---|
| <ItemIcon id="glowcane_stalk_blue" name="Blue Glowcane Stalk" size={32} inline /> | Blue | <ItemIcon id="glowcane_dust_blue" name="Blue Glowcane Dust" size={32} inline /> | Blue |
| <ItemIcon id="glowcane_stalk_pink" name="Pink Glowcane Stalk" size={32} inline /> | Pink | <ItemIcon id="glowcane_dust_pink" name="Pink Glowcane Dust" size={32} inline /> | Pink |
| <ItemIcon id="glowcane_stalk_purple" name="Purple Glowcane Stalk" size={32} inline /> | Purple | <ItemIcon id="glowcane_dust_purple" name="Purple Glowcane Dust" size={32} inline /> | Purple |
| <ItemIcon id="glowcane_stalk_red" name="Red Glowcane Stalk" size={32} inline /> | Red | <ItemIcon id="glowcane_dust_red" name="Red Glowcane Dust" size={32} inline /> | Red |

## Plants you can pick up and place

| Icon | Item | Notes |
|:-:|---|---|
| <ItemIcon id="cattail" name="Cattail" size={32} inline /> | Cattail | Placed on **grass or mud**; plants a cattail plant. |
| <ItemIcon id="cattail_rhizome" name="Cattail Rhizome" size={32} inline /> | Cattail Rhizome | A food ingredient (see [Food](food.md)). |
| <ItemIcon id="reeds" name="Reeds" size={32} inline /> | Reeds | Placed on **grass or mud**. |

**Glowshrooms** (blue, green and purple) are placed on the **top** of a block and grow from a small glowshroom.

## Baobab Powder

<ItemIcon id="baobab_powder" name="Baobab Powder" size={96} />

Made from Baobab Fruit (1 fruit gives 1 powder). It is the ingredient for the [Potion of Clarity](../brewing.md).

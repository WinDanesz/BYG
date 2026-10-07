---
title: Sand and Soil
sidebar_position: 2
description: BYG's coloured and black sand, mud, peat, rocky grass and the other ground blocks - where each one generates.
---

# Sand and Soil

## Coloured sands

Four coloured sands generate as beach deposits, plus black sand as its own biome's terrain.

| Sand | Where | Toggle |
|---|---|---|
| Light Blue Sand | Beaches in Tropical Islands, deposits of ~20 blocks, 44 attempts per chunk | `coloredSandContentEnabled` |
| Pink Sand | Beaches in Tropical Islands, ~27 blocks, 40 attempts | `coloredSandContentEnabled` |
| Purple Sand | Beaches in Tropical Islands, ~26 blocks, 40 attempts | `coloredSandContentEnabled` |
| White Sand | Mangrove Marshes and Crystal Canyons terrain | `coloredSandContentEnabled` |
| Black Sand | Dead Sea terrain | `blackSandContentEnabled` |

Each colour has matching Sandstone, Chiseled Sandstone and Smooth Sandstone blocks: 4 sand craft into sandstone, 2 sandstone craft into 1 chiseled sandstone, and 4 sandstone craft into 4 smooth sandstone. Black and White sand also smelt directly into black or clear stained glass. All coloured sands fall like vanilla sand.

## Mud, cracked sand and dirt variants

| Block | Notes                                                                                                                                                                                                                                                                                                |
|---|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Mud | Generates as a deposit in Great Lakes and some swampy biomes. Slows movement while you stand on it (a configurable multiplier, default 0.925×) and is 0.9 blocks tall. Breaking it drops 4 Mud Balls, which can be used as a throwable (see [Misc Items](../items-and-food/misc-items.md#mud-balls)). |
| Cracked Sand | A plantable falling sand variant.                                                                                                                                                                                                                                                                    |
| Hardened Dirt | A hardened, pickaxe-mined dirt variant, used as ground in some biomes.                                                                                                                                                                                                                               |
| Meadow Dirt, Pasture Dirt, Sandy Dirt | Biome-specific dirt variants that pair with their biome's grass block.                                                                                                                                                                                                                               |
| Peat Dirt | Generates as a deposit in Weeping Witch Forest (replacing grass, up to 20 blocks). Its grass form, Peatgrass, drops Peat Dirt when broken.                                                                                                                                                           |

## Rocky and sandy grass

These are surface-replacement blocks: BYG scans a biome's existing grass or snow and swaps some of it for these variants after normal generation.

| Block | Replaces | Where | Toggle |
|---|---|---|---|
| Rocky Grass | Grass or Peatgrass | Bluff Mountains, Stone Brushlands, Y 70–207 | `generateRockyGrass` |
| Rocky Grass | Snow | Alps, Y 153–162 | `generateRockyGrassAlps` |
| Rocky Stone | Grass or Peatgrass | Pine Mountains, Snowy Pine Mountains, Y 117–253 | `generateRockystone` |
| Rocky Stone | Grass, Peatgrass or Rocky Grass | Bluff Mountains, Stone Brushlands, Y 70–207 | `generateRockystone2` |
| Sandy Grass | Sand or Hardened Clay/Dirt | Lush Desert, Outback, Y 60–101 | `generateSandygrass` |
| Peatgrass | Grass | Pine Mountains, Boreal Forest, Coniferous Forest, Pine Lowlands, anywhere on the surface | `generatePeatgrass` |

Each of these switches is a feature toggle under [Worldgen Settings](../getting-started/configuration.md#worldgen-settings).

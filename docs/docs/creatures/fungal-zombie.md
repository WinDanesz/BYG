---
title: Fungal Zombie
sidebar_position: 4
description: The Fungal Zombie - spawn biome, combat stats and drops.
---

# Fungal Zombie

<BiomeScreenshot id="fungal_zombie" name="Fungal Zombie" />

*A jungle-rotted shambler that never stopped growing mushrooms out of its own decay.*

A hostile zombie variant, only appears if glowshroom (`glowshroomContentEnabled`) and Fungal Zombie content (`fungalZombieContentEnabled`) are both enabled. See [Creature Settings](../getting-started/configuration.md#creature-settings) and the [Content Settings](../getting-started/configuration.md#content-settings) to adjust its spawn weight or disable it.

| Stat | Value |
|---|---|
| Spawn biome | Fungal Jungle |
| Spawn weight | 20 (`fungalZombieSpawnWeight`) |
| Health | 35 |
| Attack damage | 6 |
| Movement speed | 0.2 |
| Armour | 0 |
| Hit effect | Poison I for 8 seconds (`fungalZombiePoisonAmplifier`, `fungalZombiePoisonDuration`) |
| Drops | Rotten flesh as usual; rarely a Green Glowshroom |

- Behaves like a vanilla zombie: breaks down doors, avoids sunlight and swims, but causes poison on hit.
- Killing one is the **Spore Wars** [advancement](../advancements.md).

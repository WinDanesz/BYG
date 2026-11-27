package windanesz.byg.compat;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rtg.api.RTGAPI;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.terrain.TerrainBase;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAlliumFields;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAlps;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAmaranthFields;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAncientForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAspenForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBaobabSavanna;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBayou;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBlueTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBluffMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBog;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBorealForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCanyons;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGChaparralLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCherryGrove;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCikaForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGConiferousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCyprusSwampland;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDeadSea;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDeciduousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDoverMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDunes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEbonyWoods;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEnchantedForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEvergreenTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGFrostyForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGFungalJungle;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGiantBlueSpruceTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGiantSeasonalSpruceTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGlaciers;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGlowshroomBayou;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGrasslandPlateau;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGreatLakes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGreatOakLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGJacarandaForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGLushDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMangroveMarshes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMapleForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMeadow;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOrchard;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOutback;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOutlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPineLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPineMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPrairie;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGQuagmire;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedOakForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedOutlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedwoodTropics;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSavannaCanopy;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalBirchForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalDeciduous;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGShrublands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSkyrisHighlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyConiferousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyEvergeenTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyPineMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSonoranDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGStellataPasture;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGStoneBrushlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalIslands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalRainforest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWeepingWitchForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWhisperingWoods;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWoodlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGZelkovaForest;
import windanesz.byg.BiomesYouGo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class RTGCompat {
    private static final Logger LOGGER = LogManager.getLogger(BiomesYouGo.MODID);
    private static final String MOD_ID = "rtg";

    private RTGCompat() {
    }

    public static void init() {
        if (Loader.isModLoaded(MOD_ID)) {
            RTGBiomes.register();
        }
    }

    // RTG's built-in BYG biomes only match another BYG port's names, so ours would fall back to RTG's plains. Runs
    // before RTG's postInit, which builds its patch biome from the biomes registered so far. Where the port's terrain
    // ends up far from BYG's own heights, the entry keeps the port's surface and decorations but swaps in BYGTerrain.
    static final class RTGBiomes {
        private static final Map<String, Function<Biome, IRealisticBiome>> BIOMES = new LinkedHashMap<>();

        static {
            BIOMES.put("allium_fields", biome -> new RealisticBiomeBYGAlliumFields(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("alps", biome -> new RealisticBiomeBYGAlps(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("amaranth_fields", biome -> new RealisticBiomeBYGAmaranthFields(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("ancient_forest", RealisticBiomeBYGAncientForest::new);
            BIOMES.put("aspen_forest", biome -> new RealisticBiomeBYGAspenForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("baobab_savanna", RealisticBiomeBYGBaobabSavanna::new);
            BIOMES.put("bayou", RealisticBiomeBYGBayou::new);
            BIOMES.put("blue_taiga", biome -> new RealisticBiomeBYGBlueTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("bluff_mountains", biome -> new RealisticBiomeBYGBluffMountains(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("bog", biome -> new RealisticBiomeBYGBog(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("boreal_forest", biome -> new RealisticBiomeBYGBorealForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("chaparral_lowlands", biome -> new RealisticBiomeBYGChaparralLowlands(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("cherry_grove", RealisticBiomeBYGCherryGrove::new);
            BIOMES.put("cika_forest", biome -> new RealisticBiomeBYGCikaForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("coniferous_forest", biome -> new RealisticBiomeBYGConiferousForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("dead_sea", biome -> new RealisticBiomeBYGDeadSea(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("deciduous_forest", biome -> new RealisticBiomeBYGDeciduousForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("dover_mountains", biome -> new RealisticBiomeBYGDoverMountains(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("dunes", biome -> new RealisticBiomeBYGDunes(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("ebony_woods", biome -> new RealisticBiomeBYGEbonyWoods(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("enchanted_forest", biome -> new RealisticBiomeBYGEnchantedForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("evergreen_taiga", biome -> new RealisticBiomeBYGEvergreenTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("frosty_forest", biome -> new RealisticBiomeBYGFrostyForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("fungal_jungle", RealisticBiomeBYGFungalJungle::new);
            BIOMES.put("giant_blue_spruce_taiga", biome -> new RealisticBiomeBYGGiantBlueSpruceTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("giant_seasonal_spruce_taiga", biome -> new RealisticBiomeBYGGiantSeasonalSpruceTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("glaciers", biome -> new RealisticBiomeBYGGlaciers(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("glowshroom_bayou", RealisticBiomeBYGGlowshroomBayou::new);
            BIOMES.put("grassland_plateau", biome -> new RealisticBiomeBYGGrasslandPlateau(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("great_lakes", biome -> new RealisticBiomeBYGGreatLakes(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("great_oak_lowlands", RealisticBiomeBYGGreatOakLowlands::new);
            BIOMES.put("jacaranda_forest", biome -> new RealisticBiomeBYGJacarandaForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("lush_desert", RealisticBiomeBYGLushDesert::new);
            BIOMES.put("mangrove_marshes", RealisticBiomeBYGMangroveMarshes::new);
            BIOMES.put("meadow", RealisticBiomeBYGMeadow::new);
            BIOMES.put("orchard", RealisticBiomeBYGOrchard::new);
            BIOMES.put("outback", RealisticBiomeBYGOutback::new);
            BIOMES.put("outlands", RealisticBiomeBYGOutlands::new);
            BIOMES.put("pine_lowlands", RealisticBiomeBYGPineLowlands::new);
            BIOMES.put("pine_mountains", biome -> new RealisticBiomeBYGPineMountains(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("prairie", biome -> new RealisticBiomeBYGPrairie(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("quagmire", RealisticBiomeBYGQuagmire::new);
            BIOMES.put("red_desert", RealisticBiomeBYGRedDesert::new);
            BIOMES.put("red_oak_forest", RealisticBiomeBYGRedOakForest::new);
            BIOMES.put("red_outlands", RealisticBiomeBYGRedOutlands::new);
            BIOMES.put("redwood_tropics", biome -> new RealisticBiomeBYGRedwoodTropics(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("savanna_canopy", RealisticBiomeBYGSavannaCanopy::new);
            BIOMES.put("seasonal_birch_forest", biome -> new RealisticBiomeBYGSeasonalBirchForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("seasonal_deciduous", biome -> new RealisticBiomeBYGSeasonalDeciduous(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("seasonal_forest", biome -> new RealisticBiomeBYGSeasonalForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("seasonal_taiga", biome -> new RealisticBiomeBYGSeasonalTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("shrublands", biome -> new RealisticBiomeBYGShrublands(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("skyris_highlands", biome -> new RealisticBiomeBYGSkyrisHighlands(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("snowy_coniferous_forest", biome -> new RealisticBiomeBYGSnowyConiferousForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("snowy_pine_mountains", biome -> new RealisticBiomeBYGSnowyPineMountains(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("sonoran_desert", RealisticBiomeBYGSonoranDesert::new);
            BIOMES.put("stellata_pasture", biome -> new RealisticBiomeBYGStellataPasture(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("stone_brushlands", biome -> new RealisticBiomeBYGStoneBrushlands(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("tropical_islands", biome -> new RealisticBiomeBYGTropicalIslands(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("tropical_mountains", biome -> new RealisticBiomeBYGTropicalMountains(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("tropical_rainforest", RealisticBiomeBYGTropicalRainforest::new);
            BIOMES.put("weeping_witch_forest", RealisticBiomeBYGWeepingWitchForest::new);
            BIOMES.put("whispering_woods", biome -> new RealisticBiomeBYGWhisperingWoods(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("woodlands", RealisticBiomeBYGWoodlands::new);
            BIOMES.put("zelkova_forest", biome -> new RealisticBiomeBYGZelkovaForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            // RTG's class names misspell these two
            BIOMES.put("cypress_swamplands", RealisticBiomeBYGCyprusSwampland::new);
            BIOMES.put("snowy_evergreen_taiga", biome -> new RealisticBiomeBYGSnowyEvergeenTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            // RTG has no port of these, so they borrow the closest one
            BIOMES.put("colored_canyons", biome -> new RealisticBiomeBYGCanyons(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("crystal_canyons", biome -> new RealisticBiomeBYGCanyons(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("maple_taiga", RealisticBiomeBYGMapleForest::new);
            BIOMES.put("flowering_plains", RealisticBiomeBYGMeadow::new);
            BIOMES.put("giant_snowy_spruce_taiga", biome -> new RealisticBiomeBYGGiantSeasonalSpruceTaiga(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("marshlands", RealisticBiomeBYGMangroveMarshes::new);
            BIOMES.put("stone_pillar_savanna", biome -> new RealisticBiomeBYGBaobabSavanna(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("northern_forest", biome -> new RealisticBiomeBYGBorealForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
            BIOMES.put("snowy_deciduous_forest", biome -> new RealisticBiomeBYGDeciduousForest(biome) {
                @Override public TerrainBase initTerrain() { return new BYGTerrain(baseBiome()); }
            });
        }

        @SuppressWarnings("unchecked")
        static void register() {
            // RTG 6 declares RTG_BIOMES as its own BiomeMap and RTG Plus as a plain Map, so a direct field access only
            // links against one of them. Both are Maps keyed by the base biome.
            Map<Biome, IRealisticBiome> rtgBiomes;
            try {
                rtgBiomes = (Map<Biome, IRealisticBiome>) RTGAPI.class.getField("RTG_BIOMES").get(null);
            } catch (ReflectiveOperationException | ClassCastException e) {
                LOGGER.warn("Could not find Realistic Terrain Generation's biome registry, BYG biomes will generate as plains", e);
                return;
            }
            int added = 0;
            for (Map.Entry<String, Function<Biome, IRealisticBiome>> entry : BIOMES.entrySet()) {
                // Disabled biomes aren't registered
                Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(BiomesYouGo.MODID, "byg_" + entry.getKey()));
                if (biome != null) {
                    rtgBiomes.put(biome, entry.getValue().apply(biome));
                    added++;
                }
            }
            LOGGER.info("Added {} biomes to Realistic Terrain Generation", added);

            List<ResourceLocation> missing = new ArrayList<>();
            for (Biome biome : ForgeRegistries.BIOMES) {
                ResourceLocation name = biome.getRegistryName();
                if (name != null && BiomesYouGo.MODID.equals(name.getNamespace())
                        && !BIOMES.containsKey(name.getPath().replaceFirst("^byg_", ""))) {
                    missing.add(name);
                }
            }
            if (!missing.isEmpty()) {
                LOGGER.warn("No Realistic Terrain Generation terrain for {}, these generate as plains", missing);
            }
        }
    }

    // RTG terrain ignores the biome's base height and variation. This one follows them: in normal worlds a BYG
    // biome's median surface is about 63 + 17 * base height, and its hills grow with the height variation.
    static final class BYGTerrain extends TerrainBase {
        private static final float HILLS_PER_VARIATION = 120;
        // Halves the wavelength of RTG's hills, which are flat over half their area, so that every patch of a
        // mountain biome gets some peaks
        private static final float HILL_FREQUENCY = 2;
        private static final float GROUND_NOISE = 4;
        private static final float GROUND_NOISE_PER_VARIATION = 8;

        private final float level;
        private final float hillStrength;
        private final float groundStrength;

        BYGTerrain(Biome biome) {
            level = 63 + 17 * biome.getBaseHeight();
            hillStrength = HILLS_PER_VARIATION * biome.getHeightVariation();
            groundStrength = GROUND_NOISE + GROUND_NOISE_PER_VARIATION * biome.getHeightVariation();
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
            float height = level + hills(x * HILL_FREQUENCY, y * HILL_FREQUENCY, hillStrength, rtgWorld)
                    + groundNoise(x, y, groundStrength, rtgWorld);
            return riverized(mountainCap(height), river);
        }
    }
}

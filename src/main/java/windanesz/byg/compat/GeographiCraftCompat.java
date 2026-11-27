package windanesz.byg.compat;

import climateControl.api.BiomePackage;
import climateControl.api.BiomePackageRegistry;
import climateControl.api.BiomeSettings;
import climateControl.api.Climate;
import com.Zeno410Utils.Mutable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import windanesz.byg.BiomesYouGo;

import java.util.EnumMap;
import java.util.Map;

public final class GeographiCraftCompat {
    private static final Logger LOGGER = LogManager.getLogger(BiomesYouGo.MODID);
    private static final String MOD_ID = "geographicraft";

    private GeographiCraftCompat() {
    }

    public static void init() {
        if (Loader.isModLoaded(MOD_ID)) {
            BYGPackage.register();
        }
    }

    // Shares the config file name of GeographiCraft's built-in BYG package, whose biome names come from another BYG
    // port. The registry keeps the first package per file name, and init runs before GeographiCraft's postInit.
    static final class BYGPackage extends BiomePackage {
        BYGPackage() {
            super("biomesyougoInCC.cfg");
        }

        static void register() {
            BiomePackageRegistry.instance.register(new BYGPackage());
            LOGGER.info("Added {} biomes to GeographiCraft", new BYGSettings().elements().size());
        }

        @Override
        public BiomeSettings freshBiomeSetting() {
            return new BYGSettings();
        }
    }

    static final class BYGSettings extends BiomeSettings {
        private static final Map<BiomeManager.BiomeType, String> CLIMATES = new EnumMap<>(BiomeManager.BiomeType.class);

        static {
            CLIMATES.put(BiomeManager.BiomeType.ICY, Climate.SNOWY.name);
            CLIMATES.put(BiomeManager.BiomeType.COOL, Climate.COOL.name);
            CLIMATES.put(BiomeManager.BiomeType.WARM, Climate.WARM.name);
            CLIMATES.put(BiomeManager.BiomeType.DESERT, Climate.HOT.name);
        }

        private final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting("BiomesYouGoOn", "", true);
        private final Mutable<Boolean> biomesInNewWorlds = climateControlCategory.booleanSetting(startBiomesName("BYG"), "Use biome in new worlds and dimensions", true);

        BYGSettings() {
            super("BYGBiome");
            for (Map.Entry<BiomeManager.BiomeType, String> climate : CLIMATES.entrySet()) {
                for (BiomeManager.BiomeEntry entry : BiomeManager.getBiomes(climate.getKey())) {
                    ResourceLocation name = entry.biome.getRegistryName();
                    if (name == null || !BiomesYouGo.MODID.equals(name.getNamespace())) {
                        continue;
                    }
                    // BYG's default weight 4 becomes GeographiCraft's default incidence 5
                    int incidence = Math.round(entry.itemWeight * 1.25f);
                    if (hasBeach(entry.biome)) {
                        new Element(displayName(name), incidence, false, climate.getValue(), name.toString());
                    } else {
                        new Element(displayName(name), incidence, false, climate.getValue(), name.toString(), "none");
                    }
                }
            }
        }

        private static boolean hasBeach(Biome biome) {
            return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN)
                    && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)
                    && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA);
        }

        // "byg_allium_fields" -> "Allium Fields", the names GeographiCraft's own BYG config uses
        private static String displayName(ResourceLocation name) {
            String path = name.getPath();
            if (path.startsWith("byg_")) {
                path = path.substring(4);
            }
            StringBuilder builder = new StringBuilder();
            for (String word : path.split("_")) {
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
            return builder.toString();
        }

        @Override
        public void onNewWorld() {
            biomesFromConfig.set(biomesInNewWorlds);
        }

        @Override
        public boolean biomesAreActive() {
            return biomesFromConfig.value();
        }
    }
}

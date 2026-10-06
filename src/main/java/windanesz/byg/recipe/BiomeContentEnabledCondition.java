package windanesz.byg.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import windanesz.byg.Config;

import java.util.function.BooleanSupplier;

/**
 * Catchall condition factory for checking if a biome content set is enabled in the config.
 * This is used in the recipe JSON files to conditionally include (or exclude) recipes based on the config.
 */
@SuppressWarnings("unused")
public class BiomeContentEnabledCondition implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String set = JsonUtils.getString(json, "set");
        switch (set) {
            case "mangrove": return Config::isMangroveContentEnabled;
            case "baobab": return Config::isBaobabContentEnabled;
            case "great_oak": return Config::isGreatOakContentEnabled;
            case "ebony": return Config::isEbonyContentEnabled;
            case "cypress": return Config::isCypressContentEnabled;
            case "cika": return Config::isCikaContentEnabled;
            case "redwood": return Config::isRedwoodContentEnabled;
            case "skyris": return Config::isSkyrisContentEnabled;
            case "enchanted_tree": return Config::isEnchantedTreeContentEnabled;
            case "crystal": return Config::isCrystalContentEnabled;
            case "palm": return Config::isPalmContentEnabled;
            case "cherry":
            case "fir":
            case "maple":
            case "pine":
            case "zelkova":
            case "mahogany":
            case "jacaranda":
            case "cacti":
            case "mushroom_decor":
            case "ground_cover":
            case "food":
            case "fungal_zombie":
            case "fungal_skeleton":
            case "kiwi_bird":
            case "crystal_crawler":
            case "structures":
            case "black_sand":
            case "colored_sand":
            case "palo_verde":
            case "orchard":
            case "stellata":
            case "birch_variants":
            case "spruce_variants":
            case "sepinite":
            case "soapstone":
            case "sodalite":
            case "scoria":
            case "glowshroom":
            case "strawberry":
            case "blueberry":
            case "rudo":
            case "cattail":
            case "salal":
            case "nether_furnace":
            case "flowers":
            case "aspen":
            case "willow":
            case "ironwood":
            case "rainbow_eucalyptus":
            case "glowcane":
            case "oak_variants":
            case "rowan":
            case "hawthorn":
            case "witch_hazel":
            case "holly":
            case "frozen_oak": return () -> Config.isWoodSetEnabled(set);
            default: throw new IllegalArgumentException("Unknown biome content set: " + set);
        }
    }
}

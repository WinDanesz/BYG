package windanesz.byg.worldgen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.minecraft.util.ResourceLocation;

class BiomeIdMatchTest {

    @Test
    void bareNameMeansVanillaBiome() {
        assertTrue(BygWorldGenerator.biomeIdMatches(new ResourceLocation("minecraft:desert"), "desert"));
        assertTrue(BygWorldGenerator.biomeIdMatches(new ResourceLocation("minecraft:extreme_hills"), "extreme_hills"));
        assertTrue(BygWorldGenerator.biomeIdMatches(new ResourceLocation("minecraft:swampland"), "swampland"));
    }

    @Test
    void fullIdsStillMatch() {
        assertTrue(BygWorldGenerator.biomeIdMatches(new ResourceLocation("byg:byg_alps"), "byg:byg_alps"));
        assertTrue(BygWorldGenerator.biomeIdMatches(new ResourceLocation("minecraft:desert"), "minecraft:desert"));
    }

    @Test
    void differentBiomesDoNotMatch() {
        assertFalse(BygWorldGenerator.biomeIdMatches(new ResourceLocation("minecraft:desert_hills"), "desert"));
        // a bare name never matches a BYG biome of the same path
        assertFalse(BygWorldGenerator.biomeIdMatches(new ResourceLocation("byg:desert"), "desert"));
        assertFalse(BygWorldGenerator.biomeIdMatches(new ResourceLocation("byg:byg_alps"), "byg_alps"));
    }
}

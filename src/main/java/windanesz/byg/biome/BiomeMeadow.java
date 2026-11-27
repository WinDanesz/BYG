package windanesz.byg.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeMeadow
        extends Biome {

    public BiomeMeadow() {
        super(new Biome.BiomeProperties("Meadow").setRainfall(0.7f).setBaseHeight(0.25f).setWaterColor(-12618012).setHeightVariation(0.12f).setTemperature(0.8f));
        this.setRegistryName("byg_meadow");
        this.topBlock = windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState();
        this.fillerBlock = windanesz.byg.registry.ModBlocks.meadow_dirt.getDefaultState();
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 0;
        // Meadow trees are templates placed by TemplateWorldgenRegistry.
        this.decorator.extraTreeChance = 0.0F;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 5;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 25;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 25;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -13404617;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -13404617;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }
}

package windanesz.byg.biome;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public class BiomeBayou
        extends Biome {

    public BiomeBayou() {
        super(new Biome.BiomeProperties("Bayou").setRainfall(0.7f).setBaseHeight(-0.35f).setWaterColor(-10388636).setHeightVariation(0.0f).setTemperature(0.8f));
        this.setRegistryName("byg_bayou");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 0;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 15;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 35;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -11766212;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -11766212;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer primer, int x, int z, double noiseVal) {
        super.genTerrainBlocks(world, random, primer, x, z, noiseVal);

        int primerX = z & 15;
        int primerZ = x & 15;
        int waterY = world.getSeaLevel() - 1;
        if (primer.getBlockState(primerX, waterY, primerZ).getMaterial() != Material.WATER) {
            return;
        }

        int floorY = waterY;
        while (floorY > 0 && primer.getBlockState(primerX, floorY, primerZ).getMaterial() == Material.WATER) {
            floorY--;
        }
        int mudY = Math.max(floorY, waterY - 4);
        for (int y = floorY + 1; y < mudY; y++) {
            primer.setBlockState(primerX, y, primerZ, Blocks.DIRT.getDefaultState());
        }
        primer.setBlockState(primerX, mudY, primerZ,
                ModBlocks.mud_block != null ? ModBlocks.mud_block.getDefaultState() : Blocks.DIRT.getDefaultState());
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return super.getRandomTreeFeature(rand);
    }

    @Override
    public void decorate(World world, Random random, BlockPos chunkPos) {
        super.decorate(world, random, chunkPos);
        BayouVegetation.decorate(world, random, chunkPos, this);
    }

}



package windanesz.byg.worldgen;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;

/**
 * Drop-in replacement for {@link WorldGenMinable} that places exactly the same blocks (same random number
 * sequence, same float/double math, same replace checks) but reads block states straight from a cached chunk
 * instead of going through {@code World.getBlockState} for every cell of every vein. BYG's veins are large
 * and frequent, so that lookup dominated underground deposit generation.
 */
public class FastMinable extends WorldGenMinable {

    private static final int WORLD_LIMIT = 30000000;

    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate<IBlockState> predicate;

    public FastMinable(IBlockState state, int blockCount, Predicate<IBlockState> predicate) {
        super(state, blockCount, predicate);
        this.oreBlock = state;
        this.numberOfBlocks = blockCount;
        this.predicate = predicate;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
        double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
        double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
        double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
        double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
        double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int chunkX = Integer.MIN_VALUE;
        int chunkZ = Integer.MIN_VALUE;
        Chunk chunk = null;

        for (int i = 0; i < this.numberOfBlocks; ++i) {
            float f1 = (float) i / (float) this.numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) this.numberOfBlocks / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int x = j; x <= i1; ++x) {
                double d12 = ((double) x + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D) {
                    for (int y = k; y <= j1; ++y) {
                        double d13 = ((double) y + 0.5D - d7) / (d11 / 2.0D);
                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int z = l; z <= k1; ++z) {
                                double d14 = ((double) z + 0.5D - d8) / (d10 / 2.0D);
                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                    // World.getBlockState returns air outside the world, and setBlockState there is a
                                    // no-op, so skipping those cells is equivalent.
                                    if (y < 0 || y >= 256 || x < -WORLD_LIMIT || x >= WORLD_LIMIT
                                            || z < -WORLD_LIMIT || z >= WORLD_LIMIT) {
                                        continue;
                                    }
                                    int cx = x >> 4;
                                    int cz = z >> 4;
                                    if (chunk == null || cx != chunkX || cz != chunkZ) {
                                        chunk = world.getChunk(cx, cz);
                                        chunkX = cx;
                                        chunkZ = cz;
                                    }
                                    IBlockState state = chunk.getBlockState(x, y, z);
                                    pos.setPos(x, y, z);
                                    if (state.getBlock().isReplaceableOreGen(state, world, pos, this.predicate)) {
                                        world.setBlockState(new BlockPos(x, y, z), this.oreBlock, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}

package windanesz.byg.items;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Helpers for the Biome Teleporter's "find a new area" search: flood-filling a connected patch of biome cells, and
 * the short list of areas that were already visited.
 */
final class VisitedBiomeAreas {
    /** How many areas a teleporter remembers. Older ones become valid destinations again. */
    static final int MAX_AREAS = 5;

    private VisitedBiomeAreas() {
    }

    /** Tells whether a biome-generation cell belongs to the biome being searched for. */
    interface CellTest {
        boolean matches(int cellX, int cellZ);
    }

    static long pack(int cellX, int cellZ) {
        return ((long) cellX << 32) | (cellZ & 0xFFFFFFFFL);
    }

    /**
     * Adds the patch of matching cells connected to the seed (diagonals included, as thin biomes like rivers often
     * only touch at corners) to {@code area}, a slice at a time. Adds nothing if the seed does not match or is
     * already in the set. Stops after adding {@code maxCells}, so a huge biome only gets its part around the seed
     * marked.
     */
    static final class Fill {
        private final CellTest test;
        private final Set<Long> area;
        private final int maxCells;
        private final ArrayDeque<Long> open = new ArrayDeque<>();
        private int added;

        Fill(int seedX, int seedZ, CellTest test, Set<Long> area, int maxCells) {
            this.test = test;
            this.area = area;
            this.maxCells = maxCells;
            long seed = pack(seedX, seedZ);
            if (!area.contains(seed) && test.matches(seedX, seedZ)) {
                area.add(seed);
                open.add(seed);
                added = 1;
            }
        }

        /** Carries on from where it stopped. Returns true once the area is complete, false if it ran out of time. */
        boolean run(BooleanSupplier outOfTime) {
            while (!open.isEmpty() && added < maxCells) {
                long cell = open.poll();
                int x = (int) (cell >> 32);
                int z = (int) cell;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        long next = pack(x + dx, z + dz);
                        if (added < maxCells && !area.contains(next) && test.matches(x + dx, z + dz)) {
                            area.add(next);
                            open.add(next);
                            added++;
                        }
                    }
                }
                if (outOfTime.getAsBoolean()) {
                    break;
                }
            }
            return open.isEmpty() || added >= maxCells;
        }
    }

    /** Returns the x, z pairs with this position added last, dropping the oldest pairs beyond {@link #MAX_AREAS}. */
    static int[] remember(int[] visited, int x, int z) {
        int[] all = Arrays.copyOf(visited, visited.length + 2);
        all[visited.length] = x;
        all[visited.length + 1] = z;
        return all.length <= MAX_AREAS * 2 ? all : Arrays.copyOfRange(all, all.length - MAX_AREAS * 2, all.length);
    }
}

package windanesz.byg.items;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class VisitedBiomeAreasTest {
    private static final int NO_LIMIT = Integer.MAX_VALUE;

    /** Two 3 x 3 squares with a one-cell gap between them. */
    private static boolean twoPatches(int x, int z) {
        return z >= 0 && z < 3 && ((x >= 0 && x < 3) || (x >= 4 && x < 7));
    }

    @Test
    void fillStaysInsideTheConnectedPatch() {
        Set<Long> area = new HashSet<>();
        VisitedBiomeAreas.fillArea(1, 1, VisitedBiomeAreasTest::twoPatches, area, NO_LIMIT);
        assertEquals(9, area.size());
        assertTrue(area.contains(VisitedBiomeAreas.pack(2, 2)));
        assertFalse(area.contains(VisitedBiomeAreas.pack(4, 1)));
    }

    @Test
    void fillIgnoresSeedOutsideTheBiome() {
        Set<Long> area = new HashSet<>();
        VisitedBiomeAreas.fillArea(3, 1, VisitedBiomeAreasTest::twoPatches, area, NO_LIMIT);
        assertTrue(area.isEmpty());
    }

    @Test
    void fillFollowsDiagonalsAndNegativeCoordinates() {
        Set<Long> area = new HashSet<>();
        VisitedBiomeAreas.fillArea(-5, -5, (x, z) -> x == z && x >= -5 && x <= 5, area, NO_LIMIT);
        assertEquals(11, area.size());
        assertTrue(area.contains(VisitedBiomeAreas.pack(5, 5)));
    }

    @Test
    void fillStopsAtTheCellLimit() {
        Set<Long> area = new HashSet<>();
        VisitedBiomeAreas.fillArea(0, 0, (x, z) -> true, area, 100);
        assertEquals(100, area.size());
    }

    @Test
    void fillSkipsAreasAlreadyMarked() {
        Set<Long> area = new HashSet<>();
        VisitedBiomeAreas.fillArea(1, 1, VisitedBiomeAreasTest::twoPatches, area, NO_LIMIT);
        VisitedBiomeAreas.fillArea(0, 0, (x, z) -> {
            throw new AssertionError("already marked cell was tested");
        }, area, NO_LIMIT);
        assertEquals(9, area.size());
    }

    @Test
    void packKeepsCellsWithNegativeCoordinatesDistinct() {
        assertFalse(VisitedBiomeAreas.pack(-1, 0) == VisitedBiomeAreas.pack(0, -1));
        assertFalse(VisitedBiomeAreas.pack(0, -1) == VisitedBiomeAreas.pack(-1, -1));
    }

    @Test
    void rememberKeepsOnlyTheNewestAreas() {
        int[] visited = new int[0];
        for (int i = 1; i <= VisitedBiomeAreas.MAX_AREAS + 2; i++) {
            visited = VisitedBiomeAreas.remember(visited, i, -i);
        }
        assertArrayEquals(new int[] {3, -3, 4, -4, 5, -5, 6, -6, 7, -7}, visited);
    }
}

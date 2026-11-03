package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.LevelLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLevelSourceTest {

    @Test
    void producesRequestedSizeAndObstacleCount() {
        LevelLayout layout = new RandomLevelSource(10, 8, 5, 0, new Random(42)).load();

        assertEquals(10, layout.getWidth());
        assertEquals(8, layout.getHeight());
        assertEquals(5, layout.getObstacles().size());
    }

    @Test
    void everythingIsInsideTheLevel() {
        LevelLayout layout = new RandomLevelSource(10, 8, 20, 0, new Random(1)).load();

        assertTrue(isInside(layout, layout.getPlayerSpawn()));
        for (GridPoint2 obstacle : layout.getObstacles()) {
            assertTrue(isInside(layout, obstacle), "outside: " + obstacle);
        }
    }

    @Test
    void cellsDoNotOverlap() {
        LevelLayout layout = new RandomLevelSource(5, 5, 20, 0, new Random(7)).load();

        Set<GridPoint2> occupied = new HashSet<>(layout.getObstacles());
        assertEquals(20, occupied.size());
        assertFalse(occupied.contains(layout.getPlayerSpawn()));
    }

    @Test
    void sameSeedGivesSameLevel() {
        List<GridPoint2> first = new RandomLevelSource(6, 6, 4, 0, new Random(3)).load().getObstacles();
        List<GridPoint2> second = new RandomLevelSource(6, 6, 4, 0, new Random(3)).load().getObstacles();

        assertEquals(first, second);
    }

    @Test
    void rejectsImpossibleParameters() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(0, 5, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(2, 2, 4, 0));
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(2, 2, -1, 0));
    }

    @Test
    void generatesRequestedNumberOfTanks() {
        LevelLayout layout = new RandomLevelSource(10, 8, 5, 3, new Random(42)).load();

        assertEquals(3, layout.getTankSpawns().size());
    }

    @Test
    void tanksDoNotOverlapWithObstaclesOrPlayer() {
        LevelLayout layout = new RandomLevelSource(5, 5, 10, 5, new Random(11)).load();

        Set<GridPoint2> occupied = new HashSet<>();
        occupied.add(layout.getPlayerSpawn());
        occupied.addAll(layout.getObstacles());

        for (GridPoint2 spawn : layout.getTankSpawns()) {
            assertTrue(occupied.add(spawn), "overlaps: " + spawn);
        }
    }

    private boolean isInside(LevelLayout layout, GridPoint2 point) {
        return point.x >= 0 && point.x < layout.getWidth() && point.y >= 0 && point.y < layout.getHeight();
    }
}
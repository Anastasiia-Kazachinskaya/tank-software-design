package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(10, 8, List.of(new Tree(new GridPoint2(1, 3))));
    }

    @Test
    void emptyCellInsideLevelIsFree() {
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertTrue(level.isFree(new GridPoint2(9, 7)));
        assertTrue(level.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void cellWithObstacleIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void cellsOutsideLevelAreNotFree() {
        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(10, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 8)));
    }

    @Test
    void isInsideChecksOnlyBounds() {
        assertTrue(level.isInside(new GridPoint2(1, 3)));
        assertFalse(level.isInside(new GridPoint2(10, 8)));
    }

    @Test
    void levelWithoutObstaclesIsFreeEverywhereInside() {
        Level empty = new Level(2, 2, List.of());

        assertTrue(empty.isFree(new GridPoint2(0, 0)));
        assertTrue(empty.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void rejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new Level(0, 5, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Level(5, -1, List.of()));
    }

    @Test
    void exposesSize() {
        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
    }
}

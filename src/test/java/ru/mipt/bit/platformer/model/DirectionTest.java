package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void applyShiftsCoordinatesByOneCell() {
        GridPoint2 origin = new GridPoint2(5, 5);

        assertEquals(new GridPoint2(5, 6), Direction.UP.apply(origin));
        assertEquals(new GridPoint2(5, 4), Direction.DOWN.apply(origin));
        assertEquals(new GridPoint2(4, 5), Direction.LEFT.apply(origin));
        assertEquals(new GridPoint2(6, 5), Direction.RIGHT.apply(origin));
    }

    @Test
    void applyDoesNotMutateArgument() {
        GridPoint2 origin = new GridPoint2(2, 3);

        GridPoint2 result = Direction.UP.apply(origin);

        assertEquals(new GridPoint2(2, 3), origin);
        assertNotSame(origin, result);
    }

    @Test
    void vectorsAreUnitVectors() {
        for (Direction direction : Direction.values()) {
            GridPoint2 v = direction.getVector();
            assertEquals(1, Math.abs(v.x) + Math.abs(v.y), direction.name());
        }
    }

    @Test
    void oppositeDirectionsCancelEachOther() {
        GridPoint2 origin = new GridPoint2(0, 0);

        assertEquals(origin, Direction.DOWN.apply(Direction.UP.apply(origin)));
        assertEquals(origin, Direction.RIGHT.apply(Direction.LEFT.apply(origin)));
    }

    @Test
    void getVectorReturnsDefensiveCopy() {
        GridPoint2 vector = Direction.RIGHT.getVector();
        vector.set(100, 100);

        assertEquals(new GridPoint2(1, 0), Direction.RIGHT.getVector());
    }

    @Test
    void rotationsMatchSpriteOrientation() {
        assertEquals(0f, Direction.RIGHT.getRotation());
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
    }
}

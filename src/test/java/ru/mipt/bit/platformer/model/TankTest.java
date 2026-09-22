package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    private static final float DURATION = Tank.DEFAULT_MOVEMENT_DURATION_SECONDS;
    private static final float EPS = 1e-5f;

    private Level level;
    private Tank tank;

    @BeforeEach
    void setUp() {
        // танк в левом нижнем углу, прямо над ним дерево
        level = new Level(10, 8, List.of(new Tree(new GridPoint2(0, 1))));
        tank = new Tank(new GridPoint2(0, 0));
    }

    @Test
    void initiallyStandsStillAndLooksRight() {
        assertFalse(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 0), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), EPS);
    }

    @Test
    void moveToFreeCellStartsMovement() {
        tank.move(Direction.RIGHT, level);

        assertTrue(tank.isMoving());
        assertEquals(0f, tank.getMovementProgress(), EPS);
        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());
    }

    @Test
    void coordinatesChangeOnlyWhenMovementFinishes() {
        tank.move(Direction.RIGHT, level);

        tank.update(DURATION / 2);
        assertTrue(tank.isMoving());
        assertEquals(0.5f, tank.getMovementProgress(), EPS);
        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());

        tank.update(DURATION / 2);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 0), tank.getCoordinates());
    }

    @Test
    void progressIsClampedToOne() {
        tank.move(Direction.RIGHT, level);

        tank.update(DURATION * 10);

        assertEquals(1f, tank.getMovementProgress(), EPS);
        assertEquals(new GridPoint2(1, 0), tank.getCoordinates());
    }

    @Test
    void doesNotMoveIntoObstacleButTurns() {
        tank.move(Direction.UP, level);

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(0, 0), tank.getDestinationCoordinates());
    }

    @Test
    void doesNotLeaveLevelButTurns() {
        tank.move(Direction.LEFT, level);
        assertFalse(tank.isMoving());
        assertEquals(Direction.LEFT, tank.getDirection());

        tank.move(Direction.DOWN, level);
        assertFalse(tank.isMoving());
        assertEquals(Direction.DOWN, tank.getDirection());
    }

    @Test
    void newMoveIsIgnoredWhileMoving() {
        tank.move(Direction.RIGHT, level);
        tank.update(DURATION / 2);

        tank.move(Direction.UP, level);

        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertEquals(0.5f, tank.getMovementProgress(), EPS);
    }

    @Test
    void canMoveAgainAfterFinishing() {
        tank.move(Direction.RIGHT, level);
        tank.update(DURATION);

        tank.move(Direction.RIGHT, level);
        tank.update(DURATION);

        assertEquals(new GridPoint2(2, 0), tank.getCoordinates());
    }

    @Test
    void updateWithoutMovementDoesNothing() {
        tank.update(1f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());
    }

    @Test
    void externalChangesDoNotAffectTank() {
        GridPoint2 initial = new GridPoint2(3, 3);
        Tank other = new Tank(initial);

        initial.set(9, 9);
        other.getCoordinates().set(9, 9);
        other.getDestinationCoordinates().set(9, 9);

        assertEquals(new GridPoint2(3, 3), other.getCoordinates());
        assertEquals(new GridPoint2(3, 3), other.getDestinationCoordinates());
    }

    @Test
    void rejectsNonPositiveDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(0, 0), 0f));
    }
}

package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    private static final float FLIGHT = Bullet.DEFAULT_FLIGHT_DURATION_SECONDS;

    private Bullet bullet;

    @BeforeEach
    void setUp() {
        bullet = new Bullet(new GridPoint2(2, 2), Direction.RIGHT);
    }

    @Test
    void startsAliveAndAimedForward() {
        assertFalse(bullet.isDestroyed());
        assertEquals(new GridPoint2(2, 2), bullet.getCoordinates());
        assertEquals(new GridPoint2(3, 2), bullet.getDestinationCoordinates());
        assertEquals(Direction.RIGHT, bullet.getDirection());
    }

    @Test
    void reachesNextCellAfterFlightDuration() {
        bullet.update(FLIGHT);

        assertEquals(new GridPoint2(3, 2), bullet.getCoordinates());
    }

    @Test
    void keepsFlyingCellAfterCell() {
        bullet.update(FLIGHT);
        bullet.update(FLIGHT);

        assertEquals(new GridPoint2(4, 2), bullet.getCoordinates());
        assertEquals(new GridPoint2(5, 2), bullet.getDestinationCoordinates());
    }

    @Test
    void staysBetweenCellsWhileFlying() {
        bullet.update(FLIGHT / 2);

        assertEquals(new GridPoint2(2, 2), bullet.getCoordinates());
        assertEquals(0.5f, bullet.getMovementProgress(), 1e-5f);
    }

    @Test
    void occupiesOnlyItsOwnCellAndDoesNotBlockMovement() {
        assertTrue(bullet.occupies(new GridPoint2(2, 2)));
        assertFalse(bullet.occupies(new GridPoint2(3, 2)));
        assertFalse(bullet.blocksMovement());
    }

    @Test
    void anyDamageDestroysBullet() {
        bullet.takeDamage(1);

        assertTrue(bullet.isDestroyed());
    }

    @Test
    void rejectsInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bullet(new GridPoint2(0, 0), Direction.UP, 0f, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new Bullet(new GridPoint2(0, 0), Direction.UP, FLIGHT, -1));
    }
}
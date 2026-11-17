package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Пуля: летит по прямой клетка за клеткой, пока во что-нибудь не попадёт.
 * Столкновения разрешает уровень — пуля только помечается уничтоженной.
 */
public class Bullet implements GameObject, Updatable, Destructible {
    public static final float DEFAULT_FLIGHT_DURATION_SECONDS = 0.1f;
    public static final int DEFAULT_DAMAGE = 25;
    private static final float FLIGHT_FINISHED = 1f;

    private final Direction direction;
    private final int damage;
    private final float flightDurationSeconds;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float flightProgress;
    private boolean destroyed;

    public Bullet(GridPoint2 initialCoordinates, Direction direction) {
        this(initialCoordinates, direction, DEFAULT_FLIGHT_DURATION_SECONDS, DEFAULT_DAMAGE);
    }

    public Bullet(GridPoint2 initialCoordinates, Direction direction, float flightDurationSeconds, int damage) {
        if (flightDurationSeconds <= 0f) {
            throw new IllegalArgumentException("Flight duration must be positive");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative");
        }
        this.direction = direction;
        this.damage = damage;
        this.flightDurationSeconds = flightDurationSeconds;
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = direction.apply(initialCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return flightProgress;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    /** Летит без остановки: долетев до клетки, сразу нацеливается на следующую. */
    @Override
    public void update(float deltaTime) {
        flightProgress = clamp(flightProgress + deltaTime / flightDurationSeconds, 0f, FLIGHT_FINISHED);

        if (isEqual(flightProgress, FLIGHT_FINISHED)) {
            coordinates.set(destinationCoordinates);
            destinationCoordinates.set(direction.apply(coordinates));
            flightProgress = 0f;
        }
    }
    @Override
    public void takeDamage(int damage) {
        destroy();
    }
}
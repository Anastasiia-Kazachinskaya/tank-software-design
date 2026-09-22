package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Модель танка: клетка, клетка назначения, прогресс перемещения и направление.
 * Ничего не знает о текстурах и отрисовке.
 */
public class Tank implements GameObject {
    public static final float DEFAULT_MOVEMENT_DURATION_SECONDS = 0.4f;
    private static final float MOVEMENT_FINISHED = 1f;

    private final float movementDurationSeconds;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress = MOVEMENT_FINISHED;
    private Direction direction = Direction.RIGHT;

    public Tank(GridPoint2 initialCoordinates) {
        this(initialCoordinates, DEFAULT_MOVEMENT_DURATION_SECONDS);
    }

    public Tank(GridPoint2 initialCoordinates, float movementDurationSeconds) {
        if (movementDurationSeconds <= 0f) {
            throw new IllegalArgumentException("Movement duration must be positive");
        }
        this.movementDurationSeconds = movementDurationSeconds;
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, MOVEMENT_FINISHED);
    }

    /**
     * Поворачивает танк и, если он стоит на месте и клетка свободна,
     * начинает движение в соседнюю клетку.
     */
    public void move(Direction direction, Level level) {
        this.direction = direction;

        if (isMoving()) {
            return;
        }

        GridPoint2 destination = direction.apply(coordinates);
        if (level.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        movementProgress = clamp(movementProgress + deltaTime / movementDurationSeconds, 0f, MOVEMENT_FINISHED);

        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }
}

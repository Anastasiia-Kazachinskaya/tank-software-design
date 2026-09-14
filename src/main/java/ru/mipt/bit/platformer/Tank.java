package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject {
    private static final float MOVEMENT_DURATION_SECONDS = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private float movementProgress;
    private Direction direction;

    public Tank(GridPoint2 initialCoordinates) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementProgress = 1f;
        this.direction = Direction.RIGHT;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

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
        movementProgress = continueProgress(
                movementProgress,
                deltaTime,
                MOVEMENT_DURATION_SECONDS
        );

        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }
}
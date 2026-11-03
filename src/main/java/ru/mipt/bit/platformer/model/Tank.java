package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Модель танка: клетка, клетка назначения, прогресс перемещения, направление,
 * здоровье и перезарядка. Ничего не знает о текстурах и отрисовке.
 */
public class Tank implements GameObject, Movable, Updatable, Destructible, Shootable {
    public static final float DEFAULT_MOVEMENT_DURATION_SECONDS = 0.4f;
    public static final float DEFAULT_RELOAD_SECONDS = 1f;
    public static final int DEFAULT_HEALTH = 3;
    private static final float MOVEMENT_FINISHED = 1f;

    private final Obstacles obstacles;
    private final ObjectSpawner spawner;
    private final float movementDurationSeconds;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;

    private int health;
    private float movementProgress = MOVEMENT_FINISHED;
    private float secondsSinceLastShot = DEFAULT_RELOAD_SECONDS;   // стрелять можно сразу
    private Direction direction = Direction.RIGHT;

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE, DEFAULT_MOVEMENT_DURATION_SECONDS, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, float movementDurationSeconds) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE, movementDurationSeconds, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, float movementDurationSeconds, int health) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE, movementDurationSeconds, health);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, ObjectSpawner spawner,
                float movementDurationSeconds, int health) {
        if (movementDurationSeconds <= 0f) {
            throw new IllegalArgumentException("Movement duration must be positive");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        this.obstacles = obstacles;
        this.spawner = spawner;
        this.movementDurationSeconds = movementDurationSeconds;
        this.health = health;
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

    public int getHealth() {
        return health;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, MOVEMENT_FINISHED);
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates) || destinationCoordinates.equals(coordinates);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }

    @Override
    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative");
        }
        health -= damage;
    }

    public boolean canShoot() {
        return !isDestroyed() && secondsSinceLastShot >= DEFAULT_RELOAD_SECONDS;
    }

    /**
     * Поворачивает танк и, если он стоит на месте и клетка свободна,
     * начинает движение в соседнюю клетку.
     */
    @Override
    public void move(Direction direction) {
        this.direction = direction;

        if (isMoving()) {
            return;
        }

        GridPoint2 destination = direction.apply(coordinates);
        if (obstacles.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    /** Пуля рождается в соседней клетке по направлению танка и летит туда же. */
    @Override
    public void shoot() {
        if (!canShoot()) {
            return;
        }
        secondsSinceLastShot = 0f;
        spawner.spawn(new Bullet(direction.apply(coordinates), direction));
    }

    @Override
    public void update(float deltaTime) {
        secondsSinceLastShot += deltaTime;
        movementProgress = clamp(movementProgress + deltaTime / movementDurationSeconds, 0f, MOVEMENT_FINISHED);

        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }
}
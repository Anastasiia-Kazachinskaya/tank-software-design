package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Модель танка: клетка, клетка назначения, прогресс перемещения, направление,
 * здоровье и перезарядка. Скорость и возможность стрелять зависят от текущего
 * состояния, которое определяется по доле оставшихся жизней.
 */
public class Tank implements GameObject, Movable, Updatable, Destructible, Shootable, HealthSource {
    public static final float DEFAULT_MOVEMENT_DURATION_SECONDS = 0.4f;
    public static final float DEFAULT_RELOAD_SECONDS = 1f;
    public static final int DEFAULT_HEALTH = 100;
    private static final float MOVEMENT_FINISHED = 1f;

    private final Obstacles obstacles;
    private final ObjectSpawner spawner;
    private final TankStateSelector stateSelector;
    private final float movementDurationSeconds;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final int maxHealth;

    private int health;
    private float movementProgress = MOVEMENT_FINISHED;
    private float secondsSinceLastShot = DEFAULT_RELOAD_SECONDS;
    private Direction direction = Direction.RIGHT;

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE,
                DEFAULT_MOVEMENT_DURATION_SECONDS, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, float movementDurationSeconds) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE,
                movementDurationSeconds, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles,
                float movementDurationSeconds, int health) {
        this(initialCoordinates, obstacles, ObjectSpawner.NONE, movementDurationSeconds, health);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, ObjectSpawner spawner,
                float movementDurationSeconds, int health) {
        this(initialCoordinates, obstacles, spawner, new HealthTankStateSelector(),
                movementDurationSeconds, health);
    }

    public Tank(GridPoint2 initialCoordinates, Obstacles obstacles, ObjectSpawner spawner,
                TankStateSelector stateSelector, float movementDurationSeconds, int health) {
        if (movementDurationSeconds <= 0f) {
            throw new IllegalArgumentException("Movement duration must be positive");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        this.obstacles = obstacles;
        this.spawner = spawner;
        this.stateSelector = stateSelector;
        this.movementDurationSeconds = movementDurationSeconds;
        this.health = health;
        this.maxHealth = health;
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

    public int getMaxHealth() {
        return maxHealth;
    }

    /** Доля оставшегося здоровья от 0 до 1 — для полоски жизней и выбора состояния. */
    @Override
    public float getHealthFraction() {
        return Math.max(0f, (float) health / maxHealth);
    }

    /** Текущее состояние вычисляется каждый раз, поэтому всегда соответствует здоровью. */
    public TankState getState() {
        return stateSelector.selectState(getHealthFraction());
    }

    /** Длительность одного шага с учётом состояния. */
    public float getMovementDuration() {
        return movementDurationSeconds * getState().getMovementDurationMultiplier();
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
        TankState state = getState();
        return !isDestroyed()
                && state.canShoot()
                && secondsSinceLastShot >= DEFAULT_RELOAD_SECONDS * state.getReloadMultiplier();
    }

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
        movementProgress = clamp(movementProgress + deltaTime / getMovementDuration(), 0f, MOVEMENT_FINISHED);

        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }
}
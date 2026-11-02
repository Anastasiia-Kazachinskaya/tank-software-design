package ru.mipt.bit.platformer.model;

import java.util.Random;

/** Простейший AI: раз в несколько секунд выбирает случайное направление. */
public class RandomCommandSource implements CommandSource {
    public static final float DEFAULT_DECISION_INTERVAL_SECONDS = 1.5f;
    public static final float DEFAULT_SHOOT_PROBABILITY = 0.3f;

    private final Movable movable;
    private final Random random;
    private final float decisionIntervalSeconds;
    private float secondsSinceLastDecision;
    private final Shootable shooter;
    private final float shootProbability;

    public RandomCommandSource(Movable movable, Shootable shooter) {
        this(movable, shooter, new Random(), DEFAULT_DECISION_INTERVAL_SECONDS, DEFAULT_SHOOT_PROBABILITY);
    }

    public RandomCommandSource(Movable movable, Shootable shooter, Random random,
                               float decisionIntervalSeconds, float shootProbability) {
        if (decisionIntervalSeconds <= 0f) {
            throw new IllegalArgumentException("Decision interval must be positive");
        }
        if (shootProbability < 0f || shootProbability > 1f) {
            throw new IllegalArgumentException("Shoot probability must be between 0 and 1");
        }
        this.movable = movable;
        this.shooter = shooter;
        this.random = random;
        this.decisionIntervalSeconds = decisionIntervalSeconds;
        this.shootProbability = shootProbability;
        this.secondsSinceLastDecision = decisionIntervalSeconds;
    }

    public void update(float deltaTime) {
        secondsSinceLastDecision += deltaTime;
    }

    @Override
    public Command nextCommand(float deltaTime) {
        secondsSinceLastDecision += deltaTime;
        if (secondsSinceLastDecision < decisionIntervalSeconds) {
            return null;
        }
        secondsSinceLastDecision = 0f;

        if (random.nextFloat() < shootProbability) {
            return new ShootCommand(shooter);
        }
        Direction[] directions = Direction.values();
        return new MoveCommand(movable, directions[random.nextInt(directions.length)]);
    }
}
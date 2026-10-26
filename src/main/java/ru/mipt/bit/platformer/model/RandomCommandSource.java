package ru.mipt.bit.platformer.model;

import java.util.Random;

/** Простейший AI: раз в несколько секунд выбирает случайное направление. */
public class RandomCommandSource implements CommandSource {
    public static final float DEFAULT_DECISION_INTERVAL_SECONDS = 1.5f;

    private final Movable movable;
    private final Random random;
    private final float decisionIntervalSeconds;
    private float secondsSinceLastDecision;

    public RandomCommandSource(Movable movable) {
        this(movable, new Random(), DEFAULT_DECISION_INTERVAL_SECONDS);
    }

    public RandomCommandSource(Movable movable, Random random, float decisionIntervalSeconds) {
        if (decisionIntervalSeconds <= 0f) {
            throw new IllegalArgumentException("Decision interval must be positive");
        }
        this.movable = movable;
        this.random = random;
        this.decisionIntervalSeconds = decisionIntervalSeconds;
        this.secondsSinceLastDecision = decisionIntervalSeconds;   // первая команда сразу
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

        Direction[] directions = Direction.values();
        return new MoveCommand(movable, directions[random.nextInt(directions.length)]);
    }
}
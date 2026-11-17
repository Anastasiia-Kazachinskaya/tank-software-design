package ru.mipt.bit.platformer.model;

/**
 * Решает, в каком состоянии находится танк.
 * Вынесено отдельно от самих состояний: сейчас выбор идёт по здоровью,
 * а бустер с поля будет другой реализацией, не зависящей от жизней.
 */
@FunctionalInterface
public interface TankStateSelector {
    TankState selectState(float healthFraction);
}
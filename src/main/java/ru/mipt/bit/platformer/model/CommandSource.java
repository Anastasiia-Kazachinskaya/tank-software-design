package ru.mipt.bit.platformer.model;

/** Решает, что танк будет делать дальше. */
@FunctionalInterface
public interface CommandSource {
    /** Следующая команда, или null если делать нечего. */
    Command nextCommand(float deltaTime);
}
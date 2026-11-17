package ru.mipt.bit.platformer.model;

/** Решает, что танк будет делать дальше. */

/** Port (driving) — через него внешний мир приводит игру в действие: клавиатура, AI, в будущем сеть. */
@FunctionalInterface
public interface CommandSource {
    /** Следующая команда, или null если делать нечего. */
    Command nextCommand(float deltaTime);
}
package ru.mipt.bit.platformer.model;

/** Принимает новые объекты, появившиеся во время игры. */
@FunctionalInterface
public interface ObjectSpawner {
    /** Заглушка для объектов, которым некуда спавнить (например, в тестах движения). */
    ObjectSpawner NONE = object -> { };

    void spawn(GameObject object);
}
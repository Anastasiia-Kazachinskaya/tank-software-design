package ru.mipt.bit.platformer.model;

/** Действие над объектом игры, завёрнутое в объект. */
@FunctionalInterface
public interface Command {
    void execute();
}
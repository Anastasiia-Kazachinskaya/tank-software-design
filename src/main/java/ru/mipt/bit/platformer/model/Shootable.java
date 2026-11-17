package ru.mipt.bit.platformer.model;

/** Объект, которому можно скомандовать "стреляй". */
@FunctionalInterface
public interface Shootable {
    void shoot();
}
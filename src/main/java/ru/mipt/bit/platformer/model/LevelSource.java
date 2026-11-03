package ru.mipt.bit.platformer.model;

@FunctionalInterface
public interface LevelSource {
    LevelLayout load();
}
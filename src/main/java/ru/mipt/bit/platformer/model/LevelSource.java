package ru.mipt.bit.platformer.model;

/** Port (driven) — откуда берётся расстановка уровня. Адаптеры: FileLevelSource, RandomLevelSource. */
@FunctionalInterface
public interface LevelSource {
    LevelLayout load();
}
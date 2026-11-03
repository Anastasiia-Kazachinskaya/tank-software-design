package ru.mipt.bit.platformer.model;

/**
 * Слушатель изменений уровня.
 * Уровень — наблюдаемый объект, графика — наблюдатель.
 */
public interface LevelListener {
    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
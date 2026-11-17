package ru.mipt.bit.platformer.model;

/**
 * Слушатель изменений уровня.
 * Уровень — наблюдаемый объект, графика — наблюдатель.
 */

/** Port (driven) — через него домен сообщает наружу об изменениях уровня. Адаптер: GameRenderer. */
public interface LevelListener {
    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
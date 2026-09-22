package ru.mipt.bit.platformer.input;

/**
 * Обработчик одной группы клавиш (движение, стрельба, пауза, ...).
 * Чтобы добавить новое действие, пишем новую реализацию и регистрируем её
 * в InputController — существующий код не меняется.
 */
@FunctionalInterface
public interface InputHandler {
    void handleInput(Keyboard keyboard);
}

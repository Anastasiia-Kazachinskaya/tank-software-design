package ru.mipt.bit.platformer.input;

/**
 * Абстракция над состоянием клавиатуры.
 * Нужна, чтобы обработчики ввода не зависели от Gdx.input и тестировались без окна.
 */
@FunctionalInterface
public interface Keyboard {
    boolean isKeyPressed(int keyCode);
}

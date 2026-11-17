package ru.mipt.bit.platformer.input;

/**
 * Абстракция над состоянием клавиатуры.
 * Нужна, чтобы обработчики ввода не зависели от Gdx.input и тестировались без окна.
 */

/** Port (driven) — абстракция устройства ввода. Адаптер: GdxKeyboard, в тестах FakeKeyboard. */
@FunctionalInterface
public interface Keyboard {
    boolean isKeyPressed(int keyCode);
}

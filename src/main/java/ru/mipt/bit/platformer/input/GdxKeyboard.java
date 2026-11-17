package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;

/** Реальная клавиатура через libGDX. */

/** Adapter (driven) — реализация порта Keyboard поверх Gdx.input. */
public class GdxKeyboard implements Keyboard {
    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}

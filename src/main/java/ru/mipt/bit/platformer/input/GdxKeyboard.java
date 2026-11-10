package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;

/** Реальная клавиатура через libGDX. */
public class GdxKeyboard implements Keyboard {
    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}

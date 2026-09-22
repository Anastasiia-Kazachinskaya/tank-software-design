package ru.mipt.bit.platformer.input;

import java.util.HashSet;
import java.util.Set;

/** Клавиатура для тестов: сами решаем, какие клавиши "зажаты". */
class FakeKeyboard implements Keyboard {
    private final Set<Integer> pressed = new HashSet<>();

    FakeKeyboard press(int... keyCodes) {
        for (int keyCode : keyCodes) {
            pressed.add(keyCode);
        }
        return this;
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return pressed.contains(keyCode);
    }
}

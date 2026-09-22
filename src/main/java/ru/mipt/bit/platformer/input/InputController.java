package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;

/** Каждый кадр опрашивает все зарегистрированные обработчики. */
public class InputController {
    private final Keyboard keyboard;
    private final List<InputHandler> handlers = new ArrayList<>();

    public InputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public InputController addHandler(InputHandler handler) {
        handlers.add(handler);
        return this;
    }

    public void handleInput() {
        for (InputHandler handler : handlers) {
            handler.handleInput(keyboard);
        }
    }
}

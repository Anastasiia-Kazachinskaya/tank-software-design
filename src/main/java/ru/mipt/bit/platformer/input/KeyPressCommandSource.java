package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Command;
import ru.mipt.bit.platformer.model.CommandSource;

public class KeyPressCommandSource implements CommandSource {
    private final int keyCode;
    private final Keyboard keyboard;
    private final Command command;

    private boolean wasPressed;

    public KeyPressCommandSource(int keyCode, Keyboard keyboard, Command command) {
        this.keyCode = keyCode;
        this.keyboard = keyboard;
        this.command = command;
    }

    @Override
    public Command nextCommand(float deltaTime) {
        boolean pressed = keyboard.isKeyPressed(keyCode);
        boolean justPressed = pressed && !wasPressed;
        wasPressed = pressed;

        return justPressed ? command : null;
    }
}
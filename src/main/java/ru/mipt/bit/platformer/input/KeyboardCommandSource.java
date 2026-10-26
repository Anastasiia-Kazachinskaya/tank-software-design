package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Command;
import ru.mipt.bit.platformer.model.CommandSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.Movable;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

/** Стратегия игрока: команда берётся с клавиатуры. */
public class KeyboardCommandSource implements CommandSource {
    private final Movable movable;
    private final Keyboard keyboard;
    private final Map<Integer, Direction> keyBindings;

    public KeyboardCommandSource(Movable movable, Keyboard keyboard) {
        this(movable, keyboard, defaultKeyBindings());
    }

    public KeyboardCommandSource(Movable movable, Keyboard keyboard, Map<Integer, Direction> keyBindings) {
        this.movable = movable;
        this.keyboard = keyboard;
        this.keyBindings = new LinkedHashMap<>(keyBindings);
    }

    public static Map<Integer, Direction> defaultKeyBindings() {
        Map<Integer, Direction> bindings = new LinkedHashMap<>();
        bindings.put(UP, Direction.UP);
        bindings.put(W, Direction.UP);
        bindings.put(LEFT, Direction.LEFT);
        bindings.put(A, Direction.LEFT);
        bindings.put(DOWN, Direction.DOWN);
        bindings.put(S, Direction.DOWN);
        bindings.put(RIGHT, Direction.RIGHT);
        bindings.put(D, Direction.RIGHT);
        return bindings;
    }

    @Override
    public Command nextCommand(float deltaTime) {
        for (Map.Entry<Integer, Direction> binding : keyBindings.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                return new MoveCommand(movable, binding.getValue());
            }
        }
        return null;
    }
}
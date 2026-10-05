package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class MovementInputHandler implements InputHandler {
    private final Movable movable;
    private final Map<Integer, Direction> keyBindings;

    public MovementInputHandler(Movable movable) {
        this(movable, defaultKeyBindings());
    }

    public MovementInputHandler(Movable movable, Map<Integer, Direction> keyBindings) {
        this.movable = movable;
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
    public void handleInput(Keyboard keyboard) {
        for (Map.Entry<Integer, Direction> binding : keyBindings.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                movable.move(binding.getValue());
                return;
            }
        }
    }
}

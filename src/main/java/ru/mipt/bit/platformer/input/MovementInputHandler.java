package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Переводит клавиши движения в команду tank.move(...).
 * Если зажато несколько клавиш, срабатывает первая по порядку привязок —
 * танк не может ехать в две стороны сразу.
 */
public class MovementInputHandler implements InputHandler {
    private final Tank tank;
    private final Level level;
    private final Map<Integer, Direction> keyBindings;

    public MovementInputHandler(Tank tank, Level level) {
        this(tank, level, defaultKeyBindings());
    }

    public MovementInputHandler(Tank tank, Level level, Map<Integer, Direction> keyBindings) {
        this.tank = tank;
        this.level = level;
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
                tank.move(binding.getValue(), level);
                return;
            }
        }
    }
}

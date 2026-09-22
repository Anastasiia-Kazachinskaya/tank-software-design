package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class InputController {
    private final Map<Integer, Direction> keyDirections = new LinkedHashMap<>();

    public InputController() {
        keyDirections.put(UP, Direction.UP);
        keyDirections.put(W, Direction.UP);

        keyDirections.put(LEFT, Direction.LEFT);
        keyDirections.put(A, Direction.LEFT);

        keyDirections.put(DOWN, Direction.DOWN);
        keyDirections.put(S, Direction.DOWN);

        keyDirections.put(RIGHT, Direction.RIGHT);
        keyDirections.put(D, Direction.RIGHT);
    }

    public Direction getDirection() {
        for (Map.Entry<Integer, Direction> entry : keyDirections.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
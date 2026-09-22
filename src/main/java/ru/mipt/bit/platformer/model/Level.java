package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/** Игровое поле: размеры и препятствия. Отвечает на вопрос "можно ли встать в клетку". */
public class Level {
    private final int width;
    private final int height;
    private final List<GameObject> obstacles;

    public Level(int width, int height, List<? extends GameObject> obstacles) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.obstacles = List.copyOf(obstacles);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    public boolean isFree(GridPoint2 coordinates) {
        if (!isInside(coordinates)) {
            return false;
        }
        for (GameObject obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return false;
            }
        }
        return true;
    }
}

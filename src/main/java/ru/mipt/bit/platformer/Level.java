package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class Level {
    private final int width;
    private final int height;
    private final List<GameObject> obstacles;

    public Level(int width, int height, List<GameObject> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
    }

    public boolean isFree(GridPoint2 coordinates) {
        if (coordinates.x < 0 || coordinates.x >= width) {
            return false;
        }

        if (coordinates.y < 0 || coordinates.y >= height) {
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
package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;
import java.util.ArrayList;


public class Level implements Obstacles {
    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();

    public Level(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
    }

    public Level addObject(GameObject object) {
        objects.add(object);
        return this;
    }

    public List<GameObject> getObjects() {
        return List.copyOf(objects);
    }

    public void update(float deltaTime) {
        for (GameObject object : objects) {
            if (object instanceof Updatable) {
                ((Updatable) object).update(deltaTime);
            }
        }
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

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (!isInside(coordinates)) {
            return false;
        }
        for (GameObject obstacle : objects) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return false;
            }
        }
        return true;
    }
}

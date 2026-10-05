package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/**
 * Описание расстановки уровня: размеры поля, где стоит игрок, где препятствия.
 * Откуда эти данные взялись (файл, генератор, сеть), здесь неважно.
 */
public class LevelLayout {
    private final int width;
    private final int height;
    private final GridPoint2 playerSpawn;
    private final List<GridPoint2> obstacles;

    public LevelLayout(int width, int height, GridPoint2 playerSpawn, List<GridPoint2> obstacles) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        if (playerSpawn == null) {
            throw new IllegalArgumentException("Level must contain a player spawn point");
        }
        this.width = width;
        this.height = height;
        this.playerSpawn = new GridPoint2(playerSpawn);
        this.obstacles = List.copyOf(obstacles);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerSpawn() {
        return new GridPoint2(playerSpawn);
    }

    public List<GridPoint2> getObstacles() {
        return obstacles;
    }
}
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.LevelLayout;
import ru.mipt.bit.platformer.model.LevelSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Расставляет игрока и препятствия случайно по полю заданного размера. */
public class RandomLevelSource implements LevelSource {
    private final int width;
    private final int height;
    private final int obstacleCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int obstacleCount) {
        this(width, height, obstacleCount, new Random());
    }

    /** Конструктор с заданным Random — чтобы тесты были воспроизводимыми. */
    public RandomLevelSource(int width, int height, int obstacleCount, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        if (obstacleCount < 0 || obstacleCount >= width * height) {
            throw new IllegalArgumentException("Obstacle count must leave room for the player: " + obstacleCount);
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.random = random;
    }

    @Override
    public LevelLayout load() {
        List<GridPoint2> cells = new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        GridPoint2 playerSpawn = cells.get(0);
        List<GridPoint2> obstacles = new ArrayList<>(cells.subList(1, 1 + obstacleCount));

        return new LevelLayout(width, height, playerSpawn, obstacles);
    }
}
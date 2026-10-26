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
    private final int tankCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int obstacleCount, int tankCount) {
        this(width, height, obstacleCount, tankCount, new Random());
    }

    /** Конструктор с заданным Random — чтобы тесты были воспроизводимыми. */
    public RandomLevelSource(int width, int height, int obstacleCount, int tankCount, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        if (obstacleCount < 0 || tankCount < 0) {
            throw new IllegalArgumentException("Counts must not be negative");
        }
        if (1 + obstacleCount + tankCount > width * height) {
            throw new IllegalArgumentException(
                    "Level " + width + "x" + height + " is too small for "
                            + obstacleCount + " obstacles and " + tankCount + " tanks");
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.tankCount = tankCount;
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
        List<GridPoint2> tankSpawns = new ArrayList<>(
                cells.subList(1 + obstacleCount, 1 + obstacleCount + tankCount));

        return new LevelLayout(width, height, playerSpawn, obstacles, tankSpawns);
    }
}
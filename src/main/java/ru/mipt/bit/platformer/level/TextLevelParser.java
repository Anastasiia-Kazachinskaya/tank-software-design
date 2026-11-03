package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.LevelLayout;

import java.util.ArrayList;
import java.util.List;

public class TextLevelParser {
    public static final char EMPTY = '_';
    public static final char TREE = 'T';
    public static final char PLAYER = 'X';
    public static final char ENEMY = 'E';

    public LevelLayout parse(List<String> lines) {
        List<String> rows = withoutBlankLines(lines);
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Level is empty");
        }

        int height = rows.size();
        int width = rows.get(0).length();
        List<GridPoint2> obstacles = new ArrayList<>();
        List<GridPoint2> tankSpawns = new ArrayList<>();
        GridPoint2 playerSpawn = null;

        for (int row = 0; row < height; row++) {
            String line = rows.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException(
                        "All level rows must have the same length: row " + row + " has " + line.length() + ", expected " + width);
            }
            int y = height - 1 - row;

            for (int x = 0; x < width; x++) {
                char symbol = line.charAt(x);
                switch (symbol) {
                    case EMPTY:
                        break;
                    case TREE:
                        obstacles.add(new GridPoint2(x, y));
                        break;
                    case PLAYER:
                        if (playerSpawn != null) {
                            throw new IllegalArgumentException("Level must contain exactly one player");
                        }
                        playerSpawn = new GridPoint2(x, y);
                        break;
                    case ENEMY:
                        tankSpawns.add(new GridPoint2(x, y));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown symbol '" + symbol + "' at (" + x + ", " + y + ")");
                }
            }
        }

        if (playerSpawn == null) {
            throw new IllegalArgumentException("Level must contain exactly one player");
        }
        return new LevelLayout(width, height, playerSpawn, obstacles, tankSpawns);
    }

    private List<String> withoutBlankLines(List<String> lines) {
        List<String> rows = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.strip();
            if (!trimmed.isEmpty()) {
                rows.add(trimmed);
            }
        }
        return rows;
    }
}
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.LevelLayout;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextLevelParserTest {
    private TextLevelParser parser;

    @BeforeEach
    void setUp() {
        parser = new TextLevelParser();
    }

    @Test
    void readsSizeFromText() {
        LevelLayout layout = parser.parse(List.of(
                "____",
                "_X__",
                "____"));

        assertEquals(4, layout.getWidth());
        assertEquals(3, layout.getHeight());
    }

    @Test
    void firstLineIsTopRow() {
        // игрок в первой строке => самый верхний ряд => y = height - 1
        LevelLayout layout = parser.parse(List.of(
                "X__",
                "___",
                "___"));

        assertEquals(new GridPoint2(0, 2), layout.getPlayerSpawn());
    }

    @Test
    void readsObstacles() {
        LevelLayout layout = parser.parse(List.of(
                "_T_",
                "X_T"));

        assertEquals(new GridPoint2(0, 0), layout.getPlayerSpawn());
        assertEquals(List.of(new GridPoint2(1, 1), new GridPoint2(2, 0)), layout.getObstacles());
    }

    @Test
    void ignoresBlankLines() {
        LevelLayout layout = parser.parse(List.of("", "X_", "  ", "__", ""));

        assertEquals(2, layout.getHeight());
        assertEquals(new GridPoint2(0, 1), layout.getPlayerSpawn());
    }

    @Test
    void rejectsLevelWithoutPlayer() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("___", "_T_")));
    }

    @Test
    void rejectsLevelWithTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("X_X")));
    }

    @Test
    void rejectsRaggedRows() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("X__", "__")));
    }

    @Test
    void rejectsUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("X_?")));
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of()));
    }
}
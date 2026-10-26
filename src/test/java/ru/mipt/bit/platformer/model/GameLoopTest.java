package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {

    @Test
    void worksWithoutSources() {
        assertDoesNotThrow(() -> new GameLoop().executeCommands(0.1f));
    }

    @Test
    void executesCommandFromEverySource() {
        List<String> executed = new ArrayList<>();
        GameLoop gameLoop = new GameLoop()
                .addCommandSource(deltaTime -> () -> executed.add("first"))
                .addCommandSource(deltaTime -> () -> executed.add("second"));

        gameLoop.executeCommands(0.1f);

        assertEquals(List.of("first", "second"), executed);
    }

    @Test
    void skipsSourcesWithoutCommand() {
        List<String> executed = new ArrayList<>();
        GameLoop gameLoop = new GameLoop()
                .addCommandSource(deltaTime -> null)
                .addCommandSource(deltaTime -> () -> executed.add("second"));

        assertDoesNotThrow(() -> gameLoop.executeCommands(0.1f));
        assertEquals(List.of("second"), executed);
    }

    @Test
    void passesDeltaTimeToSources() {
        List<Float> seen = new ArrayList<>();
        GameLoop gameLoop = new GameLoop().addCommandSource(deltaTime -> {
            seen.add(deltaTime);
            return null;
        });

        gameLoop.executeCommands(0.25f);

        assertEquals(List.of(0.25f), seen);
    }
}
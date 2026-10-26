package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomCommandSourceTest {
    private static final float INTERVAL = 1f;

    @Test
    void givesCommandOnFirstCall() {
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(commands::add, new Random(42), INTERVAL);

        Command command = source.nextCommand(0f);

        assertNotNull(command);
        command.execute();
        assertEquals(1, commands.size());
    }

    @Test
    void waitsForIntervalBeforeNextCommand() {
        RandomCommandSource source = new RandomCommandSource(direction -> {}, new Random(42), INTERVAL);
        source.nextCommand(0f);

        assertNull(source.nextCommand(INTERVAL / 2));
        assertNotNull(source.nextCommand(INTERVAL / 2));
    }

    @Test
    void choosesOnlyValidDirections() {
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(commands::add, new Random(1), INTERVAL);

        for (int i = 0; i < 50; i++) {
            Command command = source.nextCommand(INTERVAL);
            assertNotNull(command);
            command.execute();
        }

        assertEquals(50, commands.size());
        for (Direction direction : commands) {
            assertNotNull(direction);
        }
    }

    @Test
    void eventuallyChoosesEveryDirection() {
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(commands::add, new Random(1), INTERVAL);

        for (int i = 0; i < 100; i++) {
            source.nextCommand(INTERVAL).execute();
        }

        Set<Direction> used = new HashSet<>(commands);
        assertEquals(Direction.values().length, used.size());
    }

    @Test
    void sameSeedGivesSameSequence() {
        List<Direction> first = new ArrayList<>();
        List<Direction> second = new ArrayList<>();

        collect(first, new Random(7));
        collect(second, new Random(7));

        assertEquals(first, second);
    }

    @Test
    void rejectsNonPositiveInterval() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomCommandSource(direction -> {}, new Random(), 0f));
    }

    private void collect(List<Direction> target, Random random) {
        RandomCommandSource source = new RandomCommandSource(target::add, random, INTERVAL);
        for (int i = 0; i < 10; i++) {
            source.nextCommand(INTERVAL).execute();
        }
    }
}
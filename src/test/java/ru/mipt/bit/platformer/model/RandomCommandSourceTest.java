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
    private static final float NEVER_SHOOT = 0f;

    @Test
    void givesCommandOnFirstCall() {
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(
                commands::add, () -> { }, new Random(42), INTERVAL, NEVER_SHOOT);

        Command command = source.nextCommand(0f);

        assertNotNull(command);
        command.execute();
        assertEquals(1, commands.size());
    }

    @Test
    void waitsForIntervalBeforeNextCommand() {
        RandomCommandSource source = new RandomCommandSource(
                direction -> { }, () -> { }, new Random(42), INTERVAL, NEVER_SHOOT);
        source.nextCommand(0f);

        assertNull(source.nextCommand(INTERVAL / 2));
        assertNotNull(source.nextCommand(INTERVAL / 2));
    }

    @Test
    void choosesOnlyValidDirections() {
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(
                commands::add, () -> { }, new Random(1), INTERVAL, NEVER_SHOOT);

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
        RandomCommandSource source = new RandomCommandSource(
                commands::add, () -> { }, new Random(1), INTERVAL, NEVER_SHOOT);

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
    void alwaysShootsWhenProbabilityIsOne() {
        int[] shots = {0};
        RandomCommandSource source = new RandomCommandSource(
                direction -> { }, () -> shots[0]++, new Random(5), INTERVAL, 1f);

        source.nextCommand(INTERVAL).execute();

        assertEquals(1, shots[0]);
    }

    @Test
    void neverShootsWhenProbabilityIsZero() {
        int[] shots = {0};
        List<Direction> commands = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(
                commands::add, () -> shots[0]++, new Random(5), INTERVAL, NEVER_SHOOT);

        for (int i = 0; i < 20; i++) {
            source.nextCommand(INTERVAL).execute();
        }

        assertEquals(0, shots[0]);
        assertEquals(20, commands.size());
    }

    @Test
    void mixesMovementAndShootingInBetween() {
        int[] shots = {0};
        List<Direction> moves = new ArrayList<>();
        RandomCommandSource source = new RandomCommandSource(
                moves::add, () -> shots[0]++, new Random(3), INTERVAL, 0.5f);

        for (int i = 0; i < 100; i++) {
            source.nextCommand(INTERVAL).execute();
        }

        assertEquals(100, shots[0] + moves.size());
        assertTrue(shots[0] > 0, "AI ни разу не выстрелил");
        assertFalse(moves.isEmpty(), "AI ни разу не поехал");
    }

    @Test
    void rejectsNonPositiveInterval() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomCommandSource(direction -> { }, () -> { }, new Random(), 0f, NEVER_SHOOT));
    }

    @Test
    void rejectsInvalidShootProbability() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomCommandSource(direction -> { }, () -> { }, new Random(), INTERVAL, 1.5f));
        assertThrows(IllegalArgumentException.class,
                () -> new RandomCommandSource(direction -> { }, () -> { }, new Random(), INTERVAL, -0.1f));
    }

    private void collect(List<Direction> target, Random random) {
        RandomCommandSource source = new RandomCommandSource(
                target::add, () -> { }, random, INTERVAL, NEVER_SHOOT);
        for (int i = 0; i < 10; i++) {
            source.nextCommand(INTERVAL).execute();
        }
    }
}
package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Command;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardCommandSourceTest {
    private static final int UNBOUND_KEY = Input.Keys.Q;

    @Test
    void noKeysPressedGivesNoCommand() {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add, () -> { }, new FakeKeyboard());

        assertNull(source.nextCommand(0.1f));
        assertTrue(commands.isEmpty());
    }

    @Test
    void wasdKeysGiveMoveCommands() {
        assertCommandFor(Input.Keys.W, Direction.UP);
        assertCommandFor(Input.Keys.A, Direction.LEFT);
        assertCommandFor(Input.Keys.S, Direction.DOWN);
        assertCommandFor(Input.Keys.D, Direction.RIGHT);
    }

    @Test
    void arrowKeysGiveMoveCommands() {
        assertCommandFor(Input.Keys.UP, Direction.UP);
        assertCommandFor(Input.Keys.LEFT, Direction.LEFT);
        assertCommandFor(Input.Keys.DOWN, Direction.DOWN);
        assertCommandFor(Input.Keys.RIGHT, Direction.RIGHT);
    }

    @Test
    void onlyOneDirectionWhenSeveralKeysPressed() {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add, () -> { }, new FakeKeyboard().press(Input.Keys.W, Input.Keys.A));

        source.nextCommand(0.1f).execute();

        // UP стоит в привязках раньше LEFT
        assertEquals(List.of(Direction.UP), commands);
    }

    @Test
    void spaceGivesShootCommand() {
        int[] shots = {0};
        KeyboardCommandSource source = new KeyboardCommandSource(
                direction -> { }, () -> shots[0]++, new FakeKeyboard().press(Input.Keys.SPACE));

        Command command = source.nextCommand(0.1f);

        assertNotNull(command);
        command.execute();
        assertEquals(1, shots[0]);
    }

    @Test
    void shootingWinsOverMovement() {
        int[] shots = {0};
        List<Direction> moves = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                moves::add, () -> shots[0]++, new FakeKeyboard().press(Input.Keys.SPACE, Input.Keys.W));

        source.nextCommand(0.1f).execute();

        assertEquals(1, shots[0]);
        assertTrue(moves.isEmpty());
    }

    @Test
    void unboundKeysAreIgnored() {
        int[] shots = {0};
        KeyboardCommandSource source = new KeyboardCommandSource(
                direction -> { }, () -> shots[0]++, new FakeKeyboard().press(UNBOUND_KEY));

        assertNull(source.nextCommand(0.1f));
        assertEquals(0, shots[0]);
    }

    @Test
    void customBindingsAreUsed() {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add, () -> { },
                new FakeKeyboard().press(UNBOUND_KEY),
                Map.of(UNBOUND_KEY, Direction.DOWN));

        source.nextCommand(0.1f).execute();

        assertEquals(List.of(Direction.DOWN), commands);
    }

    @Test
    void customBindingsDoNotDisableShooting() {
        int[] shots = {0};
        KeyboardCommandSource source = new KeyboardCommandSource(
                direction -> { }, () -> shots[0]++,
                new FakeKeyboard().press(Input.Keys.SPACE),
                Map.of(UNBOUND_KEY, Direction.DOWN));

        source.nextCommand(0.1f).execute();

        assertEquals(1, shots[0]);
    }

    private void assertCommandFor(int key, Direction expected) {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add, () -> { }, new FakeKeyboard().press(key));

        Command command = source.nextCommand(0.1f);

        assertNotNull(command);
        command.execute();
        assertEquals(List.of(expected), commands);
    }
}
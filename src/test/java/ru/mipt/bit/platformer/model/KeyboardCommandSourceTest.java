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

    @Test
    void noKeysPressedGivesNoCommand() {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(commands::add, new FakeKeyboard());

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
                commands::add, new FakeKeyboard().press(Input.Keys.W, Input.Keys.A));

        source.nextCommand(0.1f).execute();

        // UP стоит в привязках раньше LEFT
        assertEquals(List.of(Direction.UP), commands);
    }

    @Test
    void unboundKeysAreIgnored() {
        KeyboardCommandSource source = new KeyboardCommandSource(
                direction -> {}, new FakeKeyboard().press(Input.Keys.SPACE));

        assertNull(source.nextCommand(0.1f));
    }

    @Test
    void customBindingsAreUsed() {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add,
                new FakeKeyboard().press(Input.Keys.SPACE),
                Map.of(Input.Keys.SPACE, Direction.DOWN));

        source.nextCommand(0.1f).execute();

        assertEquals(List.of(Direction.DOWN), commands);
    }

    private void assertCommandFor(int key, Direction expected) {
        List<Direction> commands = new ArrayList<>();
        KeyboardCommandSource source = new KeyboardCommandSource(
                commands::add, new FakeKeyboard().press(key));

        Command command = source.nextCommand(0.1f);

        assertNotNull(command);
        command.execute();
        assertEquals(List.of(expected), commands);
    }
}

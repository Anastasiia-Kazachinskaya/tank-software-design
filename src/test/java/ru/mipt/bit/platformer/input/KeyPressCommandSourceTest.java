package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Command;

import static org.junit.jupiter.api.Assertions.*;

class KeyPressCommandSourceTest {
    private static final int KEY = Input.Keys.L;

    private int[] executions;
    private Command command;
    private FakeKeyboard keyboard;
    private KeyPressCommandSource source;

    @BeforeEach
    void setUp() {
        executions = new int[]{0};
        command = () -> executions[0]++;
        keyboard = new FakeKeyboard();
        source = new KeyPressCommandSource(KEY, keyboard, command);
    }

    @Test
    void noCommandWhileKeyIsNotPressed() {
        assertNull(source.nextCommand(0.1f));
    }

    @Test
    void givesCommandOnPress() {
        keyboard.press(KEY);

        assertSame(command, source.nextCommand(0.1f));
    }

    @Test
    void doesNotRepeatWhileKeyIsHeld() {
        keyboard.press(KEY);

        assertNotNull(source.nextCommand(0.1f));
        assertNull(source.nextCommand(0.1f));
        assertNull(source.nextCommand(0.1f));
    }

    @Test
    void givesCommandAgainAfterRelease() {
        keyboard.press(KEY);
        source.nextCommand(0.1f);
        source.nextCommand(0.1f);

        keyboard.release(KEY);
        assertNull(source.nextCommand(0.1f));

        keyboard.press(KEY);
        assertNotNull(source.nextCommand(0.1f));
    }

    @Test
    void ignoresOtherKeys() {
        keyboard.press(Input.Keys.SPACE);

        assertNull(source.nextCommand(0.1f));
    }
}
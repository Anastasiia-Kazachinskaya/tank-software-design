package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputControllerTest {

    @Test
    void worksWithoutHandlers() {
        assertDoesNotThrow(() -> new InputController(new FakeKeyboard()).handleInput());
    }

    @Test
    void callsAllHandlersInRegistrationOrder() {
        List<String> calls = new ArrayList<>();
        InputController controller = new InputController(new FakeKeyboard())
                .addHandler(keyboard -> calls.add("movement"))
                .addHandler(keyboard -> calls.add("shooting"));

        controller.handleInput();

        assertEquals(List.of("movement", "shooting"), calls);
    }

    @Test
    void passesItsKeyboardToHandlers() {
        FakeKeyboard keyboard = new FakeKeyboard().press(Input.Keys.SPACE);
        List<Boolean> seen = new ArrayList<>();
        InputController controller = new InputController(keyboard)
                .addHandler(k -> seen.add(k.isKeyPressed(Input.Keys.SPACE)));

        controller.handleInput();

        assertEquals(List.of(true), seen);
    }

    @Test
    void handlersAreCalledEveryFrame() {
        int[] counter = {0};
        InputController controller = new InputController(new FakeKeyboard())
                .addHandler(keyboard -> counter[0]++);

        controller.handleInput();
        controller.handleInput();

        assertEquals(2, counter[0]);
    }
}

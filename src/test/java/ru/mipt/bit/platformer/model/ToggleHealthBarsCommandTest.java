package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToggleHealthBarsCommandTest {

    @Test
    void togglesOncePerExecution() {
        boolean[] visible = {false};
        Command command = new ToggleHealthBarsCommand(() -> visible[0] = !visible[0]);

        command.execute();
        assertTrue(visible[0]);

        command.execute();
        assertFalse(visible[0]);
    }

    @Test
    void doesNothingUntilExecuted() {
        int[] toggles = {0};

        new ToggleHealthBarsCommand(() -> toggles[0]++);

        assertEquals(0, toggles[0]);
    }
}
package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveCommandTest {

    @Test
    void movesTargetInGivenDirection() {
        List<Direction> commands = new ArrayList<>();
        Command command = new MoveCommand(commands::add, Direction.LEFT);

        command.execute();

        assertEquals(List.of(Direction.LEFT), commands);
    }

    @Test
    void doesNothingUntilExecuted() {
        List<Direction> commands = new ArrayList<>();

        new MoveCommand(commands::add, Direction.UP);

        assertTrue(commands.isEmpty());
    }

    @Test
    void canBeExecutedSeveralTimes() {
        List<Direction> commands = new ArrayList<>();
        Command command = new MoveCommand(commands::add, Direction.DOWN);

        command.execute();
        command.execute();

        assertEquals(List.of(Direction.DOWN, Direction.DOWN), commands);
    }
}
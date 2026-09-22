package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MovementInputHandlerTest {
    private Level level;
    private Tank tank;
    private MovementInputHandler handler;

    @BeforeEach
    void setUp() {
        // справа от танка дерево
        level = new Level(10, 8, List.of(new Tree(new GridPoint2(3, 2))));
        tank = new Tank(new GridPoint2(2, 2));
        handler = new MovementInputHandler(tank, level);
    }

    @Test
    void noKeysPressedDoesNothing() {
        handler.handleInput(new FakeKeyboard());

        assertFalse(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void wasdKeysMoveTank() {
        assertMovesTo(Input.Keys.W, Direction.UP, new GridPoint2(2, 3));
        assertMovesTo(Input.Keys.A, Direction.LEFT, new GridPoint2(1, 2));
        assertMovesTo(Input.Keys.S, Direction.DOWN, new GridPoint2(2, 1));
    }

    @Test
    void arrowKeysMoveTank() {
        assertMovesTo(Input.Keys.UP, Direction.UP, new GridPoint2(2, 3));
        assertMovesTo(Input.Keys.LEFT, Direction.LEFT, new GridPoint2(1, 2));
        assertMovesTo(Input.Keys.DOWN, Direction.DOWN, new GridPoint2(2, 1));
    }

    @Test
    void blockedMoveOnlyTurnsTank() {
        tank.move(Direction.UP, level);
        tank.update(Tank.DEFAULT_MOVEMENT_DURATION_SECONDS);
        tank.move(Direction.DOWN, level);
        tank.update(Tank.DEFAULT_MOVEMENT_DURATION_SECONDS);

        handler.handleInput(new FakeKeyboard().press(Input.Keys.D));

        assertFalse(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(new GridPoint2(2, 2), tank.getCoordinates());
    }

    @Test
    void onlyOneDirectionIsAppliedWhenSeveralKeysPressed() {
        handler.handleInput(new FakeKeyboard().press(Input.Keys.W, Input.Keys.A));

        // UP стоит в привязках раньше LEFT
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(2, 3), tank.getDestinationCoordinates());
    }

    @Test
    void unboundKeysAreIgnored() {
        handler.handleInput(new FakeKeyboard().press(Input.Keys.SPACE));

        assertFalse(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void customBindingsAreUsed() {
        MovementInputHandler custom = new MovementInputHandler(
                tank, level, Map.of(Input.Keys.SPACE, Direction.DOWN));

        custom.handleInput(new FakeKeyboard().press(Input.Keys.S));
        assertFalse(tank.isMoving());

        custom.handleInput(new FakeKeyboard().press(Input.Keys.SPACE));
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    private void assertMovesTo(int key, Direction direction, GridPoint2 destination) {
        Tank freshTank = new Tank(new GridPoint2(2, 2));
        new MovementInputHandler(freshTank, level).handleInput(new FakeKeyboard().press(key));

        assertTrue(freshTank.isMoving());
        assertEquals(direction, freshTank.getDirection());
        assertEquals(destination, freshTank.getDestinationCoordinates());
    }
}

package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void keepsCoordinates() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }

    @Test
    void isNotAffectedByExternalChanges() {
        GridPoint2 initial = new GridPoint2(1, 3);
        Tree tree = new Tree(initial);

        initial.set(7, 7);
        tree.getCoordinates().set(8, 8);

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }
}

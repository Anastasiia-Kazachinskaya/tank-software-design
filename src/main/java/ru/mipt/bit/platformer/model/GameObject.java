package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Любой объект, занимающий клетки на уровне. */
public interface GameObject {
    GridPoint2 getCoordinates();

    boolean occupies(GridPoint2 coordinates);

    boolean blocksMovement();
}
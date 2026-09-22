package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Любой объект, занимающий клетку на уровне. */
public interface GameObject {
    GridPoint2 getCoordinates();
}

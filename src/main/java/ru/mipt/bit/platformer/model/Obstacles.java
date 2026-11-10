package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

@FunctionalInterface
public interface Obstacles {
    boolean isFree(GridPoint2 coordinates);
}
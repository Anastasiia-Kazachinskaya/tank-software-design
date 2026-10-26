package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Направление движения: хранит единичный вектор смещения по сетке
 * и угол поворота спрайта (в градусах).
 */
public enum Direction {
    UP(0, 1, 90f),
    DOWN(0, -1, -90f),
    LEFT(-1, 0, -180f),
    RIGHT(1, 0, 0f);

    // GridPoint2 изменяемый, поэтому наружу отдаём только копии
    private final GridPoint2 vector;
    private final float rotation;

    Direction(int dx, int dy, float rotation) {
        this.vector = new GridPoint2(dx, dy);
        this.rotation = rotation;
    }

    public GridPoint2 getVector() {
        return new GridPoint2(vector);
    }

    public float getRotation() {
        return rotation;
    }

    /** Возвращает НОВУЮ точку, сдвинутую на одну клетку в этом направлении. */
    public GridPoint2 apply(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(vector);
    }
}

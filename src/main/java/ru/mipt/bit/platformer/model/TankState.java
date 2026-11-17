package ru.mipt.bit.platformer.model;

public interface TankState {
    /** Множитель длительности шага: 1 — обычно, 2 — вдвое медленнее, 0.5 — вдвое быстрее. */
    float getMovementDurationMultiplier();

    /** Множитель перезарядки: 1 — обычно, 0.5 — стрелять вдвое чаще. */
    float getReloadMultiplier();

    boolean canShoot();

    String getName();
}
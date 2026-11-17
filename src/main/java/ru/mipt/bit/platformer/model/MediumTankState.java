package ru.mipt.bit.platformer.model;

/** Среднее состояние: танк повреждён и едет вдвое медленнее. */
public class MediumTankState implements TankState {
    @Override
    public float getMovementDurationMultiplier() {
        return 2f;
    }

    @Override
    public float getReloadMultiplier() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }

    @Override
    public String getName() {
        return "medium";
    }
}
package ru.mipt.bit.platformer.model;

/** Лёгкое состояние: танк не повреждён и ведёт себя обычно. */
public class LightTankState implements TankState {
    @Override
    public float getMovementDurationMultiplier() {
        return 1f;
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
        return "light";
    }
}
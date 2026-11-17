package ru.mipt.bit.platformer.model;

/** Тяжёлое состояние: танк едва жив, едет втрое медленнее и стрелять не может. */
public class HeavyTankState implements TankState {
    @Override
    public float getMovementDurationMultiplier() {
        return 3f;
    }

    @Override
    public float getReloadMultiplier() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }

    @Override
    public String getName() {
        return "heavy";
    }
}
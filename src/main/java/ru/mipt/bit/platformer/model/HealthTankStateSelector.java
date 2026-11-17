package ru.mipt.bit.platformer.model;

/** Выбирает состояние по доле оставшихся жизней. */
public class HealthTankStateSelector implements TankStateSelector {
    public static final float LIGHT_THRESHOLD = 0.7f;
    public static final float MEDIUM_THRESHOLD = 0.15f;

    private final TankState light;
    private final TankState medium;
    private final TankState heavy;

    public HealthTankStateSelector() {
        this(new LightTankState(), new MediumTankState(), new HeavyTankState());
    }

    public HealthTankStateSelector(TankState light, TankState medium, TankState heavy) {
        this.light = light;
        this.medium = medium;
        this.heavy = heavy;
    }

    @Override
    public TankState selectState(float healthFraction) {
        if (healthFraction > LIGHT_THRESHOLD) {
            return light;
        }
        if (healthFraction >= MEDIUM_THRESHOLD) {
            return medium;
        }
        return heavy;
    }
}
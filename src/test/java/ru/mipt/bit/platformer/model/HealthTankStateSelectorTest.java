package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthTankStateSelectorTest {
    private HealthTankStateSelector selector;

    @BeforeEach
    void setUp() {
        selector = new HealthTankStateSelector();
    }

    @Test
    void fullHealthGivesLightState() {
        assertEquals("light", selector.selectState(1f).getName());
        assertEquals("light", selector.selectState(0.71f).getName());
    }

    @Test
    void middleHealthGivesMediumState() {
        assertEquals("medium", selector.selectState(0.7f).getName());
        assertEquals("medium", selector.selectState(0.4f).getName());
        assertEquals("medium", selector.selectState(0.15f).getName());
    }

    @Test
    void lowHealthGivesHeavyState() {
        assertEquals("heavy", selector.selectState(0.14f).getName());
        assertEquals("heavy", selector.selectState(0f).getName());
    }

    @Test
    void statesDifferInSpeedAndShooting() {
        TankState light = selector.selectState(1f);
        TankState medium = selector.selectState(0.5f);
        TankState heavy = selector.selectState(0.1f);

        assertEquals(1f, light.getMovementDurationMultiplier());
        assertEquals(2f, medium.getMovementDurationMultiplier());
        assertEquals(3f, heavy.getMovementDurationMultiplier());

        assertTrue(light.canShoot());
        assertTrue(medium.canShoot());
        assertFalse(heavy.canShoot());
    }

    @Test
    void acceptsCustomStates() {
        TankState booster = new TankState() {
            @Override
            public float getMovementDurationMultiplier() {
                return 0.5f;
            }

            @Override
            public float getReloadMultiplier() {
                return 0.5f;
            }

            @Override
            public boolean canShoot() {
                return true;
            }

            @Override
            public String getName() {
                return "booster";
            }
        };
        TankStateSelector always = healthFraction -> booster;

        assertEquals("booster", always.selectState(0.01f).getName());
    }
}
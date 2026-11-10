package ru.mipt.bit.platformer.model;

/** Умеет включать и выключать показ полосок жизней. */
@FunctionalInterface
public interface HealthBarsToggle {
    void toggleHealthBars();
}
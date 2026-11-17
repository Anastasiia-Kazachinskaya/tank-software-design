package ru.mipt.bit.platformer.model;

/** Умеет включать и выключать показ полосок жизней. */

/** Port (driven) — переключение полосок жизней. Адаптер: GameRenderer. */
@FunctionalInterface
public interface HealthBarsToggle {
    void toggleHealthBars();
}
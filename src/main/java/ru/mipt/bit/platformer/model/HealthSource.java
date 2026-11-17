package ru.mipt.bit.platformer.model;

@FunctionalInterface
public interface HealthSource {

    float getHealthFraction();
}
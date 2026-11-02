package ru.mipt.bit.platformer.model;

public interface Destructible {
    void takeDamage(int damage);

    boolean isDestroyed();
}
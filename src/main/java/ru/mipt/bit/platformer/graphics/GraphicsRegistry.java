package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

import java.util.ArrayList;
import java.util.List;

/** Знает, какой фабрикой делать графику для какого объекта. */
public class GraphicsRegistry {
    private final List<GraphicsFactory> factories = new ArrayList<>();

    public GraphicsRegistry register(GraphicsFactory factory) {
        factories.add(factory);
        return this;
    }

    public Graphics create(GameObject object) {
        for (GraphicsFactory factory : factories) {
            if (factory.supports(object)) {
                return factory.create(object);
            }
        }
        throw new IllegalArgumentException("No graphics factory registered for " + object.getClass().getName());
    }
}
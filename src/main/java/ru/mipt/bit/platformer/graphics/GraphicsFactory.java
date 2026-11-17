package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

/**
 * Умеет создать графику для объектов одного типа.
 * OCP: новый тип объекта — это новая фабрика, рендерер и реестр не меняются.
 */
public interface GraphicsFactory {
    boolean supports(GameObject object);

    Graphics create(GameObject object);
}
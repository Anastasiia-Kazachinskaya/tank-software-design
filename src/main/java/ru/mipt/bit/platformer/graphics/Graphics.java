package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/** Графическое представление одного объекта модели. */
public interface Graphics extends Disposable {
    void render(Batch batch);
}

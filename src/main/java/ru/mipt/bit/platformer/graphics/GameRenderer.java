package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Рисует кадр целиком: очищает экран, рисует карту, затем все объекты.
 * Не знает конкретных типов объектов — только интерфейс Graphics.
 */
public class GameRenderer implements Disposable {
    private final Batch batch;
    private final List<Graphics> objectGraphics = new ArrayList<>();
    private final Background background;

    public GameRenderer(Batch batch, Background background) {
        this.batch = batch;
        this.background = background;
    }
    public GameRenderer addGraphics(Graphics graphics) {
        objectGraphics.add(graphics);
        return this;
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        background.render();

        batch.begin();
        for (Graphics graphics : objectGraphics) {
            graphics.render(batch);
        }
        batch.end();
    }

    /** Владеет всем, что в него добавили, и освобождает это сам. */
    @Override
    public void dispose() {
        for (Graphics graphics : objectGraphics) {
            graphics.dispose();
        }
        background.dispose();
        batch.dispose();
    }
}

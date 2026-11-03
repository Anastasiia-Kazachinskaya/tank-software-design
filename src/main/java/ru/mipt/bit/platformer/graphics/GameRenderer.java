package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Рисует кадр целиком: очищает экран, рисует фон, затем графику всех объектов.
 * Наблюдатель уровня: узнаёт о появлении и исчезновении объектов.
 */
public class GameRenderer implements Disposable, LevelListener {
    private final Batch batch;
    private final Background background;
    private final GraphicsRegistry graphicsRegistry;
    private final Map<GameObject, Graphics> objectGraphics = new LinkedHashMap<>();

    public GameRenderer(Batch batch, Background background, GraphicsRegistry graphicsRegistry) {
        this.batch = batch;
        this.background = background;
        this.graphicsRegistry = graphicsRegistry;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        objectGraphics.computeIfAbsent(object, graphicsRegistry::create);
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        Graphics graphics = objectGraphics.remove(object);
        if (graphics != null) {
            graphics.dispose();
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        background.render();

        batch.begin();
        for (Graphics graphics : objectGraphics.values()) {
            graphics.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (Graphics graphics : objectGraphics.values()) {
            graphics.dispose();
        }
        objectGraphics.clear();
        background.dispose();
        batch.dispose();
    }
}
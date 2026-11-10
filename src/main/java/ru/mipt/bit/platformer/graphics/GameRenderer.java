package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.HealthBarsToggle;
import ru.mipt.bit.platformer.model.HealthSource;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameRenderer implements Disposable, LevelListener, HealthBarsToggle {
    private final Batch batch;
    private final Background background;
    private final GraphicsRegistry graphicsRegistry;
    private final Map<GameObject, Graphics> objectGraphics = new LinkedHashMap<>();
    private final Map<GameObject, Graphics> healthBars = new LinkedHashMap<>();

    private boolean healthBarsVisible;

    public GameRenderer(Batch batch, Background background, GraphicsRegistry graphicsRegistry) {
        this.batch = batch;
        this.background = background;
        this.graphicsRegistry = graphicsRegistry;
    }

    public boolean isHealthBarsVisible() {
        return healthBarsVisible;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        Graphics graphics = objectGraphics.computeIfAbsent(object, graphicsRegistry::create);
        if (healthBarsVisible) {
            decorateWithHealthBar(object, graphics);
        }
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        Graphics healthBar = healthBars.remove(object);
        if (healthBar != null) {
            healthBar.dispose();
        }
        Graphics graphics = objectGraphics.remove(object);
        if (graphics != null) {
            graphics.dispose();
        }
    }

    @Override
    public void toggleHealthBars() {
        healthBarsVisible = !healthBarsVisible;

        if (healthBarsVisible) {
            for (Map.Entry<GameObject, Graphics> entry : objectGraphics.entrySet()) {
                decorateWithHealthBar(entry.getKey(), entry.getValue());
            }
        } else {
            for (Graphics healthBar : healthBars.values()) {
                healthBar.dispose();
            }
            healthBars.clear();
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        background.render();

        batch.begin();
        for (Map.Entry<GameObject, Graphics> entry : objectGraphics.entrySet()) {
            Graphics healthBar = healthBars.get(entry.getKey());
            if (healthBar != null) {
                healthBar.render(batch);
            } else {
                entry.getValue().render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (Graphics healthBar : healthBars.values()) {
            healthBar.dispose();
        }
        healthBars.clear();
        for (Graphics graphics : objectGraphics.values()) {
            graphics.dispose();
        }
        objectGraphics.clear();
        background.dispose();
        batch.dispose();
    }

    private void decorateWithHealthBar(GameObject object, Graphics graphics) {
        if (object instanceof HealthSource) {
            healthBars.put(object, new HealthBarGraphics(graphics, (HealthSource) object));
        }
    }
}
package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.HealthSource;

public class HealthBarGraphics implements Graphics {
    private static final float BAR_WIDTH_RATIO = 0.8f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_MARGIN = 6f;

    private final Graphics decorated;
    private final HealthSource healthSource;
    private final Texture pixel;

    public HealthBarGraphics(Graphics decorated, HealthSource healthSource) {
        this.decorated = decorated;
        this.healthSource = healthSource;
        this.pixel = createWhitePixel();
    }

    @Override
    public void render(Batch batch) {
        decorated.render(batch);

        Rectangle bounds = decorated.getBounds();
        float barWidth = bounds.width * BAR_WIDTH_RATIO;
        float x = bounds.x + (bounds.width - barWidth) / 2f;
        float y = bounds.y + bounds.height + BAR_MARGIN;

        Color previousColor = batch.getColor().cpy();

        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixel, x, y, barWidth, BAR_HEIGHT);

        batch.setColor(Color.LIME);
        batch.draw(pixel, x, y, barWidth * healthSource.getHealthFraction(), BAR_HEIGHT);

        batch.setColor(previousColor);
    }

    @Override
    public Rectangle getBounds() {
        return decorated.getBounds();
    }

    private static Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    @Override
    public void dispose() {
        pixel.dispose();
    }
}
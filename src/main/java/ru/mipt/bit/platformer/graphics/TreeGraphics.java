package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

/** Рисует дерево. Дерево неподвижно, поэтому позиция считается один раз. */
public class TreeGraphics implements Graphics {
    private static final float NO_ROTATION = 0f;

    private final Texture texture;
    private final TextureRegion region;
    private final Rectangle rectangle;

    public TreeGraphics(Tree tree, TiledMapTileLayer groundLayer, String texturePath) {
        this.texture = new Texture(texturePath);
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
        moveRectangleAtTileCenter(groundLayer, rectangle, tree.getCoordinates());
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, region, rectangle, NO_ROTATION);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}

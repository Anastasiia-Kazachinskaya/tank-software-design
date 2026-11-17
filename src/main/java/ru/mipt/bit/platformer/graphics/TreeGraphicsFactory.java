package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tree;

public class TreeGraphicsFactory implements GraphicsFactory {
    private final TiledMapTileLayer groundLayer;
    private final String texturePath;

    public TreeGraphicsFactory(TiledMapTileLayer groundLayer, String texturePath) {
        this.groundLayer = groundLayer;
        this.texturePath = texturePath;
    }

    @Override
    public boolean supports(GameObject object) {
        return object instanceof Tree;
    }

    @Override
    public Graphics create(GameObject object) {
        return new TreeGraphics((Tree) object, groundLayer, texturePath);
    }
}
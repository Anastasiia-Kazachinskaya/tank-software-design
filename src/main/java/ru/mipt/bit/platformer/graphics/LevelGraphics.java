package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;


public class LevelGraphics implements Background {
    private final TiledMap tiledMap;
    private final TiledMapTileLayer groundLayer;
    private final MapRenderer mapRenderer;

    public LevelGraphics(String levelPath, Batch batch) {
        tiledMap = new TmxMapLoader().load(levelPath);
        groundLayer = getSingleLayer(tiledMap);
        mapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public int getWidth() {
        return groundLayer.getWidth();
    }

    public int getHeight() {
        return groundLayer.getHeight();
    }

    @Override
    public void render() {
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}

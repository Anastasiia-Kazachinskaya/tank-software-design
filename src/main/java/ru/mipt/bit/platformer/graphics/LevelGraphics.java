package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

/** Загружает tmx-карту и рисует её фон. */
public class LevelGraphics implements Disposable {
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

    /** Карта рисуется до batch.begin(): рендерер карты сам управляет batch. */
    public void render() {
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}

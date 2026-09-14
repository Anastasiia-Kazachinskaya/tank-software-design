package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameRenderer {
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    private final Batch batch;
    private final TiledMap tiledMap;
    private final TiledMapTileLayer groundLayer;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;

    private final Texture blueTankTexture;
    private final TextureRegion tankGraphics;
    private final Rectangle tankRectangle;

    private final Texture greenTreeTexture;
    private final TextureRegion treeGraphics;
    private final Rectangle treeRectangle;

    public GameRenderer() {
        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load(LEVEL_PATH);

        groundLayer = getSingleLayer(tiledMap);
        levelRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        blueTankTexture = new Texture(TANK_TEXTURE_PATH);
        tankGraphics = new TextureRegion(blueTankTexture);
        tankRectangle = createBoundingRectangle(tankGraphics);

        greenTreeTexture = new Texture(TREE_TEXTURE_PATH);
        treeGraphics = new TextureRegion(greenTreeTexture);
        treeRectangle = createBoundingRectangle(treeGraphics);
    }

    public int getLevelWidth() {
        return groundLayer.getWidth();
    }

    public int getLevelHeight() {
        return groundLayer.getHeight();
    }

    public void render(Tank tank, Tree tree) {
        tileMovement.moveRectangleBetweenTileCenters(
                tankRectangle,
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress()
        );

        moveRectangleAtTileCenter(
                groundLayer,
                treeRectangle,
                tree.getCoordinates()
        );

        levelRenderer.render();

        batch.begin();

        drawTextureRegionUnscaled(
                batch,
                tankGraphics,
                tankRectangle,
                tank.getDirection().getRotation()
        );

        drawTextureRegionUnscaled(
                batch,
                treeGraphics,
                treeRectangle,
                0f
        );

        batch.end();
    }

    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
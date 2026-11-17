package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.BulletGraphicsFactory;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.GraphicsRegistry;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphicsFactory;
import ru.mipt.bit.platformer.graphics.TreeGraphicsFactory;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardCommandSource;
import ru.mipt.bit.platformer.level.FileLevelSource;
import ru.mipt.bit.platformer.level.RandomLevelSource;
import ru.mipt.bit.platformer.model.GameLoop;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelLayout;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomCommandSource;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.KeyPressCommandSource;
import ru.mipt.bit.platformer.model.ToggleHealthBarsCommand;

public class GameDesktopLauncher implements ApplicationListener {
    private static final String MAP_PATH = "level.tmx";
    private static final String LEVEL_LAYOUT_PATH = "level.txt";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";
    private static final String BULLET_TEXTURE_PATH = "images/brick.png";
    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    private static final int RANDOM_LEVEL_WIDTH = 10;
    private static final int RANDOM_LEVEL_HEIGHT = 8;
    private static final int RANDOM_OBSTACLE_COUNT = 7;
    private static final int RANDOM_TANK_COUNT = 3;

    private final LevelSource levelSource;

    private GameRenderer renderer;
    private GameLoop gameLoop;
    private Level level;

    public GameDesktopLauncher(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        LevelLayout layout = levelSource.load();

        // модель
        level = new Level(layout.getWidth(), layout.getHeight());
        gameLoop = new GameLoop();

        for (GridPoint2 obstacle : layout.getObstacles()) {
            level.addObject(new Tree(obstacle));
        }

        Tank playerTank = new Tank(layout.getPlayerSpawn(), level, level,
                Tank.DEFAULT_MOVEMENT_DURATION_SECONDS, Tank.DEFAULT_HEALTH);
        level.addObject(playerTank);
        gameLoop.addCommandSource(new KeyboardCommandSource(playerTank, playerTank, new GdxKeyboard()));

        for (GridPoint2 spawn : layout.getTankSpawns()) {
            Tank aiTank = new Tank(spawn, level, level,
                    Tank.DEFAULT_MOVEMENT_DURATION_SECONDS, Tank.DEFAULT_HEALTH);
            level.addObject(aiTank);
            gameLoop.addCommandSource(new RandomCommandSource(aiTank, aiTank));
        }

        // графика
        Batch batch = new SpriteBatch();
        LevelGraphics levelGraphics = new LevelGraphics(MAP_PATH, batch);
        if (layout.getWidth() != levelGraphics.getWidth() || layout.getHeight() != levelGraphics.getHeight()) {
            throw new IllegalStateException(
                    "Level layout " + layout.getWidth() + "x" + layout.getHeight()
                            + " does not match the map " + levelGraphics.getWidth() + "x" + levelGraphics.getHeight());
        }
        TileMovement tileMovement = new TileMovement(levelGraphics.getGroundLayer(), Interpolation.smooth);

        GraphicsRegistry graphicsRegistry = new GraphicsRegistry()
                .register(new TankGraphicsFactory(tileMovement, TANK_TEXTURE_PATH))
                .register(new BulletGraphicsFactory(tileMovement, BULLET_TEXTURE_PATH))
                .register(new TreeGraphicsFactory(levelGraphics.getGroundLayer(), TREE_TEXTURE_PATH));

        renderer = new GameRenderer(batch, levelGraphics, graphicsRegistry);
        level.addListener(renderer);

        gameLoop.addCommandSource(new KeyPressCommandSource(
                Input.Keys.L, new GdxKeyboard(), new ToggleHealthBarsCommand(renderer)));
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        gameLoop.executeCommands(deltaTime);
        level.update(deltaTime);
        renderer.render();
    }


    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(levelSourceFrom(args)), config);
    }

    private static LevelSource levelSourceFrom(String[] args) {
        if (args.length > 0 && RANDOM_LEVEL_ARGUMENT.equals(args[0])) {
            return new RandomLevelSource(RANDOM_LEVEL_WIDTH, RANDOM_LEVEL_HEIGHT,
                    RANDOM_OBSTACLE_COUNT, RANDOM_TANK_COUNT);
        }
        return new FileLevelSource(LEVEL_LAYOUT_PATH);
    }
}
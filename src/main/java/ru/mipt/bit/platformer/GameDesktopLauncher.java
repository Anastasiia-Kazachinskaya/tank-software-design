package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardCommandSource;
import ru.mipt.bit.platformer.level.FileLevelSource;
import ru.mipt.bit.platformer.level.RandomLevelSource;
import ru.mipt.bit.platformer.model.GameLoop;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelLayout;
import ru.mipt.bit.platformer.model.LevelSource;
import ru.mipt.bit.platformer.model.RandomCommandSource;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

/**
 * Точка входа: собирает модель, графику и ввод и крутит игровой цикл
 * (команды -> обновление модели -> отрисовка). Своей логики не содержит.
 */
public class GameDesktopLauncher implements ApplicationListener {
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";
    private static final String MAP_PATH = "level.tmx";
    private static final String LEVEL_LAYOUT_PATH = "level.txt";
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

        List<Tree> trees = new ArrayList<>();
        for (GridPoint2 obstacle : layout.getObstacles()) {
            Tree tree = new Tree(obstacle);
            trees.add(tree);
            level.addObject(tree);
        }

        List<Tank> tanks = new ArrayList<>();
        Tank playerTank = new Tank(layout.getPlayerSpawn(), level);
        tanks.add(playerTank);
        level.addObject(playerTank);
        gameLoop.addCommandSource(new KeyboardCommandSource(playerTank, new GdxKeyboard()));

        for (GridPoint2 spawn : layout.getTankSpawns()) {
            Tank aiTank = new Tank(spawn, level);
            tanks.add(aiTank);
            level.addObject(aiTank);
            gameLoop.addCommandSource(new RandomCommandSource(aiTank));
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

        renderer = new GameRenderer(batch, levelGraphics);
        for (Tank tank : tanks) {
            renderer.addGraphics(new TankGraphics(tank, tileMovement, TANK_TEXTURE_PATH));
        }
        for (Tree tree : trees) {
            renderer.addGraphics(new TreeGraphics(tree, levelGraphics.getGroundLayer(), TREE_TEXTURE_PATH));
        }
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
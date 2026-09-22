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
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.MovementInputHandler;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

/**
 * Точка входа: собирает модель, графику и ввод и крутит игровой цикл
 * (ввод -> обновление модели -> отрисовка). Своей логики не содержит.
 */
public class GameDesktopLauncher implements ApplicationListener {
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    private GameRenderer renderer;
    private InputController inputController;
    private Tank tank;

    @Override
    public void create() {
        Batch batch = new SpriteBatch();
        LevelGraphics levelGraphics = new LevelGraphics(LEVEL_PATH, batch);

        // модель
        Tree tree = new Tree(new GridPoint2(1, 3));
        tank = new Tank(new GridPoint2(1, 1));
        Level level = new Level(levelGraphics.getWidth(), levelGraphics.getHeight(), List.of(tree));

        // графика
        TileMovement tileMovement = new TileMovement(levelGraphics.getGroundLayer(), Interpolation.smooth);
        renderer = new GameRenderer(batch, levelGraphics)
                .addGraphics(new TankGraphics(tank, tileMovement, TANK_TEXTURE_PATH))
                .addGraphics(new TreeGraphics(tree, levelGraphics.getGroundLayer(), TREE_TEXTURE_PATH));

        // ввод
        inputController = new InputController(new GdxKeyboard())
                .addHandler(new MovementInputHandler(tank, level));
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        inputController.handleInput();
        tank.update(deltaTime);
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
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}

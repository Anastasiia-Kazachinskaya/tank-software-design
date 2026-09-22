package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {
    private GameRenderer renderer;
    private Level level;
    private Tank tank;
    private Tree tree;
    private InputController inputController;

    @Override
    public void create() {
        renderer = new GameRenderer();

        tree = new Tree(new GridPoint2(1, 3));

        level = new Level(
                renderer.getLevelWidth(),
                renderer.getLevelHeight(),
                List.of(tree)
        );

        tank = new Tank(new GridPoint2(1, 1));
        inputController = new InputController();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        Direction direction = inputController.getDirection();

        if (direction != null) {
            tank.move(direction, level);
        }

        tank.update(deltaTime);

        renderer.render(tank, tree);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(
                new GameDesktopLauncher(),
                config
        );
    }
}
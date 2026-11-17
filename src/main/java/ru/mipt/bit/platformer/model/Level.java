package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;
import java.util.ArrayList;


public class Level implements Obstacles, ObjectSpawner {
    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<LevelListener> listeners = new ArrayList<>();

    public Level(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
    }

    public Level addObject(GameObject object) {
        objects.add(object);
        for (LevelListener listener : List.copyOf(listeners)) {
            listener.onObjectAdded(object);
        }
        return this;
    }

    public List<GameObject> getObjects() {
        return List.copyOf(objects);
    }

    /** Шаг времени: все объекты живут, потом разбираем столкновения и убираем уничтоженных. */
    public void update(float deltaTime) {
        for (GameObject object : List.copyOf(objects)) {
            if (object instanceof Updatable) {
                ((Updatable) object).update(deltaTime);
            }
        }
        resolveCollisions();
        removeDestroyed();
    }

    /** Пуля гибнет, вылетев за поле или попав в любой объект; цель получает урон. */
    private void resolveCollisions() {
        for (GameObject object : List.copyOf(objects)) {
            if (!(object instanceof Bullet)) {
                continue;
            }
            Bullet bullet = (Bullet) object;
            if (bullet.isDestroyed()) {
                continue;
            }
            if (!isInside(bullet.getCoordinates())) {
                bullet.destroy();
                continue;
            }
            for (GameObject other : List.copyOf(objects)) {
                if (other == bullet || !other.occupies(bullet.getCoordinates())) {
                    continue;
                }
                bullet.destroy();
                if (other instanceof Destructible) {
                    ((Destructible) other).takeDamage(bullet.getDamage());
                }
                break;
            }
        }
    }

    private void removeDestroyed() {
        for (GameObject object : List.copyOf(objects)) {
            if (object instanceof Destructible && ((Destructible) object).isDestroyed()) {
                objects.remove(object);
                for (LevelListener listener : List.copyOf(listeners)) {
                    listener.onObjectRemoved(object);
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (!isInside(coordinates)) {
            return false;
        }
        for (GameObject object : objects) {
            if (object.blocksMovement() && object.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void spawn(GameObject object) {
        addObject(object);
    }
    /** Подписывает слушателя и сразу рассказывает ему про уже существующие объекты. */
    public Level addListener(LevelListener listener) {
        listeners.add(listener);
        for (GameObject object : List.copyOf(objects)) {
            listener.onObjectAdded(object);
        }
        return this;
    }
}

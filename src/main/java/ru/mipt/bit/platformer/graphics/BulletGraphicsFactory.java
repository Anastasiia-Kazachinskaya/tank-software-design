package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

public class BulletGraphicsFactory implements GraphicsFactory {
    private final TileMovement tileMovement;
    private final String texturePath;

    public BulletGraphicsFactory(TileMovement tileMovement, String texturePath) {
        this.tileMovement = tileMovement;
        this.texturePath = texturePath;
    }

    @Override
    public boolean supports(GameObject object) {
        return object instanceof Bullet;
    }

    @Override
    public Graphics create(GameObject object) {
        return new BulletGraphics((Bullet) object, tileMovement, texturePath);
    }
}
package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphicsFactory implements GraphicsFactory {
    private final TileMovement tileMovement;
    private final String texturePath;

    public TankGraphicsFactory(TileMovement tileMovement, String texturePath) {
        this.tileMovement = tileMovement;
        this.texturePath = texturePath;
    }

    @Override
    public boolean supports(GameObject object) {
        return object instanceof Tank;
    }

    @Override
    public Graphics create(GameObject object) {
        return new TankGraphics((Tank) object, tileMovement, texturePath);
    }
}
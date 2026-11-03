package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

/** Рисует летящую пулю, интерполируя позицию между клетками. */
public class BulletGraphics implements Graphics {
    private final Bullet bullet;
    private final TileMovement tileMovement;

    private final Texture texture;
    private final TextureRegion region;
    private final Rectangle rectangle;

    public BulletGraphics(Bullet bullet, TileMovement tileMovement, String texturePath) {
        this.bullet = bullet;
        this.tileMovement = tileMovement;
        this.texture = new Texture(texturePath);
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                bullet.getCoordinates(),
                bullet.getDestinationCoordinates(),
                bullet.getMovementProgress()
        );
        drawTextureRegionUnscaled(batch, region, rectangle, bullet.getDirection().getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
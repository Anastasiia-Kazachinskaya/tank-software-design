package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.LevelLayout;

import static org.junit.jupiter.api.Assertions.*;

class FileLevelSourceTest {

    @Test
    void readsLevelFromResources() {
        LevelLayout layout = new FileLevelSource("test-level.txt").load();

        assertEquals(4, layout.getWidth());
        assertEquals(3, layout.getHeight());
        assertEquals(new GridPoint2(1, 1), layout.getPlayerSpawn());
        assertEquals(1, layout.getObstacles().size());
        assertEquals(new GridPoint2(2, 2), layout.getObstacles().get(0));
    }

    @Test
    void failsClearlyWhenFileIsMissing() {
        assertThrows(IllegalStateException.class, () -> new FileLevelSource("no-such-level.txt").load());
    }
}
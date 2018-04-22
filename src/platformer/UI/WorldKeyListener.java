package platformer.UI;

import platformer.GameEngine.World;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WorldKeyListener extends KeyAdapter {
    private World world;
    public WorldKeyListener(World world) {
        this.world = world;
    }

    public void keyPressed(KeyEvent e) {
        this.world.onKeyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        this.world.onKeyReleased(e);
    }
}

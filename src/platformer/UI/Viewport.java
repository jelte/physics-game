package platformer.UI;

import platformer.GameEngine.Camera;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Component;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.World;

import javax.swing.*;
import java.awt.*;

public class Viewport extends JComponent
{
    private World world;
    private Camera camera;
    private long refreshRate;

    private Thread thread;

    public Viewport(World world, long refreshRate)
    {
        this.refreshRate = refreshRate;
        this.world = world;
        this.attach(new Camera(10));
        startThread(this);
    }

    private void startThread(final Viewport view) {
        thread = new Thread(() -> {
            // this while loop will exit any time this method is called for a second time, because
            while (thread==Thread.currentThread()) {
                view.repaint();
                try {
                    Thread.sleep(1000/refreshRate);
                } catch (InterruptedException e) {
                }
            }
        });// this will cause any old threads running to self-terminate
        thread.start();
    }


    @Override
    public void paintComponent(Graphics g0)
    {
        world.update();
        camera.setViewport(this.getSize());

        Graphics2D g = (Graphics2D) g0;
        // paint the background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (GameObject gameObject : world.getGameObjects()) {
            for (Component drawable : gameObject.getComponentsInChildren(Drawable.class)) {
                ((Drawable) drawable).draw(g, camera);
            }
        }
    }

    private void attach(Camera camera)
    {
        this.camera = camera;
        camera.setViewport(this.getSize());
    }
}

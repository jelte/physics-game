package platformer.Breakout.Bricks;

import platformer.Breakout.Bar;
import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

import java.awt.*;

public class BrickPowerUpCollisionHandler extends AbstractComponent implements CollisionHandler
{
    private Bar bar;
    private double factor = 2.0;

    public BrickPowerUpCollisionHandler(Bar bar, double factor)
    {
        this.bar = bar;
        this.factor = factor;
    }

    @Override
    public void onCollision(Collision collision)
    {
        // When colliding expand the bar by factor.
        bar.expand(factor);
        // Remove this collision handler from the brick to avoid double buffs.
        this.gameObject.removeComponent(getClass());
    }

    public void setGameObject(GameObject gameObject) {
        super.setGameObject(gameObject);
        if (factor > 1) {
            // Draw a vertical line to create a +.
            gameObject.addComponent(new Line(new Vector2D(0, -.5), new Vector2D(0, .5), new Color(0, 0, 0)));
        }
        // Draw a horizontal line to create a -.
        gameObject.addComponent(new Line(new Vector2D(-.5, 0), new Vector2D(.5,0), new Color(0,0,0)));
    }
}

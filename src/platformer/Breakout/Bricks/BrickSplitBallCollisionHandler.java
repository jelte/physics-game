package platformer.Breakout.Bricks;

import platformer.Breakout.Ball;
import platformer.Breakout.Bar;
import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawables.Circle;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

import java.awt.*;

public class BrickSplitBallCollisionHandler extends AbstractComponent implements CollisionHandler
{
    private World world;

    public BrickSplitBallCollisionHandler(World world)
    {
        this.world = world;
    }

    @Override
    public void onCollision(Collision collision)
    {
        // Create a new ball
        Ball ball = new Ball(gameObject.getPosition());
        Body body = ((Body) ball.getComponent(Body.class));
        // set the velocity
        body.setVelocity(collision.getBody().getVelocity().scale(Vector2D.left().add(Vector2D.up())));
        world.add(ball);
        // Remove this collision handler from the brick to avoid double buffs.
        this.gameObject.removeComponent(getClass());
    }

    public void setGameObject(GameObject gameObject) {
        super.setGameObject(gameObject);
        gameObject.addComponent(new Circle(0.75, new Color(0,0,0)));
    }
}

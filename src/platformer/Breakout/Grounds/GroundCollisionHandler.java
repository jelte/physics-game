package platformer.Breakout.Grounds;

import platformer.Breakout.Ball;
import platformer.Breakout.Bar;
import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

public class GroundCollisionHandler extends AbstractComponent implements CollisionHandler
{
    private World world;

    public GroundCollisionHandler(World world)
    {
        this.world = world;
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getBody().getGameObject() instanceof Ball) {
            // Destroy the ball.
            collision.getBody().getGameObject().destroy();
            // If no ball is in the game, create a new ball.
            if (!world.hasGameObject(Ball.class)) {
                Bar bar = (Bar) world.getGameObject(Bar.class);
                Ball ball = new Ball(Vector2D.up().mult(2));
                bar.add(ball);
            }
        }
    }
}

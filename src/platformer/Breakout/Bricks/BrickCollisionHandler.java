package platformer.Breakout.Bricks;

import platformer.Breakout.Brick;
import platformer.GameEngine.AbstractComponent;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

public class BrickCollisionHandler extends AbstractComponent implements CollisionHandler
{
    @Override
    public void onCollision(Collision collision)
    {
        ((Brick) this.gameObject).takeDamage(1);
    }
}

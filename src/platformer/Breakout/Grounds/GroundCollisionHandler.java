package platformer.Breakout.Grounds;

import platformer.GameEngine.AbstractComponent;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

public class GroundCollisionHandler extends AbstractComponent implements CollisionHandler
{
    @Override
    public void onCollision(Collision collision) {
        collision.getBody().getGameObject().destroy();
    }
}

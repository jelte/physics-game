package platformer.PhysicsEngine;

import platformer.GameEngine.Component;

public interface CollisionHandler extends Component
{
    void onCollision(Collision collision);
}

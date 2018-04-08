package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Component;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

import java.awt.*;
import java.util.Collection;

abstract class AbstractCollider extends AbstractComponent implements Collider
{
    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    public void onCollision(Collision collision)
    {
        Collection<? extends Component> components = gameObject.getComponents(CollisionHandler.class);
        for (Component c : components) {
            ((CollisionHandler) c).onCollision(collision);
        }
    }

    @Override
    public void setColor(Color color) {

    }
}

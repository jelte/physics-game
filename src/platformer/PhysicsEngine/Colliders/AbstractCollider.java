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
    private double coefficientOfRestitution = 1.0;

    public AbstractCollider(double coefficientOfRestitution)
    {
        this.coefficientOfRestitution = coefficientOfRestitution;
    }

    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    public double getCoefficientOfRestitution()
    {
        return coefficientOfRestitution;
    }
    public void setCoefficientOfRestitution(double coefficientOfRestitution)
    {
        this.coefficientOfRestitution = coefficientOfRestitution;
    }

    public boolean checkCollision(Vector2D point, Vector2D velocity, double radius, double tolerance)
    {
        return false;
    }

    public void onCollision(Collision collision)
    {
        Collection<? extends Component> components = gameObject.getComponents(CollisionHandler.class);
        for (Component c : components) {
            ((CollisionHandler) c).onCollision(collision);
        }
    }

    public void setColor(Color color) {
    }
}

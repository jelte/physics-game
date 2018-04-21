package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;

import java.util.List;

public class Collision
{
    private final Body body;
    private final Collider collider;
    private final List<Vector2D> points;

    public Collision(Body body, Collider collider, List<Vector2D> points)
    {
        this.body = body;
        this.collider = collider;
        this.points = points;
    }

    public Collider getCollider() {
        return collider;
    }

    public Body getBody() {
        return body;
    }

    public List<Vector2D> getContactPoints() {
        return points;
    }

    public boolean isColliding() { return points.size() > 0; }

    public double getCoefficientOfRestitution()
    {
        return collider.getCoefficientOfRestitution();
    }
}

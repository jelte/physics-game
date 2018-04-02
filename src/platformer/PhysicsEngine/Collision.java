package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;

import java.util.List;

public class Collision
{
    private final Collider collider;
    private final Collider other;
    private final List<Vector2D> points;

    public Collision(Collider collider, Collider other, List<Vector2D> points)
    {
        this.collider = collider;
        this.other = other;
        this.points = points;
    }

    public Collider getCollider() {
        return collider;
    }

    public Collider getOther() {
        return other;
    }

    public List<Vector2D> getContactPoints() {
        return points;
    }
}

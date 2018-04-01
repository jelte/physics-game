package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;

public class Collision
{
    private final Collider collider;
    private final Collider other;
    private final Vector2D point;

    public Collision(Collider collider, Collider other, Vector2D point)
    {
        this.collider = collider;
        this.other = other;
        this.point = point;
    }

    public Vector2D calculateVelocityAfterACollision(Vector2D vel)
    {
        return other.calculateVelocityAfterACollision(point, vel);
    }
}

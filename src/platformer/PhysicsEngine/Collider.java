package platformer.PhysicsEngine;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Component;
import platformer.GameEngine.Vector2D;

public interface Collider extends Component, Drawable
{
    Collision collide(Collider other);
    boolean collidesAt(Vector2D point);

    Vector2D getPosition();

    boolean isNormalPointsInwards();

    Vector2D calculateVelocityAfterACollision(Vector2D pos, Vector2D vel);
}

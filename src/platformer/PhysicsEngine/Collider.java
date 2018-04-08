package platformer.PhysicsEngine;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Component;
import platformer.GameEngine.Vector2D;

import java.util.List;

public interface Collider extends Component, Drawable
{
    Vector2D getPosition();
    boolean isNormalPointsInwards();

    Vector2D getUnitNormal(Vector2D contactPoint);
    Vector2D getUnitTangent(Vector2D contactPoint);

    List<? extends Vector2D> getCorners();

    Vector2D getAP(Vector2D point);

    double getLength();

    void onCollision(Collision collision);
}

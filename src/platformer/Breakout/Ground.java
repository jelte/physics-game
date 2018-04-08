package platformer.Breakout;

import platformer.Breakout.Grounds.GroundCollisionHandler;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Colliders.LineCollider;

public class Ground extends GameObject
{
    public Ground(Vector2D position)
    {
        super(position);

        addComponent(new LineCollider(Vector2D.left().mult(35), Vector2D.right().mult(35)));
        addComponent(new GroundCollisionHandler());
    }
}

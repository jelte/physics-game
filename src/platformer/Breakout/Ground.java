package platformer.Breakout;

import platformer.Breakout.Grounds.GroundCollisionHandler;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
import platformer.PhysicsEngine.Colliders.LineCollider;

public class Ground extends GameObject
{
    public Ground(Vector2D position)
    {
        super(position);

        // Add collider.
        addComponent(new LineCollider(Vector2D.left().mult(35), Vector2D.right().mult(35)));
    }

    public void setWorld(World world) {
        super.setWorld(world);
        // add collision handler.
        addComponent(new GroundCollisionHandler(world));
    }
}

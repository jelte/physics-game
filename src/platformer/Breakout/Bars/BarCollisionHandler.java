package platformer.Breakout.Bars;

import platformer.Breakout.Bar;
import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Time;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.CollisionHandler;

public class BarCollisionHandler extends AbstractComponent implements CollisionHandler
{
    @Override
    public void onCollision(Collision collision) {
        collision.getBody().applyForce(Vector2D.left().mult(((Bar) gameObject).getSpeed() / Time.deltaTime));
    }
}

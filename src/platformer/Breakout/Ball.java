package platformer.Breakout;

import platformer.GameEngine.Behaviours.Drawables.Circle;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Colliders.PolygonCollider;

import java.awt.*;
import java.util.Random;

public class Ball extends GameObject
{
    private Body body;

    public Ball(Vector2D position)
    {
        super(position);

        body = new Body(1, 0.1);
        addComponent(body);
        // TODO: Change to curve collider or particle
        addComponent(new PolygonCollider(16, 1));

        addComponent(new Circle(2, Color.RED));

        addComponent(new Circle(new Vector2D(.05, -.05), 1.7, new Color(0,0,0, 50)));
        addComponent(new Circle(new Vector2D(.1, -.1), 1.4, new Color(0,0,0, 50)));
        addComponent(new Circle(new Vector2D(.2, -.2), 1.3, new Color(0,0,0, 50)));

        //body.setVelocity(Vector2D.down().add(Vector2D.left()).mult(25).rotate(new Random().nextInt(360)));
        body.setVelocity(Vector2D.down().mult(25));
    }
}

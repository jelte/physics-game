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
    public Ball(Vector2D position)
    {
        super(position);
        // add body.
        ((Body) addComponent(new Body(10))).enableGravity(false);
        // add collider.
        addComponent(new PolygonCollider(16, 1));
        // add visual components.
        addComponent(new Circle(2, Color.RED));
        addComponent(new Circle(new Vector2D(.05, -.05), 1.7, new Color(0,0,0, 50)));
        addComponent(new Circle(new Vector2D(.1, -.1), 1.4, new Color(0,0,0, 50)));
        addComponent(new Circle(new Vector2D(.2, -.2), 1.3, new Color(0,0,0, 50)));
    }
}

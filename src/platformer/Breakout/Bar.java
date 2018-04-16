package platformer.Breakout;

import platformer.Breakout.Bars.BarCollisionHandler;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Time;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Colliders.LineCollider;

import java.awt.*;

public class Bar extends GameObject {

    private double speed = 0.0;
    private double maxSpeed = 60.0;

    private Body body;

    public Bar(Vector2D position)
    {
        super(position);

      //  body = (Body) addComponent(new Body(1));

        addComponent(new LineCollider(new Vector2D(4, -0.5), new Vector2D(-4,-0.5)));
        addComponent(new LineCollider(new Vector2D(-4, -0.5), new Vector2D(-4,0.5)));
        addComponent(new LineCollider(new Vector2D(-4, 0.5), new Vector2D(4,0.5)));
        addComponent(new LineCollider(new Vector2D(4, 0.5), new Vector2D(4,-0.5)));

        addComponent(new Rectangle(new Dimension(8, 1), new Color(137, 59, 0)));

        addComponent(new Line(new Vector2D(4, -0.4), new Vector2D(-4,-0.4), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(3.9, -0.5), new Vector2D(3.9,0.5), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(4, 0.5), new Vector2D(-4,0.5), new Color(255,255,255, 150)));
        addComponent(new Line(new Vector2D(-4, -0.5), new Vector2D(-4,.5), new Color(255,255,255, 150)));

        addComponent(new BarController());
        addComponent(new BarCollisionHandler());
    }

    public void setSpeed(double speed)
    {
        this.speed = Math.abs(speed) <= maxSpeed ? speed : this.speed;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public void update()
    {
        setPosition(getPosition().add(Vector2D.left().mult(speed * Time.deltaTime)));
    }

    public void launch() {
        Ball ball = (Ball) findChildByType(Ball.class);
        if (ball != null) {
            ball.setPosition(ball.getPosition());
            remove(ball);
            ((Body) ball.getComponent(Body.class)).setVelocity(Vector2D.left().mult(speed).add(Vector2D.up().mult(25)));
        }
    }
}

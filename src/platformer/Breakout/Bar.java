package platformer.Breakout;

import platformer.Breakout.Bars.BarCollisionHandler;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.Component;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Time;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Colliders.LineCollider;

import java.awt.*;
import java.util.ArrayList;

public class Bar extends GameObject {

    private double speed = 0.0;
    private double maxSpeed = 100.0;
    private int width = 8;

    public Bar(Vector2D position)
    {
        super(position);

        rebuild();

        addComponent(new BarController());
        addComponent(new BarCollisionHandler());
    }

    private void rebuild()
    {
        if (world != null) {
            for (Component component : getComponents(Collider.class)) {
                world.getPhysics().unregister((Collider) component);
            }
        }
        removeComponents(Collider.class);
        removeComponents(Line.class);
        removeComponents(Rectangle.class);

        ArrayList<Collider> colliders = new ArrayList<>();
        colliders.add((Collider)addComponent(new LineCollider(new Vector2D(width/2, -0.5), new Vector2D(-width/2,-0.5))));
        colliders.add((Collider)addComponent(new LineCollider(new Vector2D(-width/2, -0.5), new Vector2D(-width/2,0.5))));
        colliders.add((Collider)addComponent(new LineCollider(new Vector2D(-width/2, 0.5), new Vector2D(width/2,0.5))));
        colliders.add((Collider)addComponent(new LineCollider(new Vector2D(width/2, 0.5), new Vector2D(width/2,-0.5))));

        if (world != null) {
            for (Collider collider : colliders) {
                world.getPhysics().register(collider);
            }
        }

        addComponent(new Rectangle(new Dimension(width, 1), new Color(137, 59, 0)));

        addComponent(new Line(new Vector2D(width/2, -0.4), new Vector2D(-width/2,-0.4), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(width/2-.1, -0.5), new Vector2D(width/2-.1,0.5), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(width/2, 0.5), new Vector2D(-width/2,0.5), new Color(255,255,255, 150)));
        addComponent(new Line(new Vector2D(-width/2, -0.5), new Vector2D(-width/2,.5), new Color(255,255,255, 150)));
    }

    public void setSpeed(double speed)
    {
        this.speed = Math.abs(speed) <= maxSpeed ? speed : this.speed;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public void expand(double factor) {
        width *= factor;
        if (width > 30) {
            width = 30;
        }
        if (width < 6) {
            width = 6;
        }
        rebuild();
    }

    public void update()
    {
        setPosition(getPosition().add(Vector2D.left().mult(speed * Time.deltaTime)));
    }

    public void setPosition(Vector2D position) {
        if (Math.abs(position.X()) < 36-width/2) {
            super.setPosition(position);
        }
    }

    public void launch() {
        Ball ball = (Ball) findChildByType(Ball.class);
        if (ball != null) {
            ball.setPosition(ball.getPosition());
            remove(ball);
            ((Body) ball.getComponent(Body.class)).setVelocity(Vector2D.left().mult(speed).add(Vector2D.up().mult(40)));
            world.getPhysics().unregister((Collider) ball.getComponent(Collider.class));
            world.add(ball);
        }
    }
}

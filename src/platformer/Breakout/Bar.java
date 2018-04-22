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
import platformer.PhysicsEngine.Colliders.BoxCollider;
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

        // add key listener.
        addComponent(new BarController());
        // add collision handler
        addComponent(new BarCollisionHandler());
    }

    private void rebuild()
    {
        // Cleanup all components
        if (world != null) {
            for (Component component : getComponents(Collider.class)) {
                world.getPhysics().unregister((Collider) component);
            }
        }
        removeComponents(Collider.class);
        removeComponents(Line.class);
        removeComponents(Rectangle.class);

        // Add the collider
        BoxCollider collider = (BoxCollider) addComponent(new BoxCollider(new Dimension(width, 1)));
        if (world != null) {
            world.getPhysics().register(collider);
        }

        // Add drawable components
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
        // Limit the maximum width of the bar.
        if (width > 30) {
            width = 30;
        }
        // Limit the minimum width of the bar.
        if (width < 6) {
            width = 6;
        }
        // Reconstruct the bar.
        rebuild();
    }

    public void update()
    {
        setPosition(getPosition().add(Vector2D.left().mult(speed * Time.deltaTime)));
    }

    public void setPosition(Vector2D position) {
        // Limit the position of the bar, in order to keep it within the screen
        // TODO: Make bar a physics body and apply springs to keep it within the window
        if (Math.abs(position.X()) < 36-width/2) {
            super.setPosition(position);
        }
    }

    public void launch() {
        Ball ball = (Ball) findChildByType(Ball.class);
        // Ensure that there is a ball on the bar.
        if (ball != null) {
            // Update the ball's relative position
            ball.setPosition(ball.getPosition());
            // Detach the ball from the bar
            remove(ball);
            // Set the ball's velocity
            ((Body) ball.getComponent(Body.class)).setVelocity(Vector2D.left().mult(speed).add(Vector2D.up().mult(40)));
            // Remove the ball from the physics engine
            world.getPhysics().unregister((Collider) ball.getComponent(Collider.class));
            // Add the ball as its own gameobject to the world.
            world.add(ball);
        }
    }
}

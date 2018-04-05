package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Circle implements Drawable
{
    private Vector2D center;
    private GameObject gameObject;
    private Color colour = Color.lightGray;
    private double radius;

    public Circle(Vector2D center, double radius, Color colour)
    {
        this.center = center;
        this.radius = radius;
        this.colour = colour;
    }

    public Circle(GameObject gameObject, double radius, Color colour)
    {
        this(gameObject, radius);
        this.colour = colour;
    }

    public Circle(GameObject gameObject, double radius)
    {
        this.gameObject = gameObject;
        this.radius = radius;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        int radius = camera.scale(this.radius);
        Vector2D center = this.gameObject == null ? this.center : gameObject.getPosition();
        g.setColor(colour);
        g.fillOval(
            camera.convertWorldXtoScreenX(center.X())-radius/2,
            camera.convertWorldYtoScreenY(center.Y())-radius/2,
            radius,
            radius
        );
    }
}

package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Circle extends AbstractComponent implements Drawable
{
    private Vector2D center = new Vector2D();
    private Color colour = Color.lightGray;
    private double radius;

    public Circle(Vector2D center, double radius, Color colour)
    {
        this(radius, colour);
        this.center = center;
    }

    public Circle(double radius, Color colour)
    {
        this(radius);
        this.colour = colour;
    }

    public Circle(double radius)
    {
        this.radius = radius;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        int radius = camera.scale(this.radius);
        Vector2D center = this.gameObject == null ? this.center : gameObject.getPosition().add(this.center);
        g.setColor(colour);
        g.fillOval(
            camera.convertWorldXtoScreenX(center.X())-radius/2,
            camera.convertWorldYtoScreenY(center.Y())-radius/2,
            radius,
            radius
        );
    }

    @Override
    public void setColor(Color color) {
        this.colour = color;
    }
}

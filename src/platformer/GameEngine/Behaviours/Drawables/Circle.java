package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;

import java.awt.*;

public class Circle implements Drawable
{
    private GameObject gameObject;
    private Color colour = Color.lightGray;
    private double radius;

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
        g.setColor(colour);
        g.fillOval(
            camera.convertWorldXtoScreenX(this.gameObject.getPosition().X())-radius/2,
            camera.convertWorldYtoScreenY(this.gameObject.getPosition().Y())-radius/2,
            radius,
            radius
        );
    }
}

package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Line extends AbstractComponent implements Drawable
{
    private Color colour = Color.lightGray;
    private Vector2D a, b;

    public Line(Vector2D a, Vector2D b, Color colour)
    {
        this(a, b);
        this.colour = colour;
    }

    public Line(Vector2D a, Vector2D b)
    {
        this.a = a;
        this.b = b;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        g.setColor(colour);
        Vector2D a = gameObject == null ? this.a : gameObject.getPosition().add(this.a.rotate(gameObject.getLocalRotation()));
        Vector2D b = gameObject == null ? this.b : gameObject.getPosition().add(this.b.rotate(gameObject.getLocalRotation()));

        g.drawLine(
            camera.convertWorldXtoScreenX(a.X()),
            camera.convertWorldYtoScreenY(a.Y()),
            camera.convertWorldXtoScreenX(b.X()),
            camera.convertWorldYtoScreenY(b.Y())
        );
    }

    @Override
    public void setColor(Color color) {
        this.colour = color;
    }
}

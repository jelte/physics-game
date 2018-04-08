package platformer.GameEngine.Behaviours.Drawables;

import org.w3c.dom.css.Rect;
import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Rectangle extends AbstractComponent implements Drawable
{
    private Vector2D center = new Vector2D();
    private Color colour = Color.lightGray;
    private Dimension dimension;

    public Rectangle(Vector2D center, Dimension dimension, Color colour)
    {
        this(dimension, colour);
        this.center = center;
    }

    public Rectangle(Dimension dimension, Color colour)
    {
        this(dimension);
        this.colour = colour;
    }

    public Rectangle(Dimension dimension)
    {
        this.dimension = dimension;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        g.setColor(colour);
        Vector2D a = gameObject.getPosition().add(center).add(new Vector2D(-dimension.getWidth()/2, -dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D b = gameObject.getPosition().add(center).add(new Vector2D(dimension.getWidth()/2, -dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D c = gameObject.getPosition().add(center).add(new Vector2D(dimension.getWidth()/2, dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D d = gameObject.getPosition().add(center).add(new Vector2D(-dimension.getWidth()/2, dimension.getHeight()/2).rotate(gameObject.getRotation()));
        g.fillPolygon(
            new int[] {
                camera.convertWorldXtoScreenX(a.X()),
                camera.convertWorldXtoScreenX(b.X()),
                camera.convertWorldXtoScreenX(c.X()),
                camera.convertWorldXtoScreenX(d.X())
            },
            new int[] {
                camera.convertWorldYtoScreenY(a.Y()),
                camera.convertWorldYtoScreenY(b.Y()),
                camera.convertWorldYtoScreenY(c.Y()),
                camera.convertWorldYtoScreenY(d.Y())
            },
            4
        );
    }

    @Override
    public void setColor(Color color) {
        this.colour = color;
    }
}

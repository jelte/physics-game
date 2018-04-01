package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Rectangle implements Drawable
{
    private GameObject gameObject;
    private Color colour = Color.lightGray;
    private Dimension dimension;

    public Rectangle(GameObject gameObject, Dimension dimension, Color colour)
    {
        this(gameObject, dimension);
        this.colour = colour;
    }

    public Rectangle(GameObject gameObject, Dimension dimension)
    {
        this.gameObject = gameObject;
        this.dimension = dimension;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        g.setColor(colour);

        Vector2D a = gameObject.getPosition().add(new Vector2D(-dimension.getWidth()/2, -dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D b = gameObject.getPosition().add(new Vector2D(dimension.getWidth()/2, -dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D c = gameObject.getPosition().add(new Vector2D(dimension.getWidth()/2, dimension.getHeight()/2).rotate(gameObject.getRotation()));
        Vector2D d = gameObject.getPosition().add(new Vector2D(-dimension.getWidth()/2, dimension.getHeight()/2).rotate(gameObject.getRotation()));
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
}

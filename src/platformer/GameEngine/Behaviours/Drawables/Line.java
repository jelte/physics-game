package platformer.GameEngine.Behaviours.Drawables;

import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;

public class Line implements Drawable
{
    private GameObject gameObject;
    private Color colour = Color.lightGray;
    private Vector2D a, b;

    public Line(GameObject gameObject, Vector2D a, Vector2D b, Color colour)
    {
        this(gameObject, a, b);
        this.colour = colour;
    }

    public Line(GameObject gameObject, Vector2D a, Vector2D b)
    {
        this.gameObject = gameObject;
        this.a = a;
        this.b = b;
    }

    public void draw(Graphics2D g, Camera camera)
    {
        g.setColor(colour);
        Vector2D a = this.gameObject.getPosition().add(this.a.rotate(this.gameObject.getLocalRotation()));
        Vector2D b = this.gameObject.getPosition().add(this.b.rotate(this.gameObject.getLocalRotation()));

        g.drawLine(
            camera.convertWorldXtoScreenX(a.X()),
            camera.convertWorldYtoScreenY(a.Y()),
            camera.convertWorldXtoScreenX(b.X()),
            camera.convertWorldYtoScreenY(b.Y())
        );
    }
}

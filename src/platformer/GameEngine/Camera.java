package platformer.GameEngine;

import java.awt.*;

public class Camera
{
    private Vector2D position;
    private Vector2D offset;
    private Dimension viewport;
    private Dimension cameraDimension;
    private int pixelsPerMeter;

    public Camera(int pixelsPerMeter)
    {
        this(new Vector2D(), pixelsPerMeter);
    }

    public Camera(Vector2D position, int pixelsPerMeter) {
        this.position = position;
        this.pixelsPerMeter = pixelsPerMeter;
    }

    public void setViewport(Dimension viewport)
    {
        this.viewport = viewport;
        this.setCameraDimension();
    }

    private void setCameraDimension()
    {
        this.cameraDimension = new Dimension(
    (int)viewport.getWidth() / pixelsPerMeter,
    (int)viewport.getHeight() / pixelsPerMeter
        );
    }

    public int scale(double size)
    {
        return (int) (size * pixelsPerMeter);
    }

    public Dimension scale(Dimension dimension)
    {
        return new Dimension(
            (int) dimension.getWidth() * pixelsPerMeter,
            (int) dimension.getHeight() * pixelsPerMeter
        );
    }

    public int convertWorldXtoScreenX(double worldX) {
        return (int) ((worldX - position.X() + cameraDimension.getWidth()/2)/cameraDimension.getWidth()*viewport.getWidth());
    }

    public int convertWorldYtoScreenY(double worldY) {
        // minus sign in here is because screen coordinates are upside down.
        return (int) (viewport.getHeight()-((worldY - position.Y() + cameraDimension.getHeight()/2)/cameraDimension.getHeight()*viewport.getHeight()));
    }
}

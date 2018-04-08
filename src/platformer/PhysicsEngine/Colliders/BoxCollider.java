package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class BoxCollider extends PolygonCollider
{
    public BoxCollider(Dimension dimension)
    {
        this(dimension, 0.0);
    }

    public BoxCollider(Dimension dimension, double rotation)
    {
        super(Arrays.asList(
                new Vector2D(-dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(-dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation)
        ), rotation);
    }
}

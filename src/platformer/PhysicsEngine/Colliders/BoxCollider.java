package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.awt.*;
import java.util.Arrays;

public class BoxCollider extends PolygonCollider
{

    public BoxCollider(GameObject gameObject, Dimension dimension)
    {
        this(gameObject, dimension, 0.0);
    }

    public BoxCollider(GameObject gameObject, Dimension dimension, double rotation)
    {
        super(gameObject, Arrays.asList(
                new Vector2D(-dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(-dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation),
                new Vector2D(dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation)
        ), rotation);
    }
}

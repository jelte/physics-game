package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static platformer.Game.DEBUG;

public class PolygonCollider extends AbstractCollider implements Collider
{
    private Dimension dimension;
    private double rotation;
    private List<Vector2D> corners;
    private List<Collider> colliders = new ArrayList<>();


    public PolygonCollider(int numberOfCorners, double radius)
    {
        this(numberOfCorners, radius, 0.0);
    }
    public PolygonCollider(int numberOfCorners, double radius, double rotation)
    {
        super(1.0);
        this.corners = new ArrayList<>();
        for ( double i = 0; i < 360; i+= 360/numberOfCorners) {
            corners.add(Vector2D.left().mult(radius).rotate(i));
    }
        this.rotation = rotation;
        initColliders(false);
    }

    public void setGameObject(GameObject gameObject)
    {
        super.setGameObject(gameObject);
        for (Collider c : colliders) {
            c.setGameObject(gameObject);
        }
    }

    public PolygonCollider(List<Vector2D> corners)
    {
        this(corners, false);
    }

    public PolygonCollider(List<Vector2D> corners, boolean invertCollider)
    {
        this(corners, invertCollider, 0.0);
    }

    public PolygonCollider(List<Vector2D> corners, boolean invertCollider, double rotation)
    {
        super(1.0);
        this.corners = corners;
        initColliders(invertCollider);
    }

    private void initColliders(boolean invertCollider)
    {
        for (int i = 0; i < corners.size(); i++) {
            Collider collider = new LineCollider(corners.get(i+1 == corners.size() ? 0 : i + 1), corners.get(i), invertCollider);
            collider.setGameObject(this.gameObject);
            colliders.add(collider);
        }
    }

    public Vector2D getUnitTangent(Vector2D contactPoint) { return getPosition().minus(contactPoint).normalise(); }
    public Vector2D getUnitNormal(Vector2D contanctPoint) { return getUnitTangent(contanctPoint).rotate90degreesAnticlockwise(); }

    @Override
    public List<? extends Vector2D> getCorners() {
        List<Vector2D> corners = new ArrayList<>();
        for (Vector2D corner : this.corners) {
            corners.add(getPosition().add(corner.rotate(gameObject.getRotation())));
        }
        return corners;
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    @Override
    public Vector2D getAP(Vector2D point) {
        return getPosition().minus(point);
    }

    @Override
    public double getLength() {
        return 0;
    }

    @Override
    public boolean isNormalPointsInwards() {
        return false;
    }

    public Collider checkCollision(Vector2D point, Vector2D velocity, double radius, double tolerance)
    {
        for (Collider collider : colliders) {
            if (collider.checkCollision(point, velocity, radius, tolerance) != null) {
                return collider;
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;

        for (Collider collider : colliders) {
            collider.draw(g, camera);
        }
    }
}

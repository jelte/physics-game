package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Collision;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static platformer.Game.DEBUG;

public class PolygonCollider implements Collider
{
    private GameObject gameObject;
    private Dimension dimension;
    private double rotation;
    private List<Vector2D> corners;
    private List<LineCollider> colliders = new ArrayList<>();


    public PolygonCollider(GameObject gameObject, int numberOfCorners, double radius)
    {
        this(gameObject, numberOfCorners, radius, 0.0);
    }
    public PolygonCollider(GameObject gameObject, int numberOfCorners, double radius, double rotation)
    {
        this.gameObject = gameObject;
        this.corners = new ArrayList<>();
        for ( double i = 0; i < 360; i+= 360/numberOfCorners) {
            corners.add(Vector2D.left().mult(radius).rotate(i));
        }
        this.rotation = rotation;
        initColliders();
    }

    public PolygonCollider(GameObject gameObject, List<Vector2D> corners)
    {
        this(gameObject, corners, 0.0);
    }

    public PolygonCollider(GameObject gameObject, List<Vector2D> corners, double rotation)
    {
        this.gameObject = gameObject;
        this.corners = corners;
        initColliders();
    }

    private void initColliders()
    {
        for (int i = 0; i < corners.size(); i++) {
            colliders.add(new LineCollider(gameObject, corners.get(i), corners.get(i+1 == corners.size() ? 0 : i + 1)));
        }
    }


    public Vector2D getUnitTangent(Vector2D contactPoint) { return getPosition().minus(contactPoint).normalise(); }

    @Override
    public List<? extends Vector2D> getCorners() {
        List<Vector2D> corners = new ArrayList<>();
        for (Vector2D corner : this.corners) {
            corners.add(getPosition().add(corner.rotate(gameObject.getRotation())));
        }
        return corners;
    }

    @Override
    public Vector2D getAP(Vector2D point) {
        return null;
    }

    @Override
    public double getLength() {
        return 0;
    }

    public Vector2D getUnitNormal(Vector2D contanctPoint) { return getUnitTangent(contanctPoint).rotate90degreesAnticlockwise(); }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    @Override
    public boolean isNormalPointsInwards() {
        return true;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;

        g.setColor(Color.orange);

        int[] x = new int[corners.size()+1];
        int[] y = new int[corners.size()+1];

        int i = 0;
        for (Vector2D corner : corners) {
            Vector2D a = gameObject.getPosition().add(corner.rotate(gameObject.getRotation()));
            x[i] = camera.convertWorldXtoScreenX(a.X());
            y[i++] = camera.convertWorldYtoScreenY(a.Y());
        }
        Vector2D a = gameObject.getPosition().add(corners.get(0).rotate(gameObject.getRotation()));
        x[i] = camera.convertWorldXtoScreenX(a.X());
        y[i] = camera.convertWorldYtoScreenY(a.Y());
        g.drawPolyline(x, y, corners.size()+1);
    }
}

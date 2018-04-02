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
    private List<Vector2D> corners;
    private List<LineCollider> colliders = new ArrayList<>();

    public PolygonCollider(GameObject gameObject, List<Vector2D> corners)
    {
        this(gameObject, corners, 0.0);
    }

    public PolygonCollider(GameObject gameObject, List<Vector2D> corners, double rotation)
    {
        this.gameObject = gameObject;
        this.corners = corners;
        for (int i = 0; i < corners.size(); i++) {
            colliders.add(new LineCollider(gameObject, corners.get(i), corners.get(i+1 == corners.size() ? 0 : i + 1), false));
        }
    }

    @Override
    public Collision collide(Collider other) {
        List<Vector2D> contactPoints = new ArrayList<>();
        for (Vector2D corner : corners) {
            Vector2D pointOfCollision = getPosition().add(corner.rotate(gameObject.getRotation()));
            if (other.collidesAt(pointOfCollision)) {
                contactPoints.add(pointOfCollision);
            }
        }
        if (contactPoints.size() > 0) {
            return new Collision(this, other, contactPoints);
        }
        return null;
    }

    @Override
    public boolean collidesAt(Vector2D point) {
        for (Collider collider : colliders) {
            if (collider.collidesAt(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    @Override
    public boolean isNormalPointsInwards() {
        return true;
    }

    @Override
    public Vector2D calculateVelocityAfterACollision(Vector2D pos, Vector2D vel) {
        return vel;
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

package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Collision;

import java.awt.*;

import static platformer.Game.DEBUG;

public class BoxCollider implements Collider
{
    private GameObject gameObject;
    private Dimension dimension;
    private Vector2D[] corners = new Vector2D[4];
    private LineCollider[] colliders = new LineCollider[4];

    public BoxCollider(GameObject gameObject, Dimension dimension)
    {
        this(gameObject, dimension, 0.0);
    }

    public BoxCollider(GameObject gameObject, Dimension dimension, double rotation)
    {
        this.gameObject = gameObject;
        this.dimension = dimension;
        corners[0] = new Vector2D(-dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation);
        corners[1] = new Vector2D(-dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation);
        corners[2] = new Vector2D(dimension.getWidth() / 2, dimension.getHeight() / 2).rotate(rotation);
        corners[3] = new Vector2D(dimension.getWidth() / 2, -dimension.getHeight() / 2).rotate(rotation);
        colliders[0] = new LineCollider(gameObject, corners[0], corners[1]);
        colliders[1] = new LineCollider(gameObject, corners[1], corners[2]);
        colliders[2] = new LineCollider(gameObject, corners[2], corners[3]);
        colliders[3] = new LineCollider(gameObject, corners[3], corners[0]);
    }

    @Override
    public Collision collide(Collider other) {

        for (Vector2D corner : corners) {
            Vector2D pointOfCollision = gameObject.getPosition().add(corner);
            if (other.collidesAt(pointOfCollision)) {
                return new Collision(this, other, pointOfCollision);
            }
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

        Vector2D a = gameObject.getPosition().add(corners[0].rotate(gameObject.getRotation()));
        Vector2D b = gameObject.getPosition().add(corners[1].rotate(gameObject.getRotation()));
        Vector2D c = gameObject.getPosition().add(corners[2].rotate(gameObject.getRotation()));
        Vector2D d = gameObject.getPosition().add(corners[3].rotate(gameObject.getRotation()));
        g.drawPolyline(
                new int[] {
                        camera.convertWorldXtoScreenX(a.X()),
                        camera.convertWorldXtoScreenX(b.X()),
                        camera.convertWorldXtoScreenX(c.X()),
                        camera.convertWorldXtoScreenX(d.X()),
                        camera.convertWorldXtoScreenX(a.X())
                },
                new int[] {
                        camera.convertWorldYtoScreenY(a.Y()),
                        camera.convertWorldYtoScreenY(b.Y()),
                        camera.convertWorldYtoScreenY(c.Y()),
                        camera.convertWorldYtoScreenY(d.Y()),
                        camera.convertWorldYtoScreenY(a.Y())
                },
                5
        );
    }
}

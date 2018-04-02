package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Collision;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static platformer.Game.DEBUG;

public class LineCollider implements Collider
{
    private final GameObject gameObject;
    private Vector2D a, b;
    private double barrierLength;
    private double accuracy = 0.5;


    public LineCollider(GameObject gameObject, Vector2D a, Vector2D b)
    {
        this(gameObject, a, b, false);
    }
    public LineCollider(GameObject gameObject, Vector2D a, Vector2D b, boolean invert)
    {
        this.gameObject = gameObject;
        this.a = invert ? b : a;
        this.b = invert ? a : b;
        barrierLength = this.b.minus(this.a).mag();
    }

    @Override
    public Collision collide(Collider other) {

        List<Vector2D> contactPoints = new ArrayList<>();
        if (other.collidesAt(getA())) {
            contactPoints.add(getA());
        }
        if (other.collidesAt(getB())) {
            contactPoints.add(getB());
        }
        if (contactPoints.size() > 0) {
            return new Collision(this, other, contactPoints);
        }
        return null;
    }

    @Override
    public boolean collidesAt(Vector2D point)
    {
        Vector2D ap = getA().minus(point);
        double distOnCorrectSideOfBarrierToCentre = ap.scalarProduct(getUnitNormal());
        double distAlongBarrier = ap.scalarProduct(getUnitTangent());

        // barrierLength is ||AB||, declared in constructor.
        return distOnCorrectSideOfBarrierToCentre<=accuracy
                && distOnCorrectSideOfBarrierToCentre>=-accuracy
                && distAlongBarrier >= 0
                && distAlongBarrier <= barrierLength;
    }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    @Override
    public boolean isNormalPointsInwards() {
        return false;
    }

    public Vector2D getUnitTangent() { return getA().minus(getB()).normalise(); }
    public Vector2D getUnitNormal() { return getUnitTangent().rotate90degreesAnticlockwise(); }
    private Vector2D getA() { return getPosition().add(a.rotate(gameObject.getRotation())); }
    private Vector2D getB() { return getPosition().add(b.rotate(gameObject.getRotation())); }

    @Override
    public Vector2D calculateVelocityAfterACollision(Vector2D pos, Vector2D vel) {
        double vParallel=vel.scalarProduct(getUnitTangent());
        double vNormal=vel.scalarProduct(getUnitNormal());
        if (vNormal>0) // assumes normal points AWAY from wall...
            vNormal=-vNormal;
        Vector2D result=getUnitTangent().mult(vParallel);
        return result.addScaled(getUnitNormal(), vNormal);
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;

        new Line(gameObject, a, b, Color.ORANGE).draw(g, camera);
    }
}

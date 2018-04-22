package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.*;
import platformer.GameEngine.Behaviours.Drawables.Circle;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.PhysicsEngine.Collider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static platformer.Game.DEBUG;

public class LineCollider extends AbstractCollider implements Collider
{
    private Vector2D a, b;
    private double barrierLength;
    private double accuracy = 0.5;


    public LineCollider(Vector2D a, Vector2D b)
    {
        this(a, b, false);
    }
    public LineCollider(Vector2D a, Vector2D b, boolean invert)
    {
        super(1.0);
        this.a = invert ? b : a;
        this.b = invert ? a : b;
        barrierLength = this.b.minus(this.a).mag();
    }
    @Override
    public boolean isNormalPointsInwards() {
        return false;
    }

    @Override
    public List<? extends Vector2D> getCorners() {
        List<Vector2D> corners = new ArrayList<>();
        corners.add(getA());
        corners.add(getB());
        return corners;
    }

    @Override
    public Vector2D getAP(Vector2D point) {
        return point.minus(getA());
    }

    @Override
    public double getLength() {
        return barrierLength;
    }

    public Vector2D getUnitTangent(Vector2D contactPoint) { return getB().minus(getA()).normalise(); }
    public Vector2D getUnitNormal(Vector2D contanctPoint) { return getUnitTangent(getB()).rotate90degreesAnticlockwise(); }
    private Vector2D getA() { return getPosition().add(a.rotate(gameObject.getRotation())); }
    private Vector2D getB() { return getPosition().add(b.rotate(gameObject.getRotation())); }

    public Collider checkCollision(Vector2D point, Vector2D velocity, double radius, double tolerance)
    {
        Vector2D n = getUnitNormal(point);
        Vector2D ap = getAP(point);
        double dist = ap.scalarProduct(n) - radius;
        if (dist <= -tolerance || dist > -0.1+tolerance) {
            return null;
        }
        Vector2D t = getUnitTangent(point);
        double proj = ap.scalarProduct(t) - radius;
        double length = getLength() + radius;
        if (proj < 0 || proj > length) {
            return null;
        }
        return velocity.scalarProduct(n) < 0 ? this : null;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;

        new Line(getA(), getB(), Color.ORANGE).draw(g, camera);
        Vector2D center = getA().add(getB()).mult(0.5);
        new Line(center, center.add(getUnitNormal(b)), Color.GREEN).draw(g, camera);
        new Line(center, center.add(getUnitTangent(b)), Color.RED).draw(g, camera);
        new Circle(getA(), .4, Color.BLUE).draw(g, camera);
        new Circle(getB(), .4, Color.PINK).draw(g, camera);
    }
}

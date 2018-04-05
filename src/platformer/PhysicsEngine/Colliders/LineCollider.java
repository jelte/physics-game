package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Behaviours.Drawables.Circle;
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
    public Vector2D getPosition() {
        return gameObject.getPosition();
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

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;


        new Line(gameObject, a, b, Color.ORANGE).draw(g, camera);
        Vector2D center = a.add(b).mult(0.5);
        new Line(gameObject, center, center.add(getUnitNormal(b)), Color.GREEN).draw(g, camera);
        new Line(gameObject, center, center.add(getUnitTangent(b)), Color.RED).draw(g, camera);
        new Circle(getA(), .4, Color.BLUE).draw(g, camera);
        new Circle(getB(), .4, Color.PINK).draw(g, camera);
    }
}

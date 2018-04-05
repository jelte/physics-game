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
import java.util.Vector;

import static platformer.Game.DEBUG;


public class CurveCollider implements Collider
{
    private final GameObject gameObject;
    private final Vector2D center;
    private final Double barrierDepth;
    private final double deltaAngle;
    private final double startAngle;
    private final double radiusOfBarrier, diameter;
    private final boolean normalPointsInwards;
    private final double accuracy = 0.25;

    public CurveCollider(GameObject gameObject, double radiusOfBarrier, boolean normalPointsInwards)
    {
        this(gameObject, -90, 360, radiusOfBarrier, normalPointsInwards);
    }

    public CurveCollider(GameObject gameObject,  double startAngle, double deltaAngle, double radiusOfBarrier, boolean normalPointsInwards) {
        this(gameObject, startAngle, deltaAngle, new Vector2D(), radiusOfBarrier, normalPointsInwards);
    }


    public CurveCollider(GameObject gameObject, double startAngle, double deltaAngle, Vector2D center, double radiusOfBarrier, boolean normalPointsInwards)
    {
        this.gameObject = gameObject;
        this.center =center;
        this.barrierDepth = 0.1;
        this.deltaAngle = deltaAngle;
        this.startAngle = startAngle;
        this.diameter = radiusOfBarrier;
        this.radiusOfBarrier = radiusOfBarrier / 2;
        this.normalPointsInwards = normalPointsInwards;
    }

    public CurveCollider(GameObject gameObject, Vector2D center, int radiusOfBarrier, boolean normalPointsInwards) {
        this(gameObject, -90, 360, center, radiusOfBarrier, normalPointsInwards);
    }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition().add(center.rotate(gameObject.getRotation()));
    }

    @Override
    public boolean isNormalPointsInwards() {
        return normalPointsInwards;
    }

    public Vector2D getUnitNormal(Vector2D contactPoint)
    {
        Vector2D normal = getPosition().minus(contactPoint).normalise();
        if (normalPointsInwards) normal=normal.mult(-1);
        return normal;
    }

    public Vector2D getUnitTangent(Vector2D contactPoint)
    {
        return getUnitNormal(contactPoint).rotate90degreesAnticlockwise().normalise();
    }

    @Override
    public List<? extends Vector2D> getCorners() {
        return new ArrayList<>();
    }

    @Override
    public Vector2D getAP(Vector2D point) {
        return point.minus(getPosition());
    }

    @Override
    public double getLength() {
        return radiusOfBarrier;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;
        int radius = camera.scale(this.diameter);
        g.setColor(Color.orange);
        g.drawOval(
            camera.convertWorldXtoScreenX(getPosition().X())-radius/2,
            camera.convertWorldYtoScreenY(getPosition().Y())-radius/2,
            radius,
            radius
        );
    }
}

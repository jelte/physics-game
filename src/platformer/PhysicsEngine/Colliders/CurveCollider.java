package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Camera;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static platformer.Game.DEBUG;


public class CurveCollider extends AbstractCollider implements Collider
{
    private final Vector2D center;
    private final double deltaAngle, startAngle;
    private final double radiusOfBarrier, diameter;
    private final boolean normalPointsInwards;

    public CurveCollider(double radiusOfBarrier, boolean normalPointsInwards)
    {
        this(-90, 360, radiusOfBarrier, normalPointsInwards);
    }

    public CurveCollider(double startAngle, double deltaAngle, double radiusOfBarrier, boolean normalPointsInwards) {
        this(startAngle, deltaAngle, new Vector2D(), radiusOfBarrier, normalPointsInwards);
    }


    public CurveCollider(double startAngle, double deltaAngle, Vector2D center, double radiusOfBarrier, boolean normalPointsInwards)
    {
        super(1.0);
        this.center =center;
        this.deltaAngle = deltaAngle;
        this.startAngle = startAngle;
        this.diameter = radiusOfBarrier;
        this.radiusOfBarrier = radiusOfBarrier / 2;
        this.normalPointsInwards = normalPointsInwards;
    }

    public CurveCollider(Vector2D center, int radiusOfBarrier, boolean normalPointsInwards) {
        this(-90, 360, center, radiusOfBarrier, normalPointsInwards);
    }

    @Override
    public Vector2D getPosition() { return gameObject.getPosition().add(center.rotate(gameObject.getRotation())); }

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
        List<Vector2D> corners = new ArrayList<>();
        for (double i = startAngle; i < startAngle+deltaAngle; i+=30) {
            corners.add(Vector2D.down().mult(radiusOfBarrier).rotate(i));
        }
        return corners;
    }

    @Override
    public Vector2D getAP(Vector2D point) {
        return point.minus(getPosition());
    }

    @Override
    public double getLength() {
        return radiusOfBarrier;
    }

    public boolean checkCollision(Vector2D point, Vector2D velocity, double radius, double tolerance)
    {
        Vector2D ap = getAP(point);
        double ang = (Math.toDegrees(ap.angle()) + 360) % 360;	// convert from radians to degrees, remove any negative angles to avoid confusion

        double startAngle = gameObject.getRotation() + this.startAngle;
        double finalAngle = startAngle + deltaAngle;

        if (deltaAngle < 0 && !((ang >= finalAngle && ang <= startAngle) || (ang >= finalAngle+360 && ang <= startAngle+360)))
            return false;

        if (deltaAngle >= 0 && !((ang <= finalAngle && ang >= startAngle) || (ang <= finalAngle+360 && ang >= startAngle+360)))
            return false;

        double distToCentreOfBarrierArc = ap.mag();

        return distToCentreOfBarrierArc + radius >= radiusOfBarrier - tolerance
                && distToCentreOfBarrierArc - radius <= radiusOfBarrier + tolerance;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;
        int radius = camera.scale(this.diameter);
        g.setColor(Color.orange);
        g.drawArc(
            camera.convertWorldXtoScreenX(getPosition().X())-radius/2,
            camera.convertWorldYtoScreenY(getPosition().Y())-radius/2,
            radius,
            radius,
            (int) startAngle,
            (int) deltaAngle
        );
    }
}

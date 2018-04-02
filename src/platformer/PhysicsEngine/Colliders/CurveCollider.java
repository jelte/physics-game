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
    public Collision collide(Collider other)
    {
        List<Vector2D> contactPoints = new ArrayList<>();
        if (other instanceof CurveCollider) {
            if (other.collidesAt(getPosition())) {
                contactPoints.add(getPosition());
            }
        } else {
            Vector2D radius = new Vector2D(radiusOfBarrier, 0).rotate(startAngle);
            for (int i = 0; i < deltaAngle; i += 5) {
                Vector2D startPoint = getPosition().add(radius.rotate(i));
                if (other.collidesAt(startPoint)) {
                    contactPoints.add(startPoint);
                }
            }
        }
        if (contactPoints.size() > 0) {
            return new Collision(this, other, contactPoints);
        }
        return null;
    }

    @Override
    public boolean collidesAt(Vector2D point) {
        Vector2D ap = point.minus(getPosition());
        double ang=ap.angle(); // relies on Math.atan2 function
        ang=ang*180/Math.PI; //convert from radians to degrees
        ang=(ang+360)%360;	// remove any negative angles to avoid confusion
        boolean withinAngleRange=false;
        if (deltaAngle<0 && ((ang>=startAngle+deltaAngle && ang<=startAngle) ||(ang>=startAngle+deltaAngle+360 && ang<=startAngle+360)))
            withinAngleRange=true;
        if (deltaAngle>=0 && ((ang<=startAngle+deltaAngle && ang>=startAngle) ||(ang<=startAngle+deltaAngle+360 && ang>=startAngle+360)))
            withinAngleRange=true;
        double distToCentreOfBarrierArc = ap.mag();
        boolean withinDistanceRange=(normalPointsInwards && distToCentreOfBarrierArc+accuracy>=this.radiusOfBarrier && distToCentreOfBarrierArc-accuracy<=this.radiusOfBarrier+(barrierDepth!=null?barrierDepth:0))
                || (!normalPointsInwards && distToCentreOfBarrierArc-accuracy<=this.radiusOfBarrier && distToCentreOfBarrierArc+accuracy>=this.radiusOfBarrier-(barrierDepth!=null?barrierDepth:0));
        return withinDistanceRange && withinAngleRange;
    }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition().add(center.rotate(gameObject.getRotation()));
    }

    @Override
    public boolean isNormalPointsInwards() {
        return normalPointsInwards;
    }


    @Override
    public Vector2D calculateVelocityAfterACollision(Vector2D pos, Vector2D vel) {
        Vector2D normal=getPosition().minus(pos).normalise();
        if (normalPointsInwards) normal=normal.mult(-1);
        Vector2D tangent=normal.rotate90degreesAnticlockwise().normalise();
        double vParallel=vel.scalarProduct(tangent);
        double vNormal=vel.scalarProduct(normal);
        if (vNormal < 0) // assumes normal points AWAY from barrierPoint...
            vNormal = -vNormal;
        Vector2D result=tangent.mult(vParallel);
        return result.addScaled(normal, vNormal);
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

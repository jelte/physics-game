package platformer.PhysicsEngine.Colliders;

import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Camera;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Collision;

import java.awt.*;

import static platformer.Game.DEBUG;


public class CurveCollider implements Collider
{
    private final GameObject gameObject;
    private final Double barrierDepth;
    private final double deltaAngle;
    private final double startAngle;
    private final double radiusOfBarrier;
    private final boolean normalPointsInwards;
    private final double accuracy = 0.1;

    public CurveCollider(GameObject gameObject, double radiusOfBarrier, boolean normalPointsInwards)
    {
        this(gameObject, 0, 360, radiusOfBarrier, normalPointsInwards);
    }

    public CurveCollider(GameObject gameObject, double startAngle, double deltaAngle, double radiusOfBarrier, boolean normalPointsInwards)
    {
        this.gameObject = gameObject;
        this.barrierDepth = null;
        this.deltaAngle = deltaAngle;
        this.startAngle = startAngle;
        this.radiusOfBarrier = radiusOfBarrier;
        this.normalPointsInwards = normalPointsInwards;
    }

    @Override
    public Collision collide(Collider other) {

        if (other.collidesAt(gameObject.getPosition())) {
            return new Collision(this, other, gameObject.getPosition());
        }
        /*
        Vector2D radius = new Vector2D(radiusOfBarrier, 0).rotate(startAngle);
        Vector2D startPoint = gameObject.getPosition().add(radius);

        Vector2D endPoint = gameObject.getPosition().add(radius.rotate(deltaAngle));
        if (other.collidesAt(endPoint)) {
            return new Collision(this, other, endPoint);
        }*/
        return null;
    }

    @Override
    public boolean collidesAt(Vector2D point) {
        Vector2D ap = point.minus(gameObject.getPosition());
        double ang=ap.angle(); // relies on Math.atan2 function
        ang=ang*180/Math.PI; //convert from radians to degrees
        ang=(ang+360)%360;	// remove any negative angles to avoid confusion
        boolean withinAngleRange=false;
        if (deltaAngle<0 && ((ang>=startAngle+deltaAngle && ang<=startAngle) ||(ang>=startAngle+deltaAngle+360 && ang<=startAngle+360)))
            withinAngleRange=true;
        if (deltaAngle>=0 && ((ang<=startAngle+deltaAngle && ang>=startAngle) ||(ang<=startAngle+deltaAngle+360 && ang>=startAngle+360)))
            withinAngleRange=true;
        double distToCentreOfBarrierArc=ap.mag();
        boolean withinDistanceRange=(normalPointsInwards && distToCentreOfBarrierArc+accuracy>=this.radiusOfBarrier && distToCentreOfBarrierArc-accuracy<=this.radiusOfBarrier+(barrierDepth!=null?barrierDepth:0))
                || (!normalPointsInwards && distToCentreOfBarrierArc-accuracy<=this.radiusOfBarrier && distToCentreOfBarrierArc+accuracy>=this.radiusOfBarrier-(barrierDepth!=null?barrierDepth:0));
        return withinDistanceRange && withinAngleRange;
    }

    @Override
    public Vector2D getPosition() {
        return gameObject.getPosition();
    }

    @Override
    public boolean isNormalPointsInwards() {
        return normalPointsInwards;
    }


    @Override
    public Vector2D calculateVelocityAfterACollision(Vector2D pos, Vector2D vel) {
        Vector2D normal=pos.minus(getPosition()).normalise();
        if (normalPointsInwards) normal=normal.mult(-1);
        Vector2D tangent=normal.rotate90degreesAnticlockwise().normalise();
        double vParallel=vel.scalarProduct(tangent);
        double vNormal=vel.scalarProduct(normal);
        if (vNormal<0) // assumes normal points AWAY from barrierPoint...
            vNormal=-vNormal;
        Vector2D result=tangent.mult(vParallel);
        return result.addScaled(normal, vNormal);
    }


    @Override
    public void draw(Graphics2D g, Camera camera) {
        if (!DEBUG) return;
        int radius = camera.scale(this.radiusOfBarrier);
        g.setColor(Color.orange);
        g.drawOval(
                camera.convertWorldXtoScreenX(this.gameObject.getPosition().X())-radius/2,
                camera.convertWorldYtoScreenY(this.gameObject.getPosition().Y())-radius/2,
                radius,
                radius
        );
    }
}

package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;
import java.util.ArrayList;
import java.util.List;

public class Physics {
    public static final Vector2D GRAVITY = new Vector2D(0, -9.8);
    public static final boolean USE_IMPROVED_EULER = true;
    public static final double WORLD_MASS = 10000000;

    public static final int NUM_EULER_UPDATES_PER_SCREEN_REFRESH = 10;
    public static final double DELTA_T = 20 / 2000.0 / NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
    private List<Body> bodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();
    private List<ElasticConnector> connectors = new ArrayList<>();

    private Thread thread;

    public void activate() {
        startThread(this);
    }

    private void startThread(final Physics physics) {
        thread = new Thread(() -> {
            // this while loop will exit any time this method is called for a second time, because
            while (thread == Thread.currentThread()) {
                try {
                    physics.update();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                } catch (Exception e) {
                    System.out.println("Physics Error: "+ e.getMessage());
                }
            }
        });// this will cause any old threads running to self-terminate
        thread.start();
    }

    public void register(Body body) {
        this.bodies.add(body);
    }

    public void register(Collider collider) {
        this.colliders.add(collider);
    }

    public void update() {
        // reset to zero at start of time step, so accumulation of forces can begin.
        for (Body body : bodies) {
            body.resetTotalForce();
            if (body.getPosition().Y() >= 18) {
                return;
            }
        }

        for (ElasticConnector ec : connectors) {
            ec.applyTensionForceToBothParticles();
        }

        // tell each body to move
        for (Body body : bodies) {
            updateBody(body);
        }

        double e = .9; // coefficient of restitution for all particle pairs
        for (int n = 0; n < bodies.size(); n++) {
            Body body = bodies.get(n);
            for (Collider collider : colliders) {
                Collision collision = worldCollisionDetection(body, collider);
                if (collision.isColliding()) {
                    worldCollisionForce(body, collider, collision.getContactPoints(), e);
                }
            }
            /*for (int m = 0; m < n; m++) {// avoids double check by requiring m<n
                Body body2 = bodies.get(m);
                Collision collision = body.collidesWith(body2);
                if (collision != null) {
                    implementElasticCollision(body, body2, collision.getContactPoints(), e);
                }
            }*/
        }

    }

    public void updateBody(Body body) {
        Vector2D acceleration = body.getAcceleration();
       //body.applyRotation(DELTA_T);
        if (USE_IMPROVED_EULER) {
            //Vector2D pos2= body.getPosition().addScaled(body.getVelocity(), DELTA_T);// in theory this could be used,e.g. if acc2 depends on pos - but in this constant gravity field it will not be relevant
            Vector2D vel2 = body.getVelocity().addScaled(acceleration, DELTA_T);
            Vector2D velAv = vel2.add(body.getVelocity()).mult(0.5);
            Vector2D acc2 = new Vector2D(acceleration);//assuming acceleration is constant
            // Note acceleration is NOT CONSTANT for distance dependent forces such as
            // Hooke's law or newton's law of gravity, so this is BUG
            // in this Improved Euler implementation.
            // The whole program structure needs changing to fix this problem properly!
            Vector2D accAv = acc2.add(acceleration).mult(0.5);
            body.setPosition(body.getPosition().addScaled(velAv, DELTA_T));
            body.setVelocity(body.getVelocity().addScaled(accAv, DELTA_T));
            body.setOrientation(body.getOrientation() + body.getAngularVelocity());
        } else {
            // basic Euler
            body.setPosition(body.getPosition().addScaled(body.getVelocity(), DELTA_T));
            body.setVelocity(body.getVelocity().addScaled(acceleration, DELTA_T));
        }
    }

    private Collision worldCollisionDetection(Body body, Collider c)
    {
        List<Vector2D> contactPoints = new ArrayList<>();
        for (Vector2D point : body.getCorners()) {
            Vector2D n = c.getUnitNormal(point);
            Vector2D t = c.getUnitTangent(point);
            Vector2D ap = c.getAP(point);
            Vector2D r = point.minus(body.getPosition());
            double length = c.getLength();
            double dist = ap.scalarProduct(n);
            double proj = ap.scalarProduct(t);
            double v = body.getVelocity().add(r.mult(body.getAngularVelocity())).scalarProduct(n);
            if (dist <= 0 && proj >= 0 && proj <= length && v < 0) {
                contactPoints.add(point);
            }
        }
        return new Collision(body, c, contactPoints);
    }

    public void worldCollisionForce(Body body, Collider c, List<Vector2D> contactPoints, double e)
    {
      //  System.out.println(c);
        double mass = body.getMass();
        double i = body.getMomentOfInertia();
        Vector2D centerContactPoint = new Vector2D();
        for (Vector2D contactPoint : contactPoints) {
            centerContactPoint = centerContactPoint.add(contactPoint);
        }
        centerContactPoint = centerContactPoint.mult(1.0/contactPoints.size());

        Vector2D n = c.getUnitNormal(centerContactPoint);
        Vector2D r = centerContactPoint.minus(body.getPosition());
        Vector2D v = body.getVelocity();
        double w = body.getAngularVelocity();

        Vector2D vap = v.add(r.mult(w));
        double j = (-(e + 1.0) * (vap.scalarProduct(n))) / ((1/mass)+(Math.pow(r.scalarProduct(n), 2)/i));

        Vector2D v2 = v.add(n.mult(j/mass));

        double w2 = w + r.scalarProduct(n.mult(j))/i;

        body.setVelocity(v2);
        body.setAngularVelocity(w2);
    }

    private void implementElasticCollision(Body b1, Body b2, List<Vector2D> contactPoints, double e) {
        Vector2D vec1to2 = b1.getPosition().minus(b2.getPosition()).normalise();
        Vector2D tangentDirection = new Vector2D(vec1to2.Y(), -vec1to2.X());
        double v1n=b1.getVelocity().scalarProduct(vec1to2);
        double v2n=b2.getVelocity().scalarProduct(vec1to2);
        double v1t=b1.getVelocity().scalarProduct(tangentDirection);
        double v2t=b2.getVelocity().scalarProduct(tangentDirection);
        double approachSpeed=v2n-v1n;
        double j=b1.getMass()*b2.getMass()*(1+e)*-approachSpeed/(b1.getMass()+b2.getMass());
        for (Vector2D contactPoint : contactPoints) {
         //   b1.applyForce(vec1to2.mult(-j/b1.getMass()), contactPoint);
//            b2.applyForce(vec1to2.mult(j/b2.getMass()), contactPoint);
        }
        b1.setVelocity(b1.getVelocity().addScaled(vec1to2, -j/b1.getMass()));
        b2.setVelocity(b2.getVelocity().addScaled(vec1to2, j/b2.getMass()));
    }
}

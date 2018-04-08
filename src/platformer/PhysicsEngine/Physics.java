package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;

import java.util.*;

public class Physics {
    public static final Vector2D GRAVITY = new Vector2D(0, -9.8);
    public static final boolean USE_IMPROVED_EULER = true;
    public static final double WORLD_MASS = 10000000;

    private List<Body> bodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();
    private List<ElasticConnector> connectors = new ArrayList<>();

    private List<Collider> removedColliders = new ArrayList<>();
    private List<Body> removedBodies = new ArrayList<>();

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
        Time.update();
        cleanUp();

        // reset to zero at start of time step, so accumulation of forces can begin.
        for (Body body : bodies) {
            body.resetTotalForce();
        }

        for (ElasticConnector ec : connectors) {
            ec.applyTensionForceToBothParticles();
        }

        double e = 1.0; // coefficient of restitution for all particle pairs
        for (int n = 0; n < bodies.size(); n++) {
            Body body = bodies.get(n);
            for (Collider collider : colliders) {
                Collision collision = worldCollisionDetection(body, collider);
                if (collision != null) {
                    worldCollisionForce(body, collider, collision.getContactPoints(), e);
                    collider.onCollision(collision);
                }
            }
            for (int m = 0; m < n; m++) {// avoids double check by requiring m<n
                Body body2 = bodies.get(m);
                Collision collision = bodyCollisinoDetection(body, body2);
                if (collision != null) {
                    implementElasticCollision(body, body2, collision.getContactPoints(), e);
                }
            }
        }

        // tell each body to move
        for (Body body : bodies) {
            updateBody(body);
        }
    }

    public void updateBody(Body body) {
        Vector2D acceleration = body.getAcceleration();
       //body.applyRotation(DELTA_T);
        if (USE_IMPROVED_EULER) {
            //Vector2D pos2= body.getPosition().addScaled(body.getVelocity(), DELTA_T);// in theory this could be used,e.g. if acc2 depends on pos - but in this constant gravity field it will not be relevant
            Vector2D vel2 = body.getVelocity().addScaled(acceleration, Time.deltaTime);
            Vector2D velAv = vel2.add(body.getVelocity()).mult(0.5);
            Vector2D acc2 = new Vector2D(acceleration);//assuming acceleration is constant
            // Note acceleration is NOT CONSTANT for distance dependent forces such as
            // Hooke's law or newton's law of gravity, so this is BUG
            // in this Improved Euler implementation.
            // The whole program structure needs changing to fix this problem properly!
            Vector2D accAv = acc2.add(acceleration).mult(0.5);
            body.setPosition(body.getPosition().addScaled(velAv, Time.deltaTime));
            body.setVelocity(body.getVelocity().addScaled(accAv, Time.deltaTime));
            body.setOrientation(body.getOrientation() + body.getAngularVelocity() * Time.deltaTime);
        } else {
            // basic Euler
            body.setPosition(body.getPosition().addScaled(body.getVelocity(), Time.deltaTime));
            body.setVelocity(body.getVelocity().addScaled(acceleration, Time.deltaTime));
        }
    }

    private Collision worldCollisionDetection(Body body, Collider c)
    {
        List<Vector2D> contactPoints = new ArrayList<>();
        for (Vector2D point : body.getCorners()) {
            Vector2D r = point.minus(body.getPosition());
            Vector2D v = body.getVelocity().add(r.mult(body.getAngularVelocity()));
            if (checkCollision(point, v, 0, c, 0.1)) {
                contactPoints.add(point);
            }
        }
        if (contactPoints.size() == 0) {
            return null;
        }
        return new Collision(body, c, contactPoints);
    }

    private boolean checkCollision(Vector2D point, Vector2D velocity, double radius, Collider c, double tolerance)
    {
        Vector2D n = c.getUnitNormal(point);
        Vector2D ap = c.getAP(point);
        double dist = ap.scalarProduct(n) - radius;
        if (dist <= -tolerance || dist > -0.1+tolerance) {
            return false;
        }
        Vector2D t = c.getUnitTangent(point);
        double proj = ap.scalarProduct(t) - radius;
        double length = c.getLength() + radius;
        if (proj < 0 || proj > length) {
            return false;
        }
        double v = velocity.scalarProduct(n);
        if (v >= 0) {
            return false;
        }
        return true;
    }

    public void worldCollisionForce(Body body, Collider c, List<Vector2D> contactPoints, double e)
    {
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

        body.setVelocity(v.add(n.mult(j/mass)));
        body.setAngularVelocity(w + r.scalarProduct(n.mult(j))/i);
    }

    private Collision bodyCollisinoDetection(Body body, Body body2) {
        for (Collider c : body2.getColliders()) {
            Collision collision = worldCollisionDetection(body, c);
            if (collision != null) {
                return collision;
            }
        }
        return null;
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
        b1.setVelocity(b1.getVelocity().addScaled(vec1to2, -j/b1.getMass()));
        b2.setVelocity(b2.getVelocity().addScaled(vec1to2, j/b2.getMass()));
    }

    public void unregister(Body b) { this.removedBodies.add(b); }
    public void unregister(Collider c) { this.removedColliders.add(c); }

    private void cleanUp()
    {
        colliders.removeAll(removedColliders);
        removedColliders.clear();
        bodies.removeAll(removedBodies);
        removedBodies.clear();
    }
}

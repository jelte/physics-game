package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;

import java.util.*;

public class Physics {
    public static final Vector2D GRAVITY = new Vector2D(0, -9.8);
    public static final boolean USE_IMPROVED_EULER = true;

    private List<Body> bodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();
    private List<ElasticConnector> connectors = new ArrayList<>();

    private List<Collider> removedColliders = new ArrayList<>();
    private List<Body> removedBodies = new ArrayList<>();

    private List<Collider> addedColliders = new ArrayList<>();
    private List<Body> addedBodies = new ArrayList<>();

    private Thread thread;

    public void update() {
        // Update deltaTime
        Time.update();
        // Cleanup bodies and colliders.
        cleanUp();

        // reset to zero at start of time step, so accumulation of forces can begin.
        for (Body body : bodies) {
            body.resetTotalForce();
        }

        //for (ElasticConnector ec : connectors) {
        //    ec.applyTensionForceToBothParticles();
        //}

        // tell each body to move
        for (Body body : bodies) {
            updateBody(body);
        }

        for (int n = 0; n < bodies.size(); n++) {
            Body body = bodies.get(n);
            for (Collider collider : colliders) {
                Collision collision = worldCollisionDetection(body, collider);
                if (collision != null) {
                    worldCollisionForce(body, collision);
                    collider.onCollision(collision);
                }
            }
            for (int m = 0; m < n; m++) {// avoids double check by requiring m<n
                Body body2 = bodies.get(m);
                Collision collision = bodyCollisionDetection(body, body2);
                if (collision != null) {
                    implementElasticCollision(body, body2, collision.getContactPoints(), collision.getCoefficientOfRestitution());
                }
            }
        }
    }

    public void updateBody(Body body) {
        Vector2D acceleration = body.getAcceleration();
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
        } else {
            // basic Euler
            body.setPosition(body.getPosition().addScaled(body.getVelocity(), Time.deltaTime));
            body.setVelocity(body.getVelocity().addScaled(acceleration, Time.deltaTime));
        }
        body.setOrientation(body.getOrientation() + body.getAngularVelocity() * Time.deltaTime);
    }

    private Collision worldCollisionDetection(Body body, Collider c)
    {
        List<Vector2D> contactPoints = new ArrayList<>();
        Collider collider = null;
        for (Vector2D point : body.getCorners()) {
            Vector2D r = point.minus(body.getPosition());
            Vector2D v = body.getVelocity().add(r.mult(body.getAngularVelocity()));
            Collider collisionCollider = c.checkCollision(point, v, 0, 0.1);
            if (collider == null && collisionCollider != null) { // Avoid collision detect of multiple colliders
                collider = collisionCollider;
            }
            if (collisionCollider != null && collider == collisionCollider) { // collision happened and on the same collider as previous
                contactPoints.add(point);
            }
        }
        if (contactPoints.size() == 0) {
            return null;
        }
        return new Collision(body, collider, contactPoints);
    }

    public void worldCollisionForce(Body body, Collision collision)
    {
        double mass = body.getMass();
        double i = body.getMomentOfInertia();
        Vector2D centerContactPoint = collision.getContactPoint();

        Vector2D n = collision.getCollider().getUnitNormal(centerContactPoint);
        Vector2D r = centerContactPoint.minus(body.getPosition());
        Vector2D v = body.getVelocity();
        double w = body.getAngularVelocity();
        double e = collision.getCoefficientOfRestitution();

        Vector2D vap = v.add(r.mult(w));
        double j = (-(e + 1.0) * (vap.scalarProduct(n))) / ((1.0/mass)+(Math.pow(r.scalarProduct(n), 2)/i));

        body.setVelocity(v.add(n.mult(j/mass)));
        body.setAngularVelocity(w + (r.scalarProduct(n)*j/i));
    }

    private Collision bodyCollisionDetection(Body body, Body body2) {
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

    // Bodies and colliders are not directly added to the active lists,
    // this is to avoid concurrency issues between threads.
    public void register(Body body) {
        this.addedBodies.add(body);
    }
    public void register(Collider collider) {
        this.addedColliders.add(collider);
    }
    public void unregister(Body b) { removedBodies.add(b); }
    public void unregister(Collider c) { removedColliders.add(c); }

    // add/remove bodies and colliders from active lists.
    private void cleanUp()
    {
        colliders.addAll(addedColliders);
        colliders.removeAll(removedColliders);
        bodies.addAll(addedBodies);
        bodies.removeAll(removedBodies);

        addedColliders.clear();
        addedBodies.clear();
        removedColliders.clear();
        removedBodies.clear();
    }
}

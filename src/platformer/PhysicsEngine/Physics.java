package platformer.PhysicsEngine;

import platformer.GameEngine.Vector2D;
import java.util.ArrayList;
import java.util.List;

public class Physics
{
    public static final Vector2D GRAVITY = new Vector2D(0, -9.8);
    public static final boolean USE_IMPROVED_EULER = true;

    public static final int NUM_EULER_UPDATES_PER_SCREEN_REFRESH=10;
    public static final double DELTA_T = 20 / 2000.0 / NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
    private List<Body> bodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();

    private Thread thread;

    public void activate()
    {
        startThread(this);
    }


    private void startThread(final Physics physics) {
        thread=new Thread(() -> {
            // this while loop will exit any time this method is called for a second time, because
            while (thread==Thread.currentThread()) {
                physics.update();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                }
            }
        });// this will cause any old threads running to self-terminate
        thread.start();
    }

    public void register(Body body)
    {
        this.bodies.add(body);
    }

    public void register(Collider collider)
    {
        this.colliders.add(collider);
    }

    public void update()
    {
        // reset to zero at start of time step, so accumulation of forces can begin.
        for (Body body : bodies) {
            body.resetTotalForce();
        }
        /*for (ElasticConnector ec: connectors) {
            ec.applyTensionForceToBothParticles();
        }*/
        // tell each body to move
        for (Body body : bodies) {
            updateBody(body);
        }

        for (Body body : bodies) {
            // check if 2 bodies collide
            for (Body body2 : bodies) {
                if (body != body2) {
                    Collision collision = body.collidesWith(body2);
                    if (collision != null) {
                        body.setVelocity(collision.calculateVelocityAfterACollision(body.getVelocity()));
                    }
                }
            }

            for (Collider collider : colliders) {
                Collision collision = body.collidesWith(collider);
                if (collision != null) {
                    body.setVelocity(collision.calculateVelocityAfterACollision(body.getVelocity()));
                }
            }
        }
        double e=0.9; // coefficient of restitution for all particle pairs
        for (int n=0;n<bodies.size();n++) {
            for (int m = 0; m < n; m++) {// avoids double check by requiring m<n
                implementElasticCollision(bodies.get(n), bodies.get(m), e);
            }
        }
    }

    private void updateBody(Body body)
    {
        Vector2D acceleration = body.getAcceleration();
        if (USE_IMPROVED_EULER) {
            //Vector2D pos2= body.getPosition().addScaled(body.getVelocity(), DELTA_T);// in theory this could be used,e.g. if acc2 depends on pos - but in this constant gravity field it will not be relevant
            Vector2D vel2= body.getVelocity().addScaled(acceleration, DELTA_T);
            Vector2D velAv = vel2.add(body.getVelocity()).mult(0.5);
            Vector2D acc2 = new Vector2D(acceleration);//assuming acceleration is constant
            // Note acceleration is NOT CONSTANT for distance dependent forces such as
            // Hooke's law or newton's law of gravity, so this is BUG
            // in this Improved Euler implementation.
            // The whole program structure needs changing to fix this problem properly!
            Vector2D accAv=acc2.add(acceleration).mult(0.5);
            body.setPosition(body.getPosition().addScaled(velAv, DELTA_T));
            body.setVelocity(body.getVelocity().addScaled(accAv, DELTA_T));
        } else {
            // basic Euler
            body.setPosition(body.getPosition().addScaled(body.getVelocity(), DELTA_T));
            body.setVelocity(body.getVelocity().addScaled(acceleration, DELTA_T));
        }
    }

    private void implementElasticCollision(Body b1, Body b2, double e) {
        Collision collision = b1.collidesWith(b2);
        if (collision == null) {
            return;
        }
        Vector2D vec1to2 = b2.getPosition().minus(b1.getPosition()).normalise();
        double v1n=b1.getVelocity().scalarProduct(vec1to2);
        double v2n=b2.getVelocity().scalarProduct(vec1to2);
        double approachSpeed=v2n-v1n;
        double j=b1.getMass()*b2.getMass()*(1+e)*-approachSpeed/(b1.getMass()+b2.getMass());
        b1.setVelocity(b1.getVelocity().addScaled(vec1to2, -j/b1.getMass()));
        b2.setVelocity(b2.getVelocity().addScaled(vec1to2, j/b2.getMass()));
    }
}

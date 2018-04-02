package platformer.PhysicsEngine;

import platformer.GameEngine.Component;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import static platformer.PhysicsEngine.Physics.GRAVITY;

public class Body implements Component
{
    private final GameObject gameObject;
    private Vector2D velocity = new Vector2D(0,0);
    private Vector2D angularVelocity = new Vector2D(0,0);
    private Vector2D totalForceThisTimestep = new Vector2D(0, 0);
    private final double mass;
    private final double rollingFriction;

    public Body(GameObject gameObject, double mass, double rollingFriction)
    {
        this.gameObject = gameObject;
        this.mass = mass;
        this.rollingFriction = rollingFriction;
    }

    void resetTotalForce() {
        totalForceThisTimestep = new Vector2D();
    }

    private void applyWeight() {
        applyForce(GRAVITY.mult(mass));
    }

    public void applyForce(Vector2D force) {
        // To calculate F_net, as used in Newton's Second Law,
        // we need to accumulate all of the forces and add them up
        totalForceThisTimestep = totalForceThisTimestep.add(force);
    }

    public void applyForce(Vector2D force, Vector2D contactPoint) {
        Vector2D relativePoint = contactPoint.minus(getPosition());
        this.angularVelocity = this.angularVelocity.add(force.scale(relativePoint.mult(0.1)));

        // To calculate F_net, as used in Newton's Second Law,
        // we need to accumulate all of the forces and add them up
        //totalForceThisTimestep = totalForceThisTimestep.add(force.pow(2).mult(1/relativePoint.mag()));
    }

    private void applyBasicRollingFriction(double amountOfRollingFriction) {
        this.angularVelocity = this.angularVelocity.mult(0.9);
        //applyForce(getVelocity().mult(-amountOfRollingFriction*mass));
    }


    public Vector2D getAcceleration() {
        // Apply forces that always exist on particle:
        applyWeight();

        // this particle has been told to slow down gradually due to rolling friction
        applyBasicRollingFriction(rollingFriction);

        //calculate Acceleration using Newton's second law.
        return totalForceThisTimestep.mult(1/mass);// using a=F/m from Newton's Second Law
    }

    public Vector2D getPosition()
    {
        return gameObject.getPosition();
    }

    void setPosition(Vector2D vector2D)
    {
        gameObject.setPosition(vector2D);
    }

    public Vector2D getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2D velocity)
    {
        this.velocity = velocity;
    }

    public double getMass() {
        return mass;
    }

    public Collision collidesWith(Body body2) {
        for (Component component : gameObject.getComponentsInChildren(Collider.class)) {
            Collision collision = body2.collidesWith((Collider) component);
            if (collision != null) {
                return collision;
            }
        }
        return null;
    }

    public Collision collidesWith(Collider collider)
    {
        for (Component component : gameObject.getComponentsInChildren(Collider.class)) {
            Collision collision = ((Collider) component).collide(collider);
            if (collision != null) {
                return collision;
            }
        }
        return null;
    }

    public void applyRotation(double deltaT)
    {
       // if (Math.abs(Math.toDegrees(angularVelocity.angle())) > 0.5) {
            gameObject.rotate(Math.toDegrees(angularVelocity.angle()) * deltaT);
        //}
    }

    public Vector2D getAngularVelocity() {
        return angularVelocity;
    }
}

package platformer.PhysicsEngine;

import platformer.GameEngine.Component;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static platformer.PhysicsEngine.Physics.GRAVITY;

public class Body implements Component
{
    private final GameObject gameObject;
    private Vector2D velocity = new Vector2D(0,0);
    private double angularVelocity = 0.0;
    private Vector2D totalForceThisTimestep = new Vector2D(0, 0);
    private final double mass;
    private final double rollingFriction;
    private double momentOfInertia = 10000000.0;

    public Body(GameObject gameObject, double mass)
    {
        this(gameObject, mass, 0.0);
    }

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

    public Vector2D getAcceleration() {
        // Apply forces that always exist on particle:
        applyWeight();

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

    public double getOrientation()
    {
        return gameObject.getRotation();
    }

    public void setOrientation(double orientation)
    {
        gameObject.setRotation(orientation);
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) { this.angularVelocity = angularVelocity; }

    public List<Vector2D> getCorners() {
        List<Vector2D> corners = new ArrayList<>();
        for (Component c : gameObject.getComponentsInChildren(Collider.class)) {
            Collider collider = (Collider) c;
            corners.addAll(collider.getCorners());
        }
        return corners;
    }

    public double getMomentOfInertia() {
        return momentOfInertia;
    }
}

package platformer.PhysicsEngine;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Component;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Colliders.PolygonCollider;

import java.util.ArrayList;
import java.util.List;

import static platformer.PhysicsEngine.Physics.GRAVITY;

public class Body extends AbstractComponent implements Component
{
    private boolean hasGravity = true;
    private Vector2D velocity = new Vector2D(0,0);
    private double angularVelocity = 0.0;
    private Vector2D totalForceThisTimestep = new Vector2D(0, 0);
    private final double mass;
    private double momentOfInertia = 100000000.0;
    private Double radius;
    private Vector2D maxVelocity = new Vector2D(60, 60);

    public Body(double mass)
    {
        this.mass = mass;
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

    public void enableGravity(boolean enabled)
    {
        hasGravity = enabled;
    }

    public Vector2D getAcceleration() {
        if (hasGravity) {
            // Apply forces that always exist on particle:
            applyWeight();
        }

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
    public Vector2D getVelocity() { return velocity; }
    public void setVelocity(Vector2D velocity)
    {
        // Limit Y velocity
        if (velocity.Y() > maxVelocity.Y()) {
            velocity = new Vector2D(velocity.X(), maxVelocity.Y());
        }
        // Limit X velocity
        if (velocity.X() > maxVelocity.X()) {
            velocity = new Vector2D(maxVelocity.X(), velocity.Y());
        }
        this.velocity = velocity;
    }
    public double getMass() {
        return mass;
    }
    public double getOrientation()
    {
        return gameObject.getRotation();
    }
    public void setOrientation(double orientation) { gameObject.setRotation(orientation); }
    public double getAngularVelocity() { return angularVelocity; }
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
    public Body setMomentOfInertia(double momentOfInertia) {
        this.momentOfInertia = momentOfInertia;
        return this;
    }

    public String toString() {
        return getClass().getSimpleName() + "("+gameObject+")";
    }

    public double getRadius() {
        if (radius == null) {
            for (Vector2D corner : getCorners()) {
                double distance = getPosition().minus(corner).mag();
                radius = radius == null || distance > radius ? distance : radius ;
            }
        }
        return radius;
    }

    public List<Collider> getColliders() {
        List<Collider> colliders = new ArrayList<>();
        for (Component c : gameObject.getComponentsInChildren(Collider.class)) {
            if (c instanceof PolygonCollider) {
                colliders.addAll(((PolygonCollider) c).getColliders());
            } else {
                colliders.add((Collider) c);
            }
        }
        return colliders;
    }
}

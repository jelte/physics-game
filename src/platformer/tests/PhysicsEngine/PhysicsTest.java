package platformer.tests.PhysicsEngine;

import org.junit.Test;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Colliders.LineCollider;
import platformer.PhysicsEngine.Colliders.PolygonCollider;
import platformer.PhysicsEngine.Collision;
import platformer.PhysicsEngine.Physics;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PhysicsTest
{
    @Test
    public void testWorldCollisionOneCollisionPoint()
    {
        GameObject gameObject = new GameObject(new Vector2D(0,1 ));
        Body body = new Body( 1.0);
        gameObject.addComponent(body);
        gameObject.addComponent(new PolygonCollider( 4, 2));
        gameObject.rotate(45);
        Collider collider = new LineCollider(new Vector2D(-10,0 ), new Vector2D(10, 0));
        collider.setGameObject(new GameObject());

        Physics engine = new Physics();
        List<Vector2D> contactPoints = new ArrayList<>();
        contactPoints.add(new Vector2D());
        //engine.updateBody(body);
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(0.0, body.getVelocity().Y(), 0.0);
        assertEquals(0.0, body.getAngularVelocity(), 0.0);

        body.setVelocity(Vector2D.down().mult(9.8));
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(9.8, body.getVelocity().Y(), 0.000001);
        assertEquals(0.0, body.getAngularVelocity(), 0.00001);
    }

    @Test
    public void testWorldCollisionTwoCollisionPoint()
    {
        GameObject gameObject = new GameObject(new Vector2D(0, 1));
        Body body = new Body( 1.0);
        gameObject.addComponent(body);
        gameObject.addComponent(new PolygonCollider( 4, 2));
        gameObject.rotate(45);
        Collider collider = new LineCollider(new Vector2D(-10,0 ), new Vector2D(10, 0));
        collider.setGameObject(new GameObject());

        Physics engine = new Physics();
        List<Vector2D> contactPoints = new ArrayList<>();
        contactPoints.add(Vector2D.left());
        contactPoints.add(Vector2D.right());
       // engine.updateBody(body);
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(0.0, body.getVelocity().Y(), 0.0);
        assertEquals(0.0, body.getAngularVelocity(), 0.0);

        body.setVelocity(Vector2D.down().mult(9.8));
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(9.8, body.getVelocity().Y(), 0.00001);
        assertEquals(0.0, body.getAngularVelocity(), 0.00001);
    }

    @Test
    public void testWorldCollisionMassLargerThan1()
    {
        GameObject gameObject = new GameObject(new Vector2D(0, 1));
        Body body = new Body( 10.0);
        gameObject.addComponent(body);
        gameObject.addComponent(new PolygonCollider( 4, 2));
        gameObject.rotate(45);
        Collider collider = new LineCollider(new Vector2D(-10,0 ), new Vector2D(10, 0));
        collider.setGameObject(new GameObject());

        Physics engine = new Physics();
        List<Vector2D> contactPoints = new ArrayList<>();
        contactPoints.add(Vector2D.left());
        contactPoints.add(Vector2D.right());
        //engine.updateBody(body);
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(0.0, body.getVelocity().Y(), 0.0);
        assertEquals(0.0, body.getAngularVelocity(), 0.0);

        body.setVelocity(Vector2D.down().mult(9.8));
        engine.worldCollisionForce(body, new Collision(body, collider, contactPoints));

        assertEquals(9.8, body.getVelocity().Y(), 0.00001);
        assertEquals(0.0, body.getAngularVelocity(), 0.00001);
    }
}

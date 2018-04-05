package platformer;

import platformer.GameEngine.Behaviours.Drawables.Circle;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Colliders.BoxCollider;
import platformer.PhysicsEngine.Colliders.CurveCollider;
import platformer.PhysicsEngine.Colliders.LineCollider;
import platformer.PhysicsEngine.Colliders.PolygonCollider;
import platformer.UI.GameWindow;
import platformer.UI.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Game
{
    static GameWindow window;
    private World world;
    public static boolean DEBUG = true;

    public Game()
    {
        world = new World();

        GameObject ground = new GameObject(new Vector2D(0, 0));
        //ground.addComponent(new Line(ground, new Vector2D(-20, 0), new Vector2D(20,0)));
        ground.addComponent(new LineCollider(ground, new Vector2D(-20, 0), new Vector2D(20,0)));
        //ground.addComponent(new Line(ground, new Vector2D(-20, 0), new Vector2D(-20,20), Color.RED));
        ground.addComponent(new LineCollider(ground, new Vector2D(-20, 20), new Vector2D(-20,0)));
        //ground.addComponent(new Line(ground, new Vector2D(20, 0), new Vector2D(20,20), Color.RED));
        ground.addComponent(new LineCollider(ground, new Vector2D(20, 20), new Vector2D(-20,20)));
        //ground.addComponent(new Line(ground, new Vector2D(20, 20), new Vector2D(-20,20), Color.RED));
        ground.addComponent(new LineCollider(ground, new Vector2D(20, 0), new Vector2D(20,20)));
        world.add(ground);

        GameObject ball = new GameObject(new Vector2D(0, 30));
        ball.addComponent(new Circle(ball, 5));
        ball.addComponent(new Body(ball, 1, 0.0));
        ball.addComponent(new CurveCollider(ball, 5, false));
       // world.add(ball);

        GameObject cart = new GameObject(new Vector2D(0, 1));
        cart.addComponent(new Body(cart, 100, 2));
        cart.addComponent(new Rectangle(cart, new Dimension(4, 2)));
        cart.addComponent(new BoxCollider(cart, new Dimension(4,2)));
        GameObject frontWheel = new GameObject(new Vector2D(1.25, -.75));
        frontWheel.addComponent(new Circle(frontWheel, 1.25, Color.RED));
        frontWheel.addComponent(new CurveCollider(frontWheel, 1.25, false));
        frontWheel.addComponent(new Line(frontWheel, new Vector2D(), new Vector2D(0.62, 0), Color.GREEN));
        cart.add(frontWheel);
        GameObject backWheel = new GameObject(new Vector2D(-1.25, -.75));
        backWheel.addComponent(new Circle(backWheel, 1.25, Color.RED));
        backWheel.addComponent(new CurveCollider(backWheel, 1.25, false));
        backWheel.addComponent(new Line(backWheel, new Vector2D(), new Vector2D(0.62, 0), Color.GREEN));
        cart.add(backWheel);
       // world.add(cart);

        world.getPhysics().activate();

        ball = new GameObject(new Vector2D(0, 17));
        //ball.addComponent(new Circle(ball, 5, Color.green));
        ball.addComponent(new Body(ball, 1, 0.1));
        ball.addComponent(new CurveCollider(ball, 2, false));
     //  world.add(ball);



        GameObject box = new GameObject(new Vector2D(0, 10));
        //box.addComponent(new Rectangle(box, new Dimension(4, 4)));
        box.addComponent(new Body(box, 1, 0.1));
        box.addComponent(new BoxCollider(box, new Dimension(2, 2)));
       // box.rotate(90);
        //world.add(box);
        ((Body)box.getComponent(Body.class)).setVelocity(Vector2D.left().mult(10));



        GameObject triangle = new GameObject(new Vector2D(3, 5));
        triangle.addComponent(new Body(triangle, 1, 0.1));
        triangle.addComponent(new PolygonCollider(triangle, 3, 2));
        world.add(triangle);

        GameObject square = new GameObject(new Vector2D(10, 5));
        square.addComponent(new Body(square, 1, 0.1));
        square.addComponent(new PolygonCollider(square, 4, 2));
        //world.add(square);

        GameObject pentagon = new GameObject(new Vector2D(-10, 5));
        pentagon.addComponent(new Body(pentagon, 1, 0.1));
        pentagon.addComponent(new PolygonCollider(pentagon, 5, 2));
        //world.add(pentagon);

        GameObject hexagon = new GameObject(new Vector2D(-5, 10));
        hexagon.addComponent(new Body(hexagon, 1, 0.1));
        hexagon.addComponent(new PolygonCollider(hexagon, 6, 2));
//        world.add(hexagon);

    }

    public static void main(String[] args) throws Exception
    {
        Game game = new Game();

        window = new GameWindow(new Viewport(game.world, 120), "platformer");
    }
}

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
import platformer.UI.GameWindow;
import platformer.UI.Viewport;

import java.awt.*;

public class Game
{
    static GameWindow window;
    private World world;
    public static boolean DEBUG = true;

    public Game()
    {
        world = new World();

        GameObject cart = new GameObject();
        cart.addComponent(new Body(cart, 100, 2));
        cart.addComponent(new Rectangle(cart, new Dimension(4, 2)));
        GameObject frontWheel = new GameObject(new Vector2D(1.25, -.75));
        frontWheel.addComponent(new Circle(frontWheel, 1.25, Color.RED));
        frontWheel.addComponent(new CurveCollider(frontWheel, 1.25, false));
        frontWheel.addComponent(new Line(frontWheel, new Vector2D(), new Vector2D(0.62, 0), Color.GREEN));
        cart.add(frontWheel);
        GameObject backWheel = new GameObject(new Vector2D(-1.25, -.75));
        backWheel.addComponent(new Circle(backWheel, 1.25, Color.RED));
        backWheel.addComponent(new CurveCollider(backWheel, 1.25, false));
        backWheel.addComponent(new Line(backWheel, new Vector2D(), new Vector2D(0.62, 0), Color.GREEN));
        backWheel.rotate(90.0);
        cart.add(backWheel);
       // world.add(cart);

        GameObject ground = new GameObject(new Vector2D(0, -1.5));
        ground.addComponent(new Line(ground, new Vector2D(-20, 0), new Vector2D(20,0)));
        ground.addComponent(new LineCollider(ground, new Vector2D(-20, 0), new Vector2D(20,0)));
        ground.addComponent(new Line(ground, new Vector2D(10, 0), new Vector2D(15,2), Color.RED));
        ground.addComponent(new LineCollider(ground, new Vector2D(10, 0), new Vector2D(15,2)));
        ground.addComponent(new Line(ground, new Vector2D(-10, 0), new Vector2D(-15,5), Color.RED));
        ground.addComponent(new LineCollider(ground, new Vector2D(-15, 5), new Vector2D(-10,0)));
        ground.addComponent(new Line(ground, new Vector2D(-10, 0), new Vector2D(-15,0), Color.RED));
        world.add(ground);

        GameObject ball = new GameObject(new Vector2D(-12, 10));
        ball.addComponent(new Circle(ball, 1));
        ball.addComponent(new Body(ball, 1, 0.1));
        ball.addComponent(new CurveCollider(ball, 1, false));
        world.add(ball);

        GameObject box = new GameObject(new Vector2D(12, 10));
        box.addComponent(new Rectangle(box, new Dimension(1, 1)));
        box.addComponent(new Body(box, 1, 0.7));
        box.addComponent(new BoxCollider(box, new Dimension(1, 1)));
        box.rotate(90);
        world.add(box);

        world.getPhysics().activate();
    }

    public static void main(String[] args) throws Exception
    {
        Game game = new Game();

        window = new GameWindow(new Viewport(game.world, 120), "platformer");
    }
}

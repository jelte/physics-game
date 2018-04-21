package platformer;

import platformer.Breakout.*;
import platformer.Breakout.Bricks.BrickPowerUpCollisionHandler;
import platformer.Breakout.Bricks.BrickSplitBallCollisionHandler;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
import platformer.PhysicsEngine.Colliders.CurveCollider;
import platformer.UI.GameWindow;
import platformer.UI.Viewport;
import platformer.UI.WorldKeyListener;

import java.util.Random;

public class Game
{
    private World world;
    public static boolean DEBUG = false;

    private Game()
    {
        // Initialize world
        world = new World();

        // Set up walls
        world.add(new Wall(Vector2D.down().mult(2)));

        // Set up ground
        world.add(new Ground(Vector2D.down().mult(3)));

        // Set up bar
        Bar bar = new Bar(Vector2D.up());

        // Set up bricks
        for ( int i = 0; i < 6; i++) {
            for (int j = -7; j <= 7; j++) {
                Brick brick = new Brick(new Vector2D(j * 4, 35 + (i * 2)), i + 1);
                if (new Random().nextInt(1000) > 950) {
                    brick.addComponent(new BrickPowerUpCollisionHandler(bar, new Random().nextInt(1000) >= 500 ? 0.75 : 1.5));
                }
                if (new Random().nextInt(1000) > 950) {
                    brick.addComponent(new BrickSplitBallCollisionHandler(world));
                }
                world.add(brick);
            }
        }

        // Set up ball
        Ball ball = new Ball(Vector2D.up().mult(2));
        bar.add(ball);
        world.add(bar);
    }

    public static void main(String[] args) throws Exception
    {
        Game game = new Game();

        GameWindow window = new GameWindow(new Viewport(game.world, 240), "Break-out");

        window.addKeyListener(new WorldKeyListener(game.world));

        game.world.getPhysics().activate();
    }
}

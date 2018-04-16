package platformer;

import platformer.Breakout.*;
import platformer.GameEngine.Vector2D;
import platformer.GameEngine.World;
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
        world = new World();

        world.add(new Wall(Vector2D.down().mult(2)));
        world.add(new Ground(Vector2D.down().mult(3)));

        for ( int i = 0; i < 6; i++) {
            for (int j = -7; j <= 7; j++) {
                world.add(new Brick(new Vector2D(j * 4, 35 + (i * 2)), i + 1));
            }
        }

        Bar bar = new Bar(Vector2D.up());
        Ball ball = new Ball(Vector2D.up().mult(2));
        bar.add(ball);
        //world.add(ball);
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

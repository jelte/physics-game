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

        for (int i = 0; i < 1; i++) {
            world.add(new Ball(new Vector2D(new Random().nextInt(50) -25, 48 - new Random().nextInt(8))));
        }
        world.add(new Bar(new Vector2D(0, 1)));

    }

    public static void main(String[] args) throws Exception
    {
        Game game = new Game();

        GameWindow window = new GameWindow(new Viewport(game.world, 240), "Break-out");

        window.addKeyListener(new WorldKeyListener(game.world));

        game.world.getPhysics().activate();
    }
}

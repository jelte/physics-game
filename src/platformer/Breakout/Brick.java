package platformer.Breakout;

import platformer.Breakout.Bricks.BrickCollisionHandler;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Behaviours.Drawables.Circle;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Colliders.LineCollider;

import java.awt.*;

public class Brick extends GameObject
{
    private static Color[] colors = new Color[] {
        new Color(132, 31, 39),
        Color.DARK_GRAY,
        Color.LIGHT_GRAY,
        Color.WHITE,
        Color.YELLOW,
        Color.RED,
        Color.GREEN
    };
    private Drawable graphic;
    private int health;

    public Brick(Vector2D position)
    {
        this(position, 0);
    }

    public Brick(Vector2D position, int health)
    {
        super(position);

        this.health = health;

        addComponent(new LineCollider(new Vector2D(2, -1), new Vector2D(-2,-1)));
        addComponent(new LineCollider(new Vector2D(-2, -1), new Vector2D(-2,1)));
        addComponent(new LineCollider(new Vector2D(-2, 1), new Vector2D(2,1)));
        addComponent(new LineCollider(new Vector2D(2, 1), new Vector2D(2,-1)));

        addComponent(new BrickCollisionHandler());

        graphic = (Drawable) addComponent(new Rectangle(new Dimension(4, 2 ), colors[health]));

        addComponent(new Line(new Vector2D(2, -0.9), new Vector2D(-2,-0.9), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(1.9, -1), new Vector2D(1.9,1), new Color(0,0,0, 150)));
        addComponent(new Line(new Vector2D(2, 1), new Vector2D(-2,1), new Color(255,255,255, 150)));
        addComponent(new Line(new Vector2D(-2, -1), new Vector2D(-2,1), new Color(255,255,255, 150)));
        if (health > 0) {
            addComponent(new Circle(new Vector2D(1.6, -.6), 0.3, Color.darkGray));
            addComponent(new Circle(new Vector2D(1.5, -.5), 0.3, Color.lightGray));
            addComponent(new Circle(new Vector2D(1.6, .6), 0.3, Color.darkGray));
            addComponent(new Circle(new Vector2D(1.5, .7), 0.3, Color.lightGray));
            addComponent(new Circle(new Vector2D(-1.6, -.6), 0.3, Color.darkGray));
            addComponent(new Circle(new Vector2D(-1.7, -.5), 0.3, Color.lightGray));
            addComponent(new Circle(new Vector2D(-1.6, .6), 0.3, Color.darkGray));
            addComponent(new Circle(new Vector2D(-1.7, .7), 0.3, Color.lightGray));
        }
    }

    public void takeDamage(int amount)
    {
        health -= amount;

        if (health <= 0) {
            this.destroy();
        } else {
            graphic.setColor(colors[health]);
        }
    }
}

package platformer.Breakout;

import platformer.Breakout.Bricks.BrickCollisionHandler;
import platformer.GameEngine.Behaviours.Drawables.Line;
import platformer.GameEngine.Behaviours.Drawables.Rectangle;
import platformer.GameEngine.GameObject;
import platformer.GameEngine.Vector2D;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Colliders.LineCollider;

import java.awt.*;

public class Wall extends GameObject
{
    public Wall(Vector2D position) {
        super(position);

        for (int j = 0; j < 28; j++) {
            for (int i = -10; i < 11; i++) {
                if (Math.abs(i) > 8 || j == 27) {
                    Brick brick = new Brick(Vector2D.right().add(Vector2D.right().mult(i * 4).add(Vector2D.up().mult(j * 2))).add(j % 2 == 0 ? new Vector2D() : Vector2D.left().mult(2)));
                    brick.removeComponents(BrickCollisionHandler.class);
                    brick.removeComponents(Collider.class);
                    add(brick);
                }
            }
        }

        addComponent(new Rectangle(Vector2D.up().mult(26), new Dimension(70, 54), Color.BLACK));
        addComponent(new Line(new Vector2D(-35, 53), new Vector2D(35, 53), Color.lightGray));
        addComponent(new Line(new Vector2D(-35, -1), new Vector2D(-35, 53), Color.lightGray));
        addComponent(new Line(new Vector2D(35, -1), new Vector2D(35, 53), Color.darkGray));

        addComponent(new LineCollider(new Vector2D(-35, 53), new Vector2D(-35, -1)));
        addComponent(new LineCollider(new Vector2D(35, -1), new Vector2D(35, 53)));
        addComponent(new LineCollider(new Vector2D(35, 53), new Vector2D(-35, 53)));
    }
}

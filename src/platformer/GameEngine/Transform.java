package platformer.GameEngine;

public class Transform {
    private Vector2D position;
    private double rotation = 0.0;

    public Transform() {
        this.position = new Vector2D();
        this.rotation = 0.0;
    }

    public Transform(Vector2D position)
    {
        this.position = position;
    }

    public double getLocalRotation() {
        return rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void rotate(double v) {
        rotation += v;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}

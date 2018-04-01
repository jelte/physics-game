package platformer.GameEngine;

import java.io.Serializable;

public class Vector2D implements Serializable
{
    private final double x, y;

    /****************
     * Constructors *
     ****************/
    public Vector2D() { this(0,0); }
    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Vector2D(Vector2D base)
    {
        this(base.x, base.y);
    }

    /**************
     * Operations *
     **************/

    public Vector2D add(Vector2D v)
    {
        return new Vector2D(this.x+v.x, this.y+v.y);
    }

    public Vector2D minus(Vector2D v)
    {
        return new Vector2D(this.x-v.x, this.y-v.y);
    }


    // scaled addition - surprisingly useful
    // note: vector subtraction can be expressed as scaled addition with factor
    // (-1)
    public Vector2D addScaled(Vector2D v, double fac)
    {
        return new Vector2D(this.x + v.x * fac, this.y + v.y * fac);
    }

    public Vector2D mult(double fac)
    {
        return new Vector2D(this.x * fac,this.y * fac);
    }

    public Vector2D rotate(double angle)
    {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double nx = x * cos - y * sin;
        double ny = x * sin + y * cos;
        return new Vector2D(nx,ny);
    }

    public double scalarProduct(Vector2D v)
    {
        return x * v.x + y * v.y;
    }

    public Vector2D normalise()
    {
        double len = mag();
        return new Vector2D(x/len, y/len);
    }

    public Vector2D rotate90degreesAnticlockwise() {
        return new Vector2D(-y,x);
    }

    /*************
     * Utilities *
     *************/
    public double mag() {
        return Math.hypot(x, y);
    }
    public double angle() {
        return Math.atan2(y, x);
    }
    /**
     * Determine the angle between this vector and another/
     * @param other
     * @return double
     */
    public double angle(Vector2D other) {
        return Math.atan2(other.y - y, other.x - x);
    }

     /**
     * Compare this object with another.
     * @param other
     * @return boolean
     */
    public boolean equals(Object other)
    {
        if (!(other instanceof Vector2D)) {
            return false;
        }
        Vector2D v = (Vector2D) other;
        return v.x == x && v.y == y;
    }

    public String toString() {
        return String.format("%s(%.01f, %.01f)", this.getClass().getName(), x, y);
    }

    /***********
     * Getters *
     ***********/
    public double X() {
        return this.x;
    }

    public double Y() {
        return this.y;
    }
}

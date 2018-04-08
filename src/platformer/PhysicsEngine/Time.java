package platformer.PhysicsEngine;

public class Time {
    public static double deltaTime = 0.0;
    public final static long startTime = System.currentTimeMillis();
    public static long lastFrame = System.currentTimeMillis();

    public static void update() {
        deltaTime = (double)(System.currentTimeMillis() - lastFrame)/1000;
        lastFrame = System.currentTimeMillis();
    }
}

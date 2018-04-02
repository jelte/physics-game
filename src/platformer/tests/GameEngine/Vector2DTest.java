package platformer.tests.GameEngine;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import platformer.GameEngine.Vector2D;

public class Vector2DTest {
    @Test
    public void testRotation()
    {
        Vector2D vector = new Vector2D(1, 1);

        Vector2D expected = vector.rotate90degreesAnticlockwise();
        Vector2D rotated = vector.rotate(90);
        assertEquals(expected.X(), rotated.X(), 0.001f);
        assertEquals(expected.Y(), rotated.Y(), 0.001f);
    }
}

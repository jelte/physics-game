package platformer.Breakout;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.KeyListener;
import platformer.GameEngine.Vector2D;

import java.awt.event.KeyEvent;

public class BarController extends AbstractComponent implements KeyListener
{
    public void onKeyDown(KeyEvent event)
    {
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                ((Bar) gameObject).setSpeed(75);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                ((Bar) gameObject).setSpeed(-75);
                break;

            case KeyEvent.VK_SPACE:
                ((Bar) gameObject).launch();
        }
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        // Reset speed to 0 when releasing the keys.
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (((Bar) gameObject).getSpeed() > 0) {
                    ((Bar) gameObject).setSpeed(0);
                }
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (((Bar) gameObject).getSpeed() < 0) {
                    ((Bar) gameObject).setSpeed(0);
                }
                break;
        }
    }
}

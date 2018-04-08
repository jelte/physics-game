package platformer.GameEngine;

import java.awt.event.KeyEvent;

public interface KeyListener extends Component {
    void onKeyDown(KeyEvent event);

    void onKeyPressed(KeyEvent event);

    void onKeyReleased(KeyEvent event);
}

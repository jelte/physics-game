package platformer.GameEngine.Behaviours;

import platformer.GameEngine.Component;
import platformer.GameEngine.Camera;

import java.awt.*;

public interface Drawable extends Component {
    void draw(Graphics2D g, Camera camera);

    void setColor(Color color);
}

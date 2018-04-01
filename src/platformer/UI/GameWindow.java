package platformer.UI;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame
{
    private Component comp;

    public GameWindow(Component comp, String title) {
        super(title);
        this.comp = comp;
        this.setPreferredSize(new Dimension(800, 600));
        getContentPane().add(BorderLayout.CENTER, comp);
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }
}

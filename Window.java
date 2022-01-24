import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame frame;

    public Window(int width, int height, String title, Main main) {
        frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(main);
        frame.addKeyListener(new Keyboard(main));
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setLayout(null);
    }
}

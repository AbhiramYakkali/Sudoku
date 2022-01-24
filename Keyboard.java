import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private Main main;

    public Keyboard(Main main) {
        this.main = main;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        main.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

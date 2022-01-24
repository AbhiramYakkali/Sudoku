import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    private Main main;

    public Mouse(Main main) {
        this.main = main;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        main.mouseClicked(e.getX(), e.getY());
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}

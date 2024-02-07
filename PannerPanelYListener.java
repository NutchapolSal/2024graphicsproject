import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JSpinner;

class PannerPanelYListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Point point;
    private JSpinner spinner;
    private int startY = 0;
    private int originY = 0;

    PannerPanelYListener(JSpinner spinner, Point point) {
        this.spinner = spinner;
        this.point = point;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startY = e.getY();
            originY = point.y;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originY + (e.getY() - startY) / 8);
        } else {
            spinner.setValue(originY + (e.getY() - startY));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        originY = point.y;
        e.getComponent().requestFocusInWindow();
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
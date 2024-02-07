import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JSpinner;

class PannerPanelHListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Dimension dim;
    private JSpinner spinner;
    private int startY = 0;
    private int originY = 0;

    PannerPanelHListener(JSpinner spinner, Dimension dim) {
        this.spinner = spinner;
        this.dim = dim;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startY = e.getY();
            originY = dim.height;
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
        originY = dim.height;
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
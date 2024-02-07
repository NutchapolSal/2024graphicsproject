import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JSpinner;

class PannerPanelWListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Dimension dim;
    private JSpinner spinner;
    private int startX = 0;
    private int originX = 0;

    PannerPanelWListener(JSpinner spinner, Dimension dim) {
        this.spinner = spinner;
        this.dim = dim;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startX = e.getX();
            originX = dim.width;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originX + (e.getX() - startX) / 8);
        } else {
            spinner.setValue(originX + (e.getX() - startX));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        originX = dim.width;
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
import java.awt.event.MouseEvent;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

import javax.swing.JSpinner;
import javax.swing.event.MouseInputListener;

class PannerPanelListener implements MouseInputListener {
    private static final int SLOW_FACTOR = 8;
    private boolean slowed = false;
    private JSpinner spinner;
    private int mouseStartV = 0;
    private int startValue = 0;
    private ToIntFunction<MouseEvent> getMouseV = MouseEvent::getX;
    private IntSupplier getV;

    PannerPanelListener(JSpinner spinner, IntSupplier getV, ToIntFunction<MouseEvent> getMouseV) {
        this.spinner = spinner;
        this.getV = getV;
        this.getMouseV = getMouseV;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            mouseStartV = getMouseV.applyAsInt(e);
            startValue = getV.getAsInt();
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(startValue + (getMouseV.applyAsInt(e) - mouseStartV) / SLOW_FACTOR);
        } else {
            spinner.setValue(startValue + (getMouseV.applyAsInt(e) - mouseStartV));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseStartV = getMouseV.applyAsInt(e);
        startValue = getV.getAsInt();
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
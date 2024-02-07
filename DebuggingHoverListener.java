import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class DebuggingHoverListener implements MouseListener {
    private Debuggable obj;
    private int debugValue;

    DebuggingHoverListener(Debuggable obj, int debugValue) {
        this.obj = obj;
        this.debugValue = debugValue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.setDebugging(debugValue);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.unsetDebugging();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class PannerPanelDebuggingHoverListener implements MouseListener {
    private Debuggable obj;
    private int debugValue;
    private int cursorStartX = 0;
    private int cursorStartY = 0;

    PannerPanelDebuggingHoverListener(Debuggable obj, int debugValue) {
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
        GlobalState.pannerPanelDragging = true;
        cursorStartX = e.getXOnScreen();
        cursorStartY = e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlobalState.pannerPanelDragging = false;
        MouseMover.moveMouse(cursorStartX, cursorStartY);
    }

}
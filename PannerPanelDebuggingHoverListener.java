import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class PannerPanelDebuggingHoverListener implements MouseListener {
    private GraphicObject obj;
    private int debugValue;
    private int cursorStartX = 0;
    private int cursorStartY = 0;

    PannerPanelDebuggingHoverListener(GraphicObject obj, int debugValue) {
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
        obj.debugging = debugValue;

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.debugging = -1;

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
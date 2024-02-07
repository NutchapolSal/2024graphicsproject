import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

interface Debuggable {
    public void setDebugging(int index);

    public void unsetDebugging();

    public void debugDraw(Graphics g, double time);

    default void debugCircle(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(x - 5, y - 5, 11, 11);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 5, y - 5, 11, 11);
    }

    default void debugDot(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.fillRect(x - 2, y - 2, 5, 5);
        g2.setColor(active ? Color.green : Color.red);
        g2.fillRect(x - 1, y - 1, 3, 3);
    }

    default void debugLine(Graphics g, int x1, int y1, int x2, int y2, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x1, y1, x2, y2);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x1, y1, x2, y2);
    }
}
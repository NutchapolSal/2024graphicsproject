import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

class GraphicCircle extends GraphicDrawFiller {
    public AnimPoint center;
    public AnimInt radius;

    GraphicCircle(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill, AnimColor fillColor,
            AnimPoint center, AnimInt radius) {
        super(stroke, strokeColor, thickness, fill, fillColor);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        Point center = this.center.get(time);
        int radius = this.radius.get(time);

        if (stroke.get(time)) {
            int thickness = this.thickness.get(time);
            g.setColor(strokeColor.get(time).get());
            midpointCircle(g, center.x, center.y, radius, thickness);
        }
        if (setupFill(g, time)) {
            g.setColor(fillColor.get(time).get());
            g.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
        }
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }

    private void midpointCircle(Graphics g, int xc, int yc, int r, int thickness) {
        int x = 0;
        int y = r;
        int Dx = 2 * x;
        int Dy = 2 * y;
        int D = 1 - r;

        while (x <= y) {
            plot(g, xc + x, yc + y, thickness);
            plot(g, xc - x, yc + y, thickness);
            plot(g, xc + x, yc - y, thickness);
            plot(g, xc - x, yc - y, thickness);
            plot(g, xc + y, yc + x, thickness);
            plot(g, xc + y, yc - x, thickness);
            plot(g, xc - y, yc + x, thickness);
            plot(g, xc - y, yc - x, thickness);

            x++;
            Dx += 2;
            D += Dx + 1;
            if (D >= 0) {
                y--;
                Dy -= 2;
                D -= Dy;
            }
        }
    }

}
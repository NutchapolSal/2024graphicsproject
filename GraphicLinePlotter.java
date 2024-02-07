import java.awt.Graphics;
import java.awt.Point;

abstract class GraphicLinePlotter extends GraphicPlotter {
    public AnimInt thickness;

    protected GraphicLinePlotter(AnimColor color, AnimInt thickness) {
        super(color);
        this.thickness = thickness;
    }

    protected void plotLine(Graphics g, double time, Point p1, Point p2) {
        int dx = Math.abs(p2.x - p1.x);
        int dy = Math.abs(p2.y - p1.y);

        int sx = (p1.x < p2.x) ? 1 : -1;
        int sy = (p1.y < p2.y) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int temp = dy;
            dy = dx;
            dx = temp;
            isSwap = true;
        }
        int D = 2 * dy - dx;

        int x = p1.x;
        int y = p1.y;
        for (int i = 1; i <= dx; i++) {
            plot(g, x, y, thickness.get(time));
            if (D >= 0) {
                if (isSwap) {
                    x += sx;
                } else {
                    y += sy;
                }
                D -= 2 * dx;
            }

            if (isSwap) {
                y += sy;
            } else {
                x += sx;
            }
            D += 2 * dy;
        }
    }
}
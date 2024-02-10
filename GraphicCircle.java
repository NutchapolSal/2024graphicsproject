import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

class GraphicCircle extends GraphicDrawFiller {
    public AnimPoint center;
    public AnimInt radius;

    /** editor default */
    GraphicCircle(TimeKeypoint tkp) {
        this(new AnimBoolean().add(tkp, true, EasingFunction.snap),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimInt().add(tkp, 1, EasingFunction.linear),
                new AnimBoolean().add(tkp, false, EasingFunction.snap),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new AnimInt().add(tkp, 10, EasingFunction.linear));
    }

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

    @Override
    public void debugDraw(Graphics g, double time) {
        Point center = this.center.get(time);
        int radius = this.radius.get(time);
        debugLine(g, center.x, center.y, center.x + radius, center.y, debugging == 2);
        debugDot(g, center.x + radius, center.y, debugging == 2);
        debugCircle(g, center.x, center.y, debugging == 1);
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CIRCLE ");
        sb.append(super.exportParamString());
        sb.append(" ");
        sb.append(ImEx.exportString(this.center));
        sb.append(" ");
        sb.append(ImEx.exportString(this.radius));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicCircle(");
        sb.append(super.exportParamCode());
        sb.append(", ");
        sb.append(ImEx.exportCode(this.center));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.radius));
        sb.append(")");
        return sb.toString();
    }
}
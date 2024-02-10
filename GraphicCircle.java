import java.awt.Graphics;
import java.awt.Point;

class GraphicCircle extends GraphicPlotter {
    public AnimInt thickness;
    public AnimPoint center;
    public AnimInt radius;

    /** editor default */
    GraphicCircle(TimeKeypoint tkp) {
        this(new AnimColor().add(tkp, "#000", EasingFunction.linear), new AnimInt().add(tkp, 1, EasingFunction.linear),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new AnimInt().add(tkp, 10, EasingFunction.linear));
    }

    GraphicCircle(AnimColor color, AnimInt thickness, AnimPoint center, AnimInt radius) {
        super(color);
        this.thickness = thickness;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics g = gOuter.create();
        g.setColor(color.get(time).get());

        Point center = this.center.get(time);
        int radius = this.radius.get(time);

        int thickness = this.thickness.get(time);
        midpointCircle(g, center.x, center.y, radius, thickness);
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
        sb.append(ImEx.exportString(this.thickness));
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
        sb.append(ImEx.exportCode(this.thickness));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.center));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.radius));
        sb.append(")");
        return sb.toString();
    }
}
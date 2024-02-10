import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

class GraphicEllipse extends GraphicDrawFiller {
    public AnimPoint center;
    public AnimInt radiusA;
    public AnimInt radiusB;

    /** editor default */
    GraphicEllipse(TimeKeypoint tkp) {
        this(new AnimBoolean().add(tkp, false, EasingFunction.snap),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimInt().add(tkp, 1, EasingFunction.linear), new AnimBoolean().add(tkp, true, EasingFunction.snap),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new AnimInt().add(tkp, 10, EasingFunction.linear), new AnimInt().add(tkp, 20, EasingFunction.linear));
    }

    GraphicEllipse(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill, AnimColor fillColor,
            AnimPoint center, AnimInt radiusA, AnimInt radiusB) {
        super(stroke, strokeColor, thickness, fill, fillColor);
        this.center = center;
        this.radiusA = radiusA;
        this.radiusB = radiusB;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        Point center = this.center.get(time);
        int radiusA = this.radiusA.get(time);
        int radiusB = this.radiusB.get(time);

        if (stroke.get(time)) {
            int thickness = this.thickness.get(time);
            g.setColor(strokeColor.get(time).get());
            midpointEllipse(g, center.x, center.y, radiusA, radiusB, thickness);
        }
        if (setupFill(g, time)) {
            g.setColor(fillColor.get(time).get());
            g.fillOval(center.x - radiusA, center.y - radiusB, radiusA * 2, radiusB * 2);
        }
    }

    private void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }

    private void midpointEllipse(Graphics g, int xc, int yc, int a, int b, int thickness) {
        int a2 = a * a;
        int b2 = b * b;
        int twoA2 = 2 * a2;
        int twoB2 = 2 * b2;

        int x = 0;
        int y = b;
        int D = Math.round(b2 - a2 * b + a2 / 4f);
        int Dx = 0;
        int Dy = twoA2 * y;
        while (Dx <= Dy) {
            plot(g, xc + x, yc + y, thickness);
            plot(g, xc - x, yc + y, thickness);
            plot(g, xc + x, yc - y, thickness);
            plot(g, xc - x, yc - y, thickness);

            x++;
            Dx += twoB2;
            D += Dx + b2;
            if (D >= 0) {
                y--;
                Dy -= twoA2;
                D -= Dy;
            }
        }

        x = a;
        y = 0;
        D = Math.round(a2 - b2 * a + b2 / 4f);
        Dx = twoB2 * x;
        Dy = 0;
        while (Dx >= Dy) {
            plot(g, xc + x, yc + y, thickness);
            plot(g, xc - x, yc + y, thickness);
            plot(g, xc + x, yc - y, thickness);
            plot(g, xc - x, yc - y, thickness);

            y++;
            Dy += twoA2;
            D += Dy + a2;
            if (D >= 0) {
                x--;
                Dx -= twoB2;
                D -= Dx;
            }
        }
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        Point center = this.center.get(time);
        int radiusA = this.radiusA.get(time);
        int radiusB = this.radiusB.get(time);
        debugLine(g, center.x, center.y, center.x + radiusA, center.y, debugging == 2);
        debugLine(g, center.x, center.y, center.x, center.y + radiusB, debugging == 3);
        debugDot(g, center.x + radiusA, center.y, debugging == 2);
        debugDot(g, center.x, center.y + radiusB, debugging == 3);
        debugCircle(g, center.x, center.y, debugging == 1);
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ELLIPSE ");
        sb.append(super.exportParamString());
        sb.append(" ");
        sb.append(ImEx.exportString(this.center));
        sb.append(" ");
        sb.append(ImEx.exportString(this.radiusA));
        sb.append(" ");
        sb.append(ImEx.exportString(this.radiusB));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicEllipse(");
        sb.append(super.exportParamCode());
        sb.append(", ");
        sb.append(ImEx.exportCode(this.center));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.radiusA));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.radiusB));
        sb.append(")");
        return sb.toString();
    }
}
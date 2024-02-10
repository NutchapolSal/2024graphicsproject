import java.awt.Graphics;
import java.awt.Point;

class GraphicEllipse extends GraphicPlotter {
    public AnimInt thickness;
    public AnimPoint center;
    public AnimInt radiusA;
    public AnimInt radiusB;

    /** editor default */
    GraphicEllipse(TimeKeypoint tkp) {
        this(new AnimColor().add(tkp, "#000", EasingFunction.linear), new AnimInt().add(tkp, 1, EasingFunction.linear),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new AnimInt().add(tkp, 10, EasingFunction.linear), new AnimInt().add(tkp, 20, EasingFunction.linear));
    }

    GraphicEllipse(AnimColor color, AnimInt thickness, AnimPoint center, AnimInt radiusA, AnimInt radiusB) {
        super(color);
        this.thickness = thickness;
        this.center = center;
        this.radiusA = radiusA;
        this.radiusB = radiusB;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics g = gOuter.create();
        g.setColor(color.get(time).get());

        Point center = this.center.get(time);
        int radiusA = this.radiusA.get(time);
        int radiusB = this.radiusB.get(time);

        int thickness = this.thickness.get(time);
        midpointEllipse(g, center.x, center.y, radiusA, radiusB, thickness);
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
        sb.append(ImEx.exportString(this.thickness));
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
        sb.append(ImEx.exportCode(this.thickness));
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
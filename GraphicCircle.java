import java.awt.Graphics;
import java.awt.Point;

// https://stackoverflow.com/q/1734745/3623350
class GraphicCircle extends GraphicBezierPlotter {
    private static final double BEZIER_CIRCLE_CONSTANT = 0.552284749831;
    public AnimPoint center;
    public AnimInt radius;

    GraphicCircle() {
        this(new AnimColor(), new AnimInt(), new AnimPoint(), new AnimInt());
    }

    GraphicCircle(AnimColor color, AnimInt thickness, AnimPoint center, AnimInt radius) {
        super(color, thickness);
        this.center = center;
        this.radius = radius;
    }

    private Point roundPoint(double x, double y) {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics g = gOuter.create();
        g.setColor(color.get(time));

        Point center = this.center.get(time);
        int radius = this.radius.get(time);
        double offset = radius * BEZIER_CIRCLE_CONSTANT;
        double perimeter = radius * 2 * Math.PI;
        int iters = (int) Math.round(perimeter);

        plotBezier(g, time, roundPoint(center.x, center.y - radius), roundPoint(center.x + offset, center.y - radius),
                roundPoint(center.x + radius, center.y - offset), roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y + radius), roundPoint(center.x + offset, center.y + radius),
                roundPoint(center.x + radius, center.y + offset), roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y + radius), roundPoint(center.x - offset, center.y + radius),
                roundPoint(center.x - radius, center.y + offset), roundPoint(center.x - radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y - radius), roundPoint(center.x - offset, center.y - radius),
                roundPoint(center.x - radius, center.y - offset), roundPoint(center.x - radius, center.y), iters);
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        Point center = this.center.get(time);
        int radius = this.radius.get(time);
        debugLine(g, center.x, center.y, center.x + radius, center.y, debugging == 2);
        debugDot(g, center.x + radius, center.y, debugging == 2);
        debugCircle(g, center.x, center.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return null; // TODO
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
import java.awt.Graphics;
import java.awt.Point;

abstract class GraphicBezierPlotter extends GraphicPlotter {
    private static final int BEZIER_ITERATIONS = 2000;
    public AnimInt thickness;

    protected GraphicBezierPlotter(AnimColor color, AnimInt thickness) {
        super(color);
        this.thickness = thickness;
    }

    protected void plotBezier(Graphics g, double time, Point pA, Point pB, Point pC, Point pD) {
        plotBezier(g, time, pA, pB, pC, pD, BEZIER_ITERATIONS);
    }

    protected void plotBezier(Graphics g, double time, Point pA, Point pB, Point pC, Point pD, int iterations) {
        for (int i = 0; i < iterations; i++) {
            double t = i / (double) iterations;

            double x = Math.pow(1 - t, 3) * pA.x + 3 * t * Math.pow(1 - t, 2) * pB.x
                    + 3 * Math.pow(t, 2) * (1 - t) * pC.x + Math.pow(t, 3) * pD.x;

            double y = Math.pow(1 - t, 3) * pA.y + 3 * t * Math.pow(1 - t, 2) * pB.y
                    + 3 * Math.pow(t, 2) * (1 - t) * pC.y + Math.pow(t, 3) * pD.y;

            plot(g, (int) Math.round(x), (int) Math.round(y), thickness.get(time));
        }
    }

    protected String exportParamString() {
        return super.exportParamString() + " " + ImEx.exportString(thickness);
    }

    protected String exportParamCode() {
        return super.exportParamCode() + ", " + ImEx.exportCode(thickness);
    }
}
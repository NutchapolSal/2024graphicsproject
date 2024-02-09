import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GraphicPath2D extends GraphicDrawFiller {
    public AnimPoint p1;
    public List<Path2DData> data;
    public AnimBoolean closed;

    /** editor default */
    GraphicPath2D(TimeKeypoint tkp) {
        this(new AnimBoolean().add(tkp, true, EasingFunction.snap),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimInt().add(tkp, 1, EasingFunction.linear),
                new AnimBoolean().add(tkp, false, EasingFunction.snap),
                new AnimColor().add(tkp, "#222", EasingFunction.linear),
                new AnimBoolean().add(tkp, false, EasingFunction.snap),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new Path2DLine(new AnimPoint().add(tkp, new Point(20, 20), EasingFunction.linear)));
    }

    GraphicPath2D(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill, AnimColor fillColor,
            AnimBoolean closed, AnimPoint p1, Path2DData... data) {
        this(stroke, strokeColor, thickness, fill, fillColor, closed, p1, new ArrayList<>(Arrays.asList(data)));
    }

    GraphicPath2D(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill, AnimColor fillColor,
            AnimBoolean closed, AnimPoint p1, List<Path2DData> data) {
        super(stroke, strokeColor, thickness, fill, fillColor);
        this.p1 = p1;
        this.data = data;
        this.closed = closed;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        g.setColor(Color.black);

        Path2D path = new Path2D.Double();
        path.moveTo(p1.get(time).x, p1.get(time).y);
        for (Path2DData d : data) {
            d.run(path, time);
        }
        if (closed.get(time)) {
            path.closePath();
        }

        if (setupFill(g, time)) {
            g.fill(path);
        }
        if (setupStroke(g, time)) {
            g.draw(path);
        }
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        debugCircle(g, p1.get(time).x, p1.get(time).y, debugging == 1);
        Point pNextA = p1.get(time);
        int i = 2;
        for (Path2DData d : data) {
            if (d instanceof Path2DLine) {
                Path2DLine line = (Path2DLine) d;
                debugCircle(g, line.pNext.get(time).x, line.pNext.get(time).y, debugging == i);
                i++;
            } else if (d instanceof Path2DBezier) {
                Path2DBezier bezier = (Path2DBezier) d;
                debugLine(g, pNextA.x, pNextA.y, bezier.pNext.get(time).x, bezier.pNext.get(time).y, debugging == i);
                debugDot(g, bezier.pNext.get(time).x, bezier.pNext.get(time).y, debugging == i);
                i++;
                for (int j = 1; j < bezier.morePoints.size(); j += 2) {
                    Point controlP = bezier.morePoints.get(j - 1).get(time);
                    Point endP = bezier.morePoints.get(j).get(time);
                    Point controlEndP = endP;
                    if (j + 2 < bezier.morePoints.size()) {
                        controlEndP = new Point(endP.x + (endP.x - controlP.x), endP.y + (endP.y - controlP.y));
                    }
                    debugLine(g, controlP.x, controlP.y, controlEndP.x, controlEndP.y, debugging == i);
                    debugDot(g, controlP.x, controlP.y, debugging == i);
                    debugCircle(g, endP.x, endP.y, debugging == i + 1);
                    i += 2;
                }
            }
            pNextA = d.lastPoint().get(time);
        }

    }

    @Override
    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PATH2D ");
        sb.append(super.exportParamString());
        sb.append(" ");
        sb.append(ImEx.exportString(this.closed));
        sb.append(" ");
        sb.append(ImEx.exportString(this.p1));
        sb.append("\n");
        for (Path2DData p2d : this.data) {
            sb.append(p2d.exportString());
            sb.append("\n");
        }
        sb.append("END");
        return sb.toString();
    }

    @Override
    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicPath2D(");
        sb.append(super.exportParamCode());
        sb.append(", ");
        sb.append(ImEx.exportCode(this.closed));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.p1));
        for (Path2DData p2d : this.data) {
            sb.append(", ");
            sb.append(p2d.exportCode());
        }
        sb.append(")");
        return sb.toString();
    }

}
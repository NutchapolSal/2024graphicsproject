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
}
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
    public static void main(String[] args) {

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("third point six swing")
                .add(new GraphicLine("#000000", 1, new Point(25, 550), new Point(575, 550)))
                .add(new GraphicBezierCurve("#FF0000", 1,
                        new Point(100, 500), new Point(100, 100),
                        new Point(500, 100), new Point(500, 500)))
                .add(new GraphicBezierCurve("#FF7700", 2,
                        new Point(100, 500), new Point(500, 100),
                        new Point(100, 500), new Point(500, 500)))
                .add(new GraphicPolyline("#00FF00", 3, false,
                        new Point(150, 150), new Point(250, 100), new Point(325, 125),
                        new Point(375, 225), new Point(400, 325), new Point(275, 375), new Point(100, 300)))
                .add(new GraphicPolygon("#0000FF",
                        new Point(350, 100), new Point(350, 200), new Point(300, 200)))
                .add(new GraphicPolyBezier("#FF00FF", 1,
                        new Point(100, 100),
                        new PolyBezierData(new Point(50, 130),
                                new Point(150, 130), new Point(90, 160)),
                        new PolyBezierData(new Point(100, 300),
                                new Point(250, 150), new Point(300, 250),
                                new Point(450, 150), new Point(500, 250))))
                .add(new GraphicFloodFill("#00FFFF", new Point(250, 250)))
                .add(new GraphicCircle("#2266AA", 1, new Point(80, 40), 20))

        );

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("MMXXIV");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}

class GraphicLayer {
    public boolean shown = true;
    public String name;
    public List<GraphicObject> objects;

    GraphicLayer(String name) {
        this.name = name;
        this.objects = new ArrayList<>();
    }

    GraphicLayer(String name, List<GraphicObject> objects) {
        this.name = name;
        this.objects = objects;
    }

    BufferedImage draw() {
        BufferedImage buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        for (GraphicObject object : objects) {
            object.draw(buffer);
        }
        return buffer;
    }

    GraphicLayer setShown(boolean shown) {
        this.shown = shown;
        return this;
    }

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

    GraphicLayer remove(GraphicObject object) {
        objects.remove(object);
        return this;
    }

}

abstract class GraphicObject {
    abstract public void draw(BufferedImage buffer);
}

class ColorHexer {

    public static Color decode(String hex) {
        return decodeOptional(hex).orElse(Color.black);
    }

    // formats:
    // #RGB
    // #RGBA
    // #RRGGBB
    // #RRGGBBAA
    public static Optional<Color> decodeOptional(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        try {
            Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (hex.length() == 3) {
            return Optional.of(Color.decode("#" +
                    hex.substring(0, 1).repeat(2) +
                    hex.substring(1, 2).repeat(2) +
                    hex.substring(2, 3).repeat(2)));
        } else if (hex.length() == 4) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 1), 16) * 17,
                    Integer.parseInt(hex.substring(1, 2), 16) * 17,
                    Integer.parseInt(hex.substring(2, 3), 16) * 17,
                    Integer.parseInt(hex.substring(3, 4), 16) * 17));
        } else if (hex.length() == 6) {
            return Optional.of(Color.decode("#" + hex));
        } else if (hex.length() == 8) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16),
                    Integer.parseInt(hex.substring(6, 8), 16)));
        } else {
            return Optional.empty();
        }
    }

    public static String encode(Color c) {
        if (c.getAlpha() == 255) {
            return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        } else {
            return String.format("#%02x%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
    }
}

abstract class GraphicPlotter extends GraphicObject {
    public Color color;

    protected GraphicPlotter(String hexColor) {
        this.color = ColorHexer.decode(hexColor);
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }
}

abstract class GraphicLinePlotter extends GraphicPlotter {
    public int thickness;

    protected GraphicLinePlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness = thickness;
    }

    protected void plotLine(Graphics g, Point p1, Point p2) {
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
            plot(g, x, y, thickness);
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

class GraphicLine extends GraphicLinePlotter {
    public Point p1, p2;

    GraphicLine(String hexColor, int thickness, Point p1, Point p2) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        plotLine(g, p1, p2);
    }

}

class GraphicPolygon extends GraphicPlotter {
    public List<Point> points;

    GraphicPolygon(String hexColor, Point... points) {
        this(hexColor, new ArrayList<>(Arrays.asList(points)));
    }

    GraphicPolygon(String hexColor, List<Point> points) {
        super(hexColor);
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics2D g = buffer.createGraphics();
        g.setColor(color);

        Polygon poly = new Polygon();
        for (Point point : points) {
            poly.addPoint(point.x, point.y);
        }
        g.fillPolygon(poly);
    }

}

class GraphicPolyline extends GraphicLinePlotter {
    public boolean closed;
    public List<Point> points;

    GraphicPolyline(String hexColor, int thickness, boolean closed, Point... points) {
        this(hexColor, thickness, closed, new ArrayList<>(Arrays.asList(points)));
    }

    GraphicPolyline(String hexColor, int thickness, boolean closed, List<Point> points) {
        super(hexColor, thickness);
        this.closed = closed;
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        for (int i = 1; i < points.size(); i++) {
            plotLine(g, points.get(i - 1), points.get(i));
        }
        if (closed) {
            plotLine(g, points.get(points.size() - 1), points.get(0));
        }
    }

}

abstract class GraphicBezierPlotter extends GraphicPlotter {
    private static final int BEZIER_ITERATIONS = 2000;
    public int thickness;

    protected GraphicBezierPlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness = thickness;
    }

    protected void plotBezier(Graphics g, Point pA, Point pB, Point pC, Point pD) {
        plotBezier(g, pA, pB, pC, pD, BEZIER_ITERATIONS);
    }

    protected void plotBezier(Graphics g, Point pA, Point pB, Point pC, Point pD, int iterations) {
        for (int i = 0; i < iterations; i++) {
            double t = i / (double) iterations;

            double x = Math.pow(1 - t, 3) * pA.x +
                    3 * t * Math.pow(1 - t, 2) * pB.x +
                    3 * Math.pow(t, 2) * (1 - t) * pC.x
                    + Math.pow(t, 3) * pD.x;

            double y = Math.pow(1 - t, 3) * pA.y +
                    3 * t * Math.pow(1 - t, 2) * pB.y +
                    3 * Math.pow(t, 2) * (1 - t) * pC.y
                    + Math.pow(t, 3) * pD.y;

            plot(g, (int) Math.round(x), (int) Math.round(y), thickness);
        }
    }
}

class GraphicBezierCurve extends GraphicBezierPlotter {
    public Point p1, p2;
    public List<Point> continuedPoints;

    GraphicBezierCurve(String hexColor, int thickness, Point p1, Point p2, Point... continuedPoints) {
        this(hexColor, thickness, p1, p2, new ArrayList<>(Arrays.asList(continuedPoints)));
    }

    GraphicBezierCurve(String hexColor, int thickness, Point p1, Point p2, List<Point> continuedPoints) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.p2 = p2;
        this.continuedPoints = continuedPoints;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        plotBezier(g, p1, p2, continuedPoints.get(0), continuedPoints.get(1));
        for (int i = 3; i < continuedPoints.size(); i += 2) {
            Point pA = continuedPoints.get(i - 2);
            Point pBtemp = continuedPoints.get(i - 3);
            Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
            Point pC = continuedPoints.get(i - 1);
            Point pD = continuedPoints.get(i);

            plotBezier(g, pA, pB, pC, pD);
        }
    }

}

class PolyBezierData {
    public Point p2;
    public List<Point> morePoints;

    PolyBezierData(Point p2, Point... morePoints) {
        this(p2, new ArrayList<>(Arrays.asList(morePoints)));
    }

    PolyBezierData(Point p2, List<Point> morePoints) {
        this.p2 = p2;
        this.morePoints = morePoints;
    }

}

class GraphicPolyBezier extends GraphicBezierPlotter {
    public Point p1;
    public List<PolyBezierData> data;

    GraphicPolyBezier(String hexColor, int thickness, Point p1, PolyBezierData... data) {
        this(hexColor, thickness, p1, new ArrayList<>(Arrays.asList(data)));
    }

    GraphicPolyBezier(String hexColor, int thickness, Point p1, List<PolyBezierData> data) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.data = data;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        Point pNextA = p1;

        for (PolyBezierData d : data) {
            plotBezier(g, pNextA, d.p2, d.morePoints.get(0), d.morePoints.get(1));
            for (int i = 3; i < d.morePoints.size(); i += 2) {
                Point pA = d.morePoints.get(i - 2);
                Point pBtemp = d.morePoints.get(i - 3);
                Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
                Point pC = d.morePoints.get(i - 1);
                Point pD = d.morePoints.get(i);

                plotBezier(g, pA, pB, pC, pD);
            }
            pNextA = d.morePoints.get(d.morePoints.size() - 1);
        }
    }

}

// https://stackoverflow.com/q/1734745/3623350
class GraphicCircle extends GraphicBezierPlotter {
    private static final double BEZIER_CIRCLE_CONSTANT = 0.552284749831;
    public Point center;
    public int radius;

    GraphicCircle(String hexColor, int thickness, Point center, int radius) {
        super(hexColor, thickness);
        this.center = center;
        this.radius = radius;
    }

    private Point roundPoint(double x, double y) {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);
        double offset = radius * BEZIER_CIRCLE_CONSTANT;
        double perimeter = radius * 2 * Math.PI;
        int iters = (int) Math.round(perimeter);

        plotBezier(g, roundPoint(center.x, center.y - radius),
                roundPoint(center.x + offset, center.y - radius),
                roundPoint(center.x + radius, center.y - offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y + radius),
                roundPoint(center.x + offset, center.y + radius),
                roundPoint(center.x + radius, center.y + offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y + radius),
                roundPoint(center.x - offset, center.y + radius),
                roundPoint(center.x - radius, center.y + offset),
                roundPoint(center.x - radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y - radius),
                roundPoint(center.x - offset, center.y - radius),
                roundPoint(center.x - radius, center.y - offset),
                roundPoint(center.x - radius, center.y), iters);
    }

}

class GraphicFloodFill extends GraphicPlotter {
    public Point point;

    GraphicFloodFill(String hexColor, Point point) {
        super(hexColor);
        this.point = point;
    }

    @Override
    public void draw(BufferedImage buffer) {
        if (!(0 <= point.x &&
                point.x < buffer.getWidth() &&
                0 <= point.y &&
                point.y < buffer.getHeight())) {
            return;
        }
        if (buffer.getRGB(point.x, point.y) == color.getRGB()) {
            return;
        }

        Queue<Point> q = new ArrayDeque<>();
        q.add(point);
        Color target_color = new Color(buffer.getRGB(point.x, point.y), true);

        for (Point p = q.poll(); p != null; p = q.poll()) {
            if (!(0 <= p.x &&
                    p.x < buffer.getWidth() &&
                    0 <= p.y &&
                    p.y < buffer.getHeight())) {
                continue;
            }

            if (buffer.getRGB(p.x, p.y) == target_color.getRGB()) {
                buffer.setRGB(p.x, p.y, color.getRGB());
                q.add(new Point(p.x + 1, p.y));
                q.add(new Point(p.x - 1, p.y));
                q.add(new Point(p.x, p.y + 1));
                q.add(new Point(p.x, p.y - 1));
            }
        }
    }

}

class GraphicsPanel extends JPanel {
    List<GraphicLayer> instructions;

    GraphicsPanel(List<GraphicLayer> instructions) {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        this.instructions = instructions;
    }

    @Override
    protected void paintComponent(Graphics gOuter) {
        super.paintComponent(gOuter);

        for (GraphicLayer layer : instructions) {
            if (layer.shown) {
                gOuter.drawImage(layer.draw(), 0, 0, null);
            }
        }
    }

}

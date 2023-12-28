import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Main {
    public static void main(String[] args) {

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("cross", new ArrayList<>())
                .add(new GraphicLine("#777700", new Point(50, 50), new Point(550, 550)))
                .add(new GraphicLine("#777700", new Point(50, 550), new Point(550, 50))));
        instructions.add(new GraphicLayer("third swing", new ArrayList<>())
                .add(new GraphicLine("#000000", new Point(25, 550), new Point(575, 550)))
                .add(new GraphicBezierCurve("#FF0000",
                        new Point(100, 500), new Point(100, 100), new Point(500, 100), new Point(500, 500)))
                .add(new GraphicBezierCurve("#FF7700",
                        new Point(100, 500), new Point(500, 100), new Point(100, 500), new Point(500, 500)))
                .add(new GraphicPolygon("#00FF00",
                        List.of(new Point(150, 150), new Point(250, 100), new Point(325, 125), new Point(375, 225),
                                new Point(400, 325), new Point(275, 375), new Point(100, 300))))
                .add(new GraphicFloodFill("#00FFFF", new Point(310, 340))));

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("MMXXIV");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JFrame frame2 = new JFrame();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(EditingPanelFactory.create(instructions.get(1)));
        frame2.add(scrollPane);
        frame2.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        // EditingPanelFactory.recurseAddBorders(frame2.getContentPane());
        frame2.setTitle("editor");
        frame2.pack();
        frame2.setVisible(true);

        new Timer(1000 / 60, e -> {
            panel.repaint();
        }).start();

    }
}

class GraphicLayer {
    public boolean shown = true;
    public String name;
    public List<GraphicObject> objects;

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

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

}

abstract class GraphicObject {
    public Color color = Color.black;

    protected void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    abstract public void draw(BufferedImage buffer);
}

class GraphicLine extends GraphicObject {
    public Point p1, p2;

    GraphicLine(String hexColor, Point p1, Point p2) {
        this.color = Color.decode(hexColor);
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

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
            plot(g, x, y);
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

class GraphicPolygon extends GraphicObject {
    public List<Point> points;

    GraphicPolygon(String hexColor, List<Point> points) {
        this.color = Color.decode(hexColor);
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        Polygon poly = new Polygon();
        for (Point point : points) {
            poly.addPoint(point.x, point.y);
        }
        g.drawPolygon(poly);
    }
}

class GraphicBezierCurve extends GraphicObject {
    private static final int BEZIER_ITERATIONS = 2000;
    public Point p1, p2, p3, p4;

    GraphicBezierCurve(String hexColor, Point p1, Point p2, Point p3, Point p4) {
        this.color = Color.decode(hexColor);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        for (int i = 0; i < BEZIER_ITERATIONS; i++) {
            double t = i / (double) BEZIER_ITERATIONS;

            double x = Math.pow(1 - t, 3) * p1.x +
                    3 * t * Math.pow(1 - t, 2) * p2.x +
                    3 * Math.pow(t, 2) * (1 - t) * p3.x
                    + Math.pow(t, 3) * p4.x;

            double y = Math.pow(1 - t, 3) * p1.y +
                    3 * t * Math.pow(1 - t, 2) * p2.y +
                    3 * Math.pow(t, 2) * (1 - t) * p3.y
                    + Math.pow(t, 3) * p4.y;

            plot(g, (int) Math.round(x), (int) Math.round(y));
        }
    }
}

class GraphicFloodFill extends GraphicObject {
    public Point point;

    GraphicFloodFill(String hexColor, Point point) {
        this.color = Color.decode(hexColor);
        this.point = point;
    }

    @Override
    public void draw(BufferedImage buffer) {
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

class EditingPanelFactory {
    private EditingPanelFactory() {
    }

    public static JPanel create(String labelText, Point point) {

        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JSpinner xSpinner = new JSpinner(new SpinnerNumberModel(point.x, 0, 599, 1));
        xSpinner.addChangeListener(e -> {
            point.x = (int) xSpinner.getValue();
        });

        JSpinner ySpinner = new JSpinner(new SpinnerNumberModel(point.y, 0, 599, 1));
        ySpinner.addChangeListener(e -> {
            point.y = (int) ySpinner.getValue();
        });

        JPanel pannerPanel = new JPanel();
        pannerPanel.setPreferredSize(new Dimension(20, 20));
        pannerPanel.setMaximumSize(new Dimension(20, 20));
        pannerPanel.setMinimumSize(new Dimension(20, 20));
        pannerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        JPanel pannerXPanel = new JPanel();
        pannerXPanel.setPreferredSize(new Dimension(20, 8));
        pannerXPanel.setMaximumSize(new Dimension(20, 8));
        pannerXPanel.setMinimumSize(new Dimension(20, 8));
        pannerXPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerXPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

        JPanel pannerYPanel = new JPanel();
        pannerYPanel.setPreferredSize(new Dimension(8, 20));
        pannerYPanel.setMaximumSize(new Dimension(8, 20));
        pannerYPanel.setMinimumSize(new Dimension(8, 20));
        pannerYPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerYPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));

        class PannerPanelXListener implements MouseMotionListener, MouseListener {
            private int startX = 0;
            private int originX = 0;

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                point.x = originX + (e.getX() - startX);
                xSpinner.setValue(point.x);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                originX = point.x;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        }
        class PannerPanelYListener implements MouseMotionListener, MouseListener {
            private int startY = 0;
            private int originY = 0;

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                point.y = originY + (e.getY() - startY);
                ySpinner.setValue(point.y);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startY = e.getY();
                originY = point.y;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        }

        var xListener = new PannerPanelXListener();
        var yListener = new PannerPanelYListener();
        pannerPanel.addMouseListener(xListener);
        pannerPanel.addMouseListener(yListener);
        pannerPanel.addMouseMotionListener(xListener);
        pannerPanel.addMouseMotionListener(yListener);
        pannerXPanel.addMouseListener(xListener);
        pannerXPanel.addMouseMotionListener(xListener);
        pannerYPanel.addMouseListener(yListener);
        pannerYPanel.addMouseMotionListener(yListener);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(xSpinner)
                .addComponent(ySpinner)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(pannerYPanel)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(pannerPanel)
                        .addComponent(pannerXPanel)));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(pannerXPanel)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(label)
                                .addComponent(xSpinner)
                                .addComponent(ySpinner)
                                .addComponent(pannerYPanel)
                                .addComponent(pannerPanel)));

        return panel;
    }

    public static JPanel create(GraphicLayer layer) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        var vGroup = layout.createSequentialGroup();
        var hGroup = layout.createParallelGroup();
        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);

        var label = new JLabel(layer.name);
        vGroup.addComponent(label);
        hGroup.addComponent(label);
        for (var gObj : layer.objects) {
            var objPanel = create(gObj);
            vGroup.addPreferredGap(ComponentPlacement.RELATED);
            vGroup.addComponent(objPanel);
            hGroup.addComponent(objPanel);
        }

        return panel;
    }

    public static JPanel create(GraphicObject object) {
        if (object instanceof GraphicLine) {
            return create((GraphicLine) object);
        } else if (object instanceof GraphicPolygon) {
            return create((GraphicPolygon) object);
        } else if (object instanceof GraphicBezierCurve) {
            return create((GraphicBezierCurve) object);
        } else if (object instanceof GraphicFloodFill) {
            return create((GraphicFloodFill) object);
        } else {
            return new JPanel();
        }
    }

    public static JPanel create(GraphicLine line) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicLine");
        var p1Panel = create("p1", line.p1);
        var p2Panel = create("p2", line.p2);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(p1Panel)
                .addComponent(p2Panel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(p1Panel)
                .addGap(0, 2, 2)
                .addComponent(p2Panel));

        return panel;
    }

    public static JPanel create(GraphicPolygon polygon) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPolygon");
        var pointsPanel = new JPanel();
        GroupLayout pointsLayout = new GroupLayout(pointsPanel);
        pointsPanel.setLayout(pointsLayout);
        var pointsVGroup = pointsLayout.createSequentialGroup();
        var pointsHGroup = pointsLayout.createParallelGroup();
        pointsLayout.setVerticalGroup(pointsVGroup);
        pointsLayout.setHorizontalGroup(pointsHGroup);

        for (int i = 0; i < polygon.points.size(); i++) {
            var pointPanel = create("p" + i, polygon.points.get(i));
            pointsVGroup.addPreferredGap(ComponentPlacement.RELATED);
            pointsVGroup.addComponent(pointPanel);
            pointsHGroup.addComponent(pointPanel);
        }

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(pointsPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(pointsPanel));

        return panel;
    }

    public static JPanel create(GraphicBezierCurve bezierCurve) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicBezierCurve");
        var p1Panel = create("p1", bezierCurve.p1);
        var p2Panel = create("p2", bezierCurve.p2);
        var p3Panel = create("p3", bezierCurve.p3);
        var p4Panel = create("p4", bezierCurve.p4);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(p1Panel)
                .addComponent(p2Panel)
                .addComponent(p3Panel)
                .addComponent(p4Panel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(p1Panel)
                .addGap(0, 2, 2)
                .addComponent(p2Panel)
                .addGap(0, 2, 2)
                .addComponent(p3Panel)
                .addGap(0, 2, 2)
                .addComponent(p4Panel));

        return panel;
    }

    public static JPanel create(GraphicFloodFill floodFill) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicFloodFill");
        var pointPanel = create("point", floodFill.point);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(pointPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(pointPanel));

        return panel;
    }

    public static void recurseAddBorders(Container c) {
        for (var v : c.getComponents()) {
            try {
                ((JComponent) v).setBorder(BorderFactory.createLineBorder(Color.RED));
            } catch (ClassCastException e) {
            }
            try {
                recurseAddBorders((Container) v);
            } catch (ClassCastException e) {
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
import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

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
                .add(new GraphicImage("null", new Point(10, 10), new Dimension(20, 20), 1.0))
                .add(new GraphicCircle("#2266AA", 1, new Point(80, 40), 20))

        );

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("MMXXIV");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        var exx = ImportExport.exportString(instructions);
        System.out.println(exx);
        System.out.println(ImportExport.exportString(ImportExport.importString(exx)));

        new EditorFrame(frame, instructions);

        new Timer(1000 / 60, e -> {
            panel.repaint();
        }).start();

    }

}

class EditorFrame {

    static Preferences prefs = Preferences.userRoot().node("2024graphicsprojecteditor");

    private List<GraphicLayer> instructions;

    private MutableInt currentLayer = new MutableInt(-1);
    private MutableString savePath = new MutableString(null);

    private JScrollPane layerScrollPane = new JScrollPane();
    private JScrollPane editorScrollPane = new JScrollPane();

    private JFrame frame2 = new JFrame();

    EditorFrame(JFrame frame, List<GraphicLayer> instructions) {
        this.instructions = instructions;

        frame2.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        frame2.setTitle("editor");
        frame2.setSize(600, 600);

        JPanel panel2 = new JPanel();
        frame2.setContentPane(panel2);

        GroupLayout layout = new GroupLayout(panel2);
        panel2.setLayout(layout);

        layerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        layerScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        editorScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        editorScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        updateLayerListPanel();

        JButton addLayerButton = new JButton("add layer");
        addLayerButton.addActionListener(e -> {
            this.instructions.add(new GraphicLayer("new layer", new ArrayList<>()));
            updateLayerListPanel();
        });
        JButton layerUpButton = new JButton("^");
        layerUpButton.addActionListener(e -> {
            if (currentLayer.value > 0) {
                var temp = instructions.get(currentLayer.value - 1);
                instructions.set(currentLayer.value - 1, instructions.get(currentLayer.value));
                instructions.set(currentLayer.value, temp);
                currentLayer.value--;
                updateLayerListPanel();
            }
        });

        JButton layerDownButton = new JButton("v");
        layerDownButton.addActionListener(e -> {
            if (currentLayer.value < instructions.size() - 1) {
                var temp = instructions.get(currentLayer.value + 1);
                instructions.set(currentLayer.value + 1, instructions.get(currentLayer.value));
                instructions.set(currentLayer.value, temp);
                currentLayer.value++;
                updateLayerListPanel();
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(layerScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addLayerButton)
                                .addComponent(layerUpButton)
                                .addComponent(layerDownButton)))
                .addComponent(editorScrollPane));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(layerScrollPane)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(addLayerButton)
                                .addComponent(layerUpButton)
                                .addComponent(layerDownButton)))
                .addComponent(editorScrollPane));

        JMenuBar menuBar = new JMenuBar();
        frame2.setJMenuBar(menuBar);

        var fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        var saveMenuItem = fileMenu.add("Save");
        var saveAsMenuItem = fileMenu.add("Save as...");
        var loadMenuItem = fileMenu.add("Load");

        JFileChooser fileChooser = new JFileChooser(prefs.get("lastSavePath", System.getProperty("user.home")));

        saveMenuItem.addActionListener(e -> {
            if (this.savePath.value == null) {
                if (fileChooser.showSaveDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                    this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                }
            }
            if (this.savePath.value != null) {
                try {
                    FileWriter fw = new FileWriter(this.savePath.value);
                    fw.write(ImportExport.exportString(instructions));
                    fw.close();
                    frame2.setTitle("editor - " + new File(this.savePath.value).getName() + " @ "
                            + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                    prefs.put("lastSavePath", this.savePath.value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        saveAsMenuItem.addActionListener(e -> {
            if (fileChooser.showSaveDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    FileWriter fw = new FileWriter(this.savePath.value);
                    fw.write(ImportExport.exportString(instructions));
                    fw.close();
                    frame2.setTitle("editor - " + new File(this.savePath.value).getName() + " @ "
                            + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                    prefs.put("lastSavePath", this.savePath.value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        loadMenuItem.addActionListener(e -> {
            if (fileChooser.showOpenDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    var file = new File(this.savePath.value);
                    Scanner scanner = new Scanner(file);
                    instructions.clear();
                    var newInstructions = ImportExport.importLayers(scanner);
                    instructions.addAll(newInstructions);
                    scanner.close();
                    prefs.put("lastSavePath", this.savePath.value);
                    frame2.setTitle("editor - " + file.getName());
                    updateLayerListPanel();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK));

        frame2.setVisible(true);

        new Timer(250, e -> {
            if (GlobalState.needsUpdateEditor) {
                changeEditorPane(this.currentLayer.value);
                GlobalState.needsUpdateEditor = false;
            }
            if (GlobalState.needsUpdateLayers) {
                updateLayerListPanel();
                GlobalState.needsUpdateLayers = false;
            }
        }).start();
    }

    private void changeEditorPane(int layerIndex) {
        if (layerIndex < 0 || layerIndex >= instructions.size()) {
            editorScrollPane.setViewportView(new JPanel());
            this.currentLayer.value = layerIndex;
            return;
        }
        boolean sameLayer = layerIndex == this.currentLayer.value;
        int scrollPos = editorScrollPane.getVerticalScrollBar().getValue();
        editorScrollPane
                .setViewportView(EditingPanelFactory.create(this.instructions.get(layerIndex)));
        if (sameLayer) {
            editorScrollPane.getVerticalScrollBar().setValue(scrollPos);
        } else {
            editorScrollPane.getVerticalScrollBar().setValue(0);
        }
        this.currentLayer.value = layerIndex;
    }

    private void updateLayerListPanel() {
        JPanel layerPane = new JPanel();
        GroupLayout layerLayout = new GroupLayout(layerPane);
        layerPane.setLayout(layerLayout);

        var layerVGroup = layerLayout.createSequentialGroup();
        var layerCheckboxHGroup = layerLayout.createParallelGroup();
        var layerRadioHGroup = layerLayout.createParallelGroup();
        layerLayout.setHorizontalGroup(layerLayout.createSequentialGroup()
                .addGroup(layerCheckboxHGroup)
                .addGroup(layerRadioHGroup));
        layerLayout.setVerticalGroup(layerVGroup);

        ButtonGroup layerButtonGroup = new ButtonGroup();

        changeEditorPane(Math.max(0, Math.min(this.currentLayer.value, instructions.size() - 1)));

        int layerI = 0;
        for (GraphicLayer layer : instructions) {
            var layerVisibleCheckbox = new JCheckBox();
            layerVisibleCheckbox.setSelected(layer.shown);
            layerVisibleCheckbox.addActionListener(e -> {
                layer.shown = layerVisibleCheckbox.isSelected();
            });

            var layerEditRadio = new JRadioButton(layer.name.value);
            if (layerI == currentLayer.value) {
                layerEditRadio.setSelected(true);
            }
            final int layerI2 = layerI;
            layerEditRadio.addActionListener(e -> {
                changeEditorPane(layerI2);
            });
            layerButtonGroup.add(layerEditRadio);

            layerVGroup.addGroup(
                    layerLayout.createParallelGroup(Alignment.CENTER)
                            .addComponent(layerVisibleCheckbox)
                            .addComponent(layerEditRadio));
            layerVGroup.addPreferredGap(ComponentPlacement.RELATED);
            layerCheckboxHGroup.addComponent(layerVisibleCheckbox);
            layerRadioHGroup.addComponent(layerEditRadio);

            JPopupMenu layerPopupMenu = new JPopupMenu();
            var layerDeleteMenuItem = layerPopupMenu.add("Delete");
            layerDeleteMenuItem.addActionListener(e -> {
                instructions.remove(layer);
                GlobalState.needsUpdateLayers = true;
            });

            layerEditRadio.setComponentPopupMenu(layerPopupMenu);

            layerI++;
        }
        layerScrollPane.setViewportView(layerPane);
    }
}

class MutableBoolean {
    public boolean value;

    MutableBoolean(boolean value) {
        this.value = value;
    }
}

class MutableColor {
    public Color value;

    MutableColor(Color value) {
        this.value = value;
    }
}

class MutableDouble {
    public double value;

    MutableDouble(double value) {
        this.value = value;
    }
}

class MutableInt {
    public int value;

    MutableInt(int value) {
        this.value = value;
    }
}

class MutableString {
    public String value;

    MutableString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;

    }
}

class GraphicLayer {
    public boolean shown = true;
    public MutableString name = new MutableString("");
    public List<GraphicObject> objects;

    private BufferedImage cache = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

    GraphicLayer(String name) {
        this.name.value = name;
        this.objects = new ArrayList<>();
    }

    GraphicLayer(String name, List<GraphicObject> objects) {
        this.name.value = name;
        this.objects = objects;
    }

    BufferedImage draw() {
        var changed = false;
        for (GraphicObject object : objects) {
            if (object.changed) {
                changed = true;
                break;
            }
        }
        if (!changed) {
            return cache;
        }
        BufferedImage buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        for (GraphicObject object : objects) {
            object.draw(buffer);
            object.changed = false;
        }
        cache = buffer;
        return buffer;
    }

    void debugDraw(Graphics g) {
        for (GraphicObject object : objects) {
            if (object.debugging != -1) {
                object.debugDraw(g);
            }
        }
    }

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

}

abstract class GraphicObject {
    abstract public void draw(BufferedImage buffer);

    public int debugging = -1;
    public boolean changed = true;

    abstract public void debugDraw(Graphics g);

    abstract public GraphicObject copy();

    protected void debugCircle(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(x - 5, y - 5, 11, 11);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 5, y - 5, 11, 11);
    }

    protected void debugDot(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.fillRect(x - 2, y - 2, 5, 5);
        g2.setColor(active ? Color.green : Color.red);
        g2.fillRect(x - 1, y - 1, 3, 3);
    }

    protected void debugLine(Graphics g, int x1, int y1, int x2, int y2, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x1, y1, x2, y2);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x1, y1, x2, y2);
    }
}

class ColorHexer {

    // formats:
    // #RGB
    // #RGBA
    // #RRGGBB
    // #RRGGBBAA
    public static Color decode(String hex) {
        if (!hex.startsWith("#")) {
            return Color.black;
        }
        try {
            Long.parseLong(hex.substring(1), 16);
        } catch (NumberFormatException e) {
            return Color.black;
        }

        if (hex.length() == 4) {
            return Color.decode("#" +
                    hex.substring(1, 2).repeat(2) +
                    hex.substring(2, 3).repeat(2) +
                    hex.substring(3, 4).repeat(2));
        } else if (hex.length() == 5) {
            return new Color(
                    Integer.parseInt(hex.substring(1, 2), 16) * 17,
                    Integer.parseInt(hex.substring(2, 3), 16) * 17,
                    Integer.parseInt(hex.substring(3, 4), 16) * 17,
                    Integer.parseInt(hex.substring(4, 5), 16) * 17);
        } else if (hex.length() == 7) {
            return Color.decode(hex);
        } else if (hex.length() == 9) {
            return new Color(
                    Integer.parseInt(hex.substring(1, 3), 16),
                    Integer.parseInt(hex.substring(3, 5), 16),
                    Integer.parseInt(hex.substring(5, 7), 16),
                    Integer.parseInt(hex.substring(7, 9), 16));
        } else {
            return Color.black;
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
    public MutableColor color = new MutableColor(Color.black);

    protected GraphicPlotter(String hexColor) {
        this.color.value = ColorHexer.decode(hexColor);
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }
}

abstract class GraphicLinePlotter extends GraphicPlotter {
    public MutableInt thickness = new MutableInt(1);

    protected GraphicLinePlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness.value = thickness;
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
            plot(g, x, y, thickness.value);
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
        g.setColor(color.value);

        plotLine(g, p1, p2);
    }

    @Override
    public void debugDraw(Graphics g) {
        debugCircle(g, p1.x, p1.y, debugging == 1);
        debugCircle(g, p2.x, p2.y, debugging == 2);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicLine(ColorHexer.encode(color.value), thickness.value, new Point(p1.x, p1.y),
                new Point(p2.x, p2.y));
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
        g.setColor(color.value);

        Polygon poly = new Polygon();
        for (Point point : points) {
            poly.addPoint(point.x, point.y);
        }
        g.fillPolygon(poly);
    }

    @Override
    public void debugDraw(Graphics g) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            debugCircle(g, point.x, point.y, debugging == i + 1);
        }
    }

    @Override
    public GraphicObject copy() {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : points) {
            newPoints.add(new Point(point.x, point.y));
        }
        return new GraphicPolygon(ColorHexer.encode(color.value), newPoints);
    }
}

class GraphicPolyline extends GraphicLinePlotter {
    public MutableBoolean closed = new MutableBoolean(false);
    public List<Point> points;

    GraphicPolyline(String hexColor, int thickness, boolean closed, Point... points) {
        this(hexColor, thickness, closed, new ArrayList<>(Arrays.asList(points)));
    }

    GraphicPolyline(String hexColor, int thickness, boolean closed, List<Point> points) {
        super(hexColor, thickness);
        this.closed.value = closed;
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color.value);

        for (int i = 1; i < points.size(); i++) {
            plotLine(g, points.get(i - 1), points.get(i));
        }
        if (closed.value) {
            plotLine(g, points.get(points.size() - 1), points.get(0));
        }
    }

    @Override
    public void debugDraw(Graphics g) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            debugCircle(g, point.x, point.y, debugging == i + 1);
        }
    }

    @Override
    public GraphicObject copy() {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : points) {
            newPoints.add(new Point(point.x, point.y));
        }
        return new GraphicPolyline(ColorHexer.encode(color.value), thickness.value, closed.value, newPoints);
    }
}

abstract class GraphicBezierPlotter extends GraphicPlotter {
    private static final int BEZIER_ITERATIONS = 2000;
    public MutableInt thickness = new MutableInt(1);

    protected GraphicBezierPlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness.value = thickness;
    }

    protected void plotBezier(Graphics g, Point pA, Point pB, Point pC, Point pD) {
        for (int i = 0; i < BEZIER_ITERATIONS; i++) {
            double t = i / (double) BEZIER_ITERATIONS;

            double x = Math.pow(1 - t, 3) * pA.x +
                    3 * t * Math.pow(1 - t, 2) * pB.x +
                    3 * Math.pow(t, 2) * (1 - t) * pC.x
                    + Math.pow(t, 3) * pD.x;

            double y = Math.pow(1 - t, 3) * pA.y +
                    3 * t * Math.pow(1 - t, 2) * pB.y +
                    3 * Math.pow(t, 2) * (1 - t) * pC.y
                    + Math.pow(t, 3) * pD.y;

            plot(g, (int) Math.round(x), (int) Math.round(y), thickness.value);
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
        g.setColor(color.value);

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

    @Override
    public void debugDraw(Graphics g) {
        debugLine(g, p1.x, p1.y, p2.x, p2.y, debugging == 2);
        debugCircle(g, p1.x, p1.y, debugging == 1);
        debugDot(g, p2.x, p2.y, debugging == 2);
        for (int i = 1; i < continuedPoints.size(); i += 2) {
            Point controlP = continuedPoints.get(i - 1);
            Point endP = continuedPoints.get(i);
            Point controlEndP = endP;
            if (i + 2 < continuedPoints.size()) {
                controlEndP = new Point(endP.x + (endP.x - controlP.x), endP.y + (endP.y - controlP.y));
            }
            debugLine(g, controlP.x, controlP.y, controlEndP.x, controlEndP.y, debugging == i + 2);
            debugDot(g, controlP.x, controlP.y, debugging == i + 2);
            debugCircle(g, endP.x, endP.y, debugging == i + 3);
        }
    }

    @Override
    public GraphicObject copy() {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : continuedPoints) {
            newPoints.add(new Point(point.x, point.y));
        }
        return new GraphicBezierCurve(ColorHexer.encode(color.value), thickness.value, new Point(p1.x, p1.y),
                new Point(p2.x, p2.y), newPoints);
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

    public PolyBezierData copy() {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : morePoints) {
            newPoints.add(new Point(point.x, point.y));
        }
        return new PolyBezierData(new Point(p2.x, p2.y), newPoints);
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
        g.setColor(color.value);

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

    @Override
    public void debugDraw(Graphics g) {
        debugCircle(g, p1.x, p1.y, debugging == 1);
        Point pNextA = p1;
        int i = 2;
        for (PolyBezierData d : data) {
            debugLine(g, pNextA.x, pNextA.y, d.p2.x, d.p2.y, debugging == i);
            debugDot(g, d.p2.x, d.p2.y, debugging == i);
            i++;
            for (int j = 1; j < d.morePoints.size(); j += 2) {
                Point controlP = d.morePoints.get(j - 1);
                Point endP = d.morePoints.get(j);
                Point controlEndP = endP;
                if (j + 2 < d.morePoints.size()) {
                    controlEndP = new Point(endP.x + (endP.x - controlP.x), endP.y + (endP.y - controlP.y));
                }
                debugLine(g, controlP.x, controlP.y, controlEndP.x, controlEndP.y, debugging == i);
                debugDot(g, controlP.x, controlP.y, debugging == i);
                debugCircle(g, endP.x, endP.y, debugging == i + 1);
                i += 2;
            }
            pNextA = d.morePoints.get(d.morePoints.size() - 1);
        }
    }

    @Override
    public GraphicObject copy() {
        List<PolyBezierData> newData = new ArrayList<>();
        for (PolyBezierData d : data) {
            newData.add(d.copy());
        }
        return new GraphicPolyBezier(ColorHexer.encode(color.value), thickness.value, new Point(p1.x, p1.y),
                newData);
    }

}

// https://stackoverflow.com/q/1734745/3623350
class GraphicCircle extends GraphicBezierPlotter {
    private static final double BEZIER_CIRCLE_CONSTANT = 0.552284749831;
    public Point center;
    public MutableInt radius;

    GraphicCircle(String hexColor, int thickness, Point center, int radius) {
        super(hexColor, thickness);
        this.center = center;
        this.radius = new MutableInt(radius);
    }

    private Point roundPoint(double x, double y) {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color.value);
        double offset = radius.value * BEZIER_CIRCLE_CONSTANT;

        plotBezier(g, roundPoint(center.x, center.y - radius.value),
                roundPoint(center.x + offset, center.y - radius.value),
                roundPoint(center.x + radius.value, center.y - offset),
                roundPoint(center.x + radius.value, center.y));

        plotBezier(g, roundPoint(center.x, center.y + radius.value),
                roundPoint(center.x + offset, center.y + radius.value),
                roundPoint(center.x + radius.value, center.y + offset),
                roundPoint(center.x + radius.value, center.y));

        plotBezier(g, roundPoint(center.x, center.y + radius.value),
                roundPoint(center.x - offset, center.y + radius.value),
                roundPoint(center.x - radius.value, center.y + offset),
                roundPoint(center.x - radius.value, center.y));

        plotBezier(g, roundPoint(center.x, center.y - radius.value),
                roundPoint(center.x - offset, center.y - radius.value),
                roundPoint(center.x - radius.value, center.y - offset),
                roundPoint(center.x - radius.value, center.y));
    }

    @Override
    public void debugDraw(Graphics g) {
        debugLine(g, center.x, center.y, center.x + radius.value, center.y, debugging == 2);
        debugDot(g, center.x + radius.value, center.y, debugging == 2);
        debugCircle(g, center.x, center.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicCircle(ColorHexer.encode(color.value), thickness.value, new Point(center.x, center.y),
                radius.value);
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
        if (buffer.getRGB(point.x, point.y) == color.value.getRGB()) {
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
                buffer.setRGB(p.x, p.y, color.value.getRGB());
                q.add(new Point(p.x + 1, p.y));
                q.add(new Point(p.x - 1, p.y));
                q.add(new Point(p.x, p.y + 1));
                q.add(new Point(p.x, p.y - 1));
            }
        }
    }

    @Override
    public void debugDraw(Graphics g) {
        debugCircle(g, point.x, point.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicFloodFill(ColorHexer.encode(color.value), new Point(point.x, point.y));
    }
}

class GraphicImage extends GraphicObject {
    public MutableString filePath;
    private String lastFilePath = "";
    public BufferedImage image;
    public Point origin;
    public Dimension size;
    public MutableDouble opacity;

    GraphicImage(String filePath, Point origin, Dimension size, double opacity) {
        this.filePath = new MutableString(filePath);
        this.origin = origin;
        this.size = size;
        this.opacity = new MutableDouble(opacity);
        updateImage();
    }

    private void updateImage() {
        if (lastFilePath.equals(filePath.value)) {
            return;
        }
        try {
            this.image = ImageIO.read(new File(this.filePath.value));
        } catch (Exception e) {
            this.image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            g.setColor(Color.red);
            g.fillRect(0, 0, 50, 50);
            g.setColor(Color.white);
            g.drawString("ERROR", 0, 0);
        }
        lastFilePath = filePath.value;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics2D g = (Graphics2D) buffer.createGraphics();

        updateImage();

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity.value);
        g.setComposite(alphaComposite);
        g.drawImage(image, origin.x, origin.y, size.width, size.height, null);
    }

    @Override
    public void debugDraw(Graphics g) {
        debugLine(g, origin.x, origin.y, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugDot(g, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugCircle(g, origin.x, origin.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicImage(filePath.value, new Point(origin.x, origin.y), new Dimension(size.width, size.height),
                opacity.value);
    }
}

class PannerPanelXListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Point point;
    private JSpinner spinner;
    private int startX = 0;
    private int originX = 0;

    PannerPanelXListener(JSpinner spinner, Point point) {
        this.spinner = spinner;
        this.point = point;
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startX = e.getX();
            originX = point.x;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originX + (e.getX() - startX) / 8);
        } else {
            spinner.setValue(originX + (e.getX() - startX));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        originX = point.x;
        e.getComponent().requestFocusInWindow();
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
    private boolean slowed = false;
    private Point point;
    private JSpinner spinner;
    private int startY = 0;
    private int originY = 0;

    PannerPanelYListener(JSpinner spinner, Point point) {
        this.spinner = spinner;
        this.point = point;
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startY = e.getY();
            originY = point.y;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originY + (e.getY() - startY) / 8);
        } else {
            spinner.setValue(originY + (e.getY() - startY));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        originY = point.y;
        e.getComponent().requestFocusInWindow();
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

class PannerPanelWListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Dimension dim;
    private JSpinner spinner;
    private int startX = 0;
    private int originX = 0;

    PannerPanelWListener(JSpinner spinner, Dimension dim) {
        this.spinner = spinner;
        this.dim = dim;
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startX = e.getX();
            originX = dim.width;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originX + (e.getX() - startX) / 8);
        } else {
            spinner.setValue(originX + (e.getX() - startX));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        originX = dim.width;
        e.getComponent().requestFocusInWindow();
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

class PannerPanelHListener implements MouseMotionListener, MouseListener {
    private boolean slowed = false;
    private Dimension dim;
    private JSpinner spinner;
    private int startY = 0;
    private int originY = 0;

    PannerPanelHListener(JSpinner spinner, Dimension dim) {
        this.spinner = spinner;
        this.dim = dim;
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (GlobalState.pannerPanelSlow != slowed) {
            startY = e.getY();
            originY = dim.height;
            slowed = GlobalState.pannerPanelSlow;
        }

        if (GlobalState.pannerPanelSlow) {
            spinner.setValue(originY + (e.getY() - startY) / 8);
        } else {
            spinner.setValue(originY + (e.getY() - startY));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        originY = dim.height;
        e.getComponent().requestFocusInWindow();
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

class MouseMover {
    // https://stackoverflow.com/a/10665280/3623350
    public static void moveMouse(int x, int y) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        // Search the devices for the one that draws the specified point.
        for (GraphicsDevice device : gs) {
            GraphicsConfiguration[] configurations = device.getConfigurations();
            for (GraphicsConfiguration config : configurations) {
                Rectangle bounds = config.getBounds();
                if (bounds.contains(x, y)) {
                    // Set point to screen coordinates.
                    Point b = bounds.getLocation();
                    Point s = new Point(x - b.x, y - b.y);

                    try {
                        Robot r = new Robot(device);
                        r.mouseMove(s.x, s.y);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        }
        // Couldn't move to the point, it may be off screen.
        return;
    }
}

class PannerPanelDebuggingHoverListener implements MouseListener {
    private GraphicObject obj;
    private int debugValue;
    private int cursorStartX = 0;
    private int cursorStartY = 0;

    PannerPanelDebuggingHoverListener(GraphicObject obj, int debugValue) {
        this.obj = obj;
        this.debugValue = debugValue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.debugging = debugValue;

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.debugging = -1;

    }

    @Override
    public void mousePressed(MouseEvent e) {
        GlobalState.pannerPanelDragging = true;
        cursorStartX = e.getXOnScreen();
        cursorStartY = e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlobalState.pannerPanelDragging = false;
        MouseMover.moveMouse(cursorStartX, cursorStartY);
    }

}

class GlobalState {
    public static boolean pannerPanelDragging = false;
    public static boolean pannerPanelSlow = false;
    public static boolean pannerShowDebugging = false;

    public static boolean needsUpdateEditor = false;
    public static boolean needsUpdateLayers = false;
}

class DebuggingHoverListener implements MouseListener {
    private GraphicObject obj;
    private int debugValue;

    DebuggingHoverListener(GraphicObject obj, int debugValue) {
        this.obj = obj;
        this.debugValue = debugValue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.debugging = debugValue;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.debugging = -1;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}

class ColorButton extends JButton {
    static int squareSize = 10;
    private Color color;

    public ColorButton() {
        this.setText(" ");
    }

    public void setColor(Color color) {
        this.color = color;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < getWidth(); i += squareSize) {
            for (int j = 0; j < getHeight(); j += squareSize) {
                g.setColor((i / squareSize + j / squareSize) % 2 == 0 ? Color.lightGray : Color.white);
                g.fillRect(i, j, squareSize, squareSize);
            }
        }
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(color.getRGB(), false));
        g.fillRect(0, 0, getWidth() / 3, getHeight());
    }
}

class EditingPanelFactory {

    private EditingPanelFactory() {
    }

    private static void addPannerKeybinds(JComponent comp) {
        var inputMap = comp.getInputMap();
        var actionMap = comp.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "slow pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "slow released");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "debug pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "debug released");

        actionMap.put("slow pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalState.pannerPanelSlow = true;
            }
        });

        actionMap.put("slow released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalState.pannerPanelSlow = false;
            }
        });

        actionMap.put("debug pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalState.pannerShowDebugging = true;
            }
        });

        actionMap.put("debug released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalState.pannerShowDebugging = false;
            }
        });

    }

    public static JPanel create(String labelText, Point point, GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        SpinnerNumberModel xModel = new SpinnerNumberModel();
        xModel.setValue(point.x);
        SpinnerNumberModel yModel = new SpinnerNumberModel();
        yModel.setValue(point.y);
        JSpinner xSpinner = new JSpinner(xModel);
        xSpinner.addChangeListener(e -> {
            point.x = (int) xSpinner.getValue();
            obj.changed = true;
        });
        JSpinner ySpinner = new JSpinner(yModel);
        ySpinner.addChangeListener(e -> {
            point.y = (int) ySpinner.getValue();
            obj.changed = true;
        });

        JPanel pannerPanel = new JPanel();
        pannerPanel.setPreferredSize(new Dimension(20, 20));
        pannerPanel.setMaximumSize(new Dimension(20, 20));
        pannerPanel.setMinimumSize(new Dimension(20, 20));
        pannerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        pannerPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        pannerPanel.setFocusable(true);
        addPannerKeybinds(pannerPanel);

        JPanel pannerXPanel = new JPanel();
        pannerXPanel.setPreferredSize(new Dimension(20, 8));
        pannerXPanel.setMaximumSize(new Dimension(20, 8));
        pannerXPanel.setMinimumSize(new Dimension(20, 8));
        pannerXPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerXPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        pannerXPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        pannerXPanel.setFocusable(true);
        addPannerKeybinds(pannerXPanel);

        JPanel pannerYPanel = new JPanel();
        pannerYPanel.setPreferredSize(new Dimension(8, 20));
        pannerYPanel.setMaximumSize(new Dimension(8, 20));
        pannerYPanel.setMinimumSize(new Dimension(8, 20));
        pannerYPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerYPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        pannerYPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        pannerYPanel.setFocusable(true);
        addPannerKeybinds(pannerYPanel);

        var xListener = new PannerPanelXListener(xSpinner, point);
        var yListener = new PannerPanelYListener(ySpinner, point);
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

    public static JPanel create(String labelText, Dimension dim, GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        SpinnerNumberModel wModel = new SpinnerNumberModel();
        wModel.setValue(dim.width);
        SpinnerNumberModel hModel = new SpinnerNumberModel();
        hModel.setValue(dim.height);
        JSpinner wSpinner = new JSpinner(wModel);

        wSpinner.addChangeListener(e -> {
            dim.width = (int) wSpinner.getValue();
            obj.changed = true;
        });

        JSpinner hSpinner = new JSpinner(hModel);
        hSpinner.addChangeListener(e -> {
            dim.height = (int) hSpinner.getValue();
            obj.changed = true;
        });

        JPanel pannerPanel = new JPanel();
        pannerPanel.setPreferredSize(new Dimension(20, 20));
        pannerPanel.setMaximumSize(new Dimension(20, 20));
        pannerPanel.setMinimumSize(new Dimension(20, 20));
        pannerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        pannerPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        addPannerKeybinds(pannerPanel);

        JPanel pannerXPanel = new JPanel();
        pannerXPanel.setPreferredSize(new Dimension(20, 8));
        pannerXPanel.setMaximumSize(new Dimension(20, 8));
        pannerXPanel.setMinimumSize(new Dimension(20, 8));
        pannerXPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerXPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        pannerXPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        addPannerKeybinds(pannerXPanel);

        JPanel pannerYPanel = new JPanel();
        pannerYPanel.setPreferredSize(new Dimension(8, 20));
        pannerYPanel.setMaximumSize(new Dimension(8, 20));
        pannerYPanel.setMinimumSize(new Dimension(8, 20));
        pannerYPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerYPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        pannerYPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj, debugValue));
        addPannerKeybinds(pannerYPanel);

        var xListener = new PannerPanelWListener(wSpinner, dim);
        var yListener = new PannerPanelHListener(hSpinner, dim);
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
                .addComponent(wSpinner)
                .addComponent(hSpinner)
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
                                .addComponent(wSpinner)
                                .addComponent(hSpinner)
                                .addComponent(pannerYPanel)
                                .addComponent(pannerPanel)));

        return panel;
    }

    public static JPanel create(String labelText, MutableString str, GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JTextField textField = new JTextField(str.value);
        if (obj == null) {
            textField.addActionListener(e -> {
                str.value = textField.getText();
                GlobalState.needsUpdateLayers = true;
            });
        } else {
            textField.addActionListener(e -> {
                str.value = textField.getText();
                obj.changed = true;
            });
        }

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(textField));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(textField));

        return panel;
    }

    public static JPanel create(String labelText, MutableDouble doub, double min, double max, double stepSize,
            GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (int) ((max - min) / stepSize);
        JSlider slider = new JSlider(0, sliderSteps, (int) ((doub.value - min) / stepSize));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(doub.value, min, max, stepSize));
        spinner.addChangeListener(e -> {
            doub.value = (double) spinner.getValue();
            slider.setValue((int) ((doub.value - min) / stepSize));
            obj.changed = true;
        });
        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
            obj.changed = true;
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(slider)
                .addComponent(spinner));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(slider)
                        .addComponent(spinner));

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableInt integer, int min, int max, int stepSize, GraphicObject obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (max - min) / stepSize;
        JSlider slider = new JSlider(0, sliderSteps, (integer.value - min) / stepSize);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(integer.value, min, Integer.MAX_VALUE, stepSize));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(2);
        spinner.addChangeListener(e -> {
            integer.value = (int) spinner.getValue();
            slider.setValue((integer.value - min) / stepSize);
            obj.changed = true;
        });
        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
            obj.changed = true;
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(slider)
                .addComponent(spinner));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(slider)
                        .addComponent(spinner));

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableColor color, GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        ColorButton button = new ColorButton();
        button.setColor(color.value);

        JTextField textField = new JTextField(ColorHexer.encode(color.value));

        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", color.value);
            if (newColor != null) {
                color.value = newColor;
                button.setColor(color.value);
                textField.setText(ColorHexer.encode(color.value));
                obj.changed = true;
            }
        });
        textField.addActionListener(e -> {
            color.value = ColorHexer.decode(textField.getText());
            button.setColor(color.value);
            obj.changed = true;
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(button)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(textField));

        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(button)
                        .addComponent(textField));

        button.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableBoolean bool, GraphicObject obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(bool.value);
        checkBox.addActionListener(e -> {
            bool.value = checkBox.isSelected();
            obj.changed = true;
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(checkBox));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(checkBox));

        checkBox.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(GraphicLayer layer) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        var vGroup = layout.createSequentialGroup();
        var hGroup = layout.createParallelGroup();
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(10)
                .addGroup(hGroup)
                .addGap(10));
        vGroup.addGap(5);
        layout.setVerticalGroup(vGroup);

        var layerNamePanel = create("layer", layer.name, null, 0);
        vGroup.addComponent(layerNamePanel);
        hGroup.addComponent(layerNamePanel);
        for (var gObj : layer.objects) {
            var objPanel = create(gObj);
            vGroup.addPreferredGap(ComponentPlacement.RELATED);
            vGroup.addComponent(objPanel);
            hGroup.addComponent(objPanel);
        }

        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("GraphicLine");
        comboBox.addItem("GraphicPolyline");
        comboBox.addItem("GraphicPolygon");
        comboBox.addItem("GraphicBezierCurve");
        comboBox.addItem("GraphicPolyBezier");
        comboBox.addItem("GraphicCircle");
        comboBox.addItem("GraphicFloodFill");
        comboBox.addItem("GraphicImage");

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            Predicate<? super GraphicObject> pred;
            GraphicObject defaultObj;
            switch ((String) comboBox.getSelectedItem()) {
                case "GraphicLine":
                    pred = obj -> obj instanceof GraphicLine;
                    defaultObj = (new GraphicLine("#000000", 1, new Point(0, 0), new Point(50, 50)));
                    break;
                case "GraphicPolyline":
                    pred = obj -> obj instanceof GraphicPolyline;
                    defaultObj = new GraphicPolyline("#000000", 1, false,
                            new Point(0, 0), new Point(50, 50));
                    break;
                case "GraphicPolygon":
                    pred = obj -> obj instanceof GraphicPolygon;
                    defaultObj = new GraphicPolygon("#000000",
                            new Point(0, 0), new Point(50, 50), new Point(0, 50));
                    break;
                case "GraphicBezierCurve":
                    pred = obj -> obj instanceof GraphicBezierCurve;
                    defaultObj = new GraphicBezierCurve("#000000", 1, new Point(0, 0), new Point(50, 0),
                            new Point(50, 50),
                            new Point(0, 50));
                    break;
                case "GraphicPolyBezier":
                    pred = obj -> obj instanceof GraphicPolyBezier;
                    defaultObj = new GraphicPolyBezier("#000000", 1,
                            new Point(0, 0),
                            new PolyBezierData(new Point(50, 0),
                                    new Point(50, 50), new Point(0, 50)));
                    break;
                case "GraphicCircle":
                    pred = obj -> obj instanceof GraphicCircle;
                    defaultObj = new GraphicCircle("#000000", 1, new Point(0, 0), 50);
                    break;
                case "GraphicFloodFill":
                    pred = obj -> obj instanceof GraphicFloodFill;
                    defaultObj = new GraphicFloodFill("#000000", new Point(0, 0));
                    break;
                case "GraphicImage":
                    pred = obj -> obj instanceof GraphicImage;
                    defaultObj = new GraphicImage("image.png", new Point(0, 0), new Dimension(50, 50), 1.0);
                    break;
                default:
                    return;
            }

            GlobalState.needsUpdateEditor = true;

            var streamResult = layer.objects.stream().filter(pred).reduce((a, b) -> b);
            if (!streamResult.isPresent()) {
                layer.objects.add(defaultObj);
                return;
            }
            switch ((String) comboBox.getSelectedItem()) {
                case "GraphicLine": {
                    var result = (GraphicLine) streamResult.get().copy();
                    result.p1.translate(10, 10);
                    result.p2.translate(10, 10);
                    layer.add(result);
                    break;
                }
                case "GraphicPolyline": {
                    var result = (GraphicPolyline) streamResult.get().copy();
                    result.points.subList(2, result.points.size()).clear();
                    for (Point point : result.points) {
                        point.translate(10, 10);
                    }
                    layer.add(result);
                    break;
                }
                case "GraphicPolygon": {
                    var result = (GraphicPolygon) streamResult.get().copy();
                    result.points.subList(3, result.points.size()).clear();
                    for (Point point : result.points) {
                        point.translate(10, 10);
                    }
                    layer.add(result);
                    break;
                }
                case "GraphicBezierCurve": {
                    var result = (GraphicBezierCurve) streamResult.get().copy();
                    result.p1.translate(10, 10);
                    result.p2.translate(10, 10);
                    result.continuedPoints.subList(2, result.continuedPoints.size()).clear();
                    for (Point point : result.continuedPoints) {
                        point.translate(10, 10);
                    }
                    layer.add(result);
                    break;
                }
                case "GraphicPolyBezier": {
                    var result = (GraphicPolyBezier) streamResult.get().copy();
                    result.data.subList(1, result.data.size()).clear();
                    result.p1.translate(10, 10);
                    for (var d : result.data) {
                        d.p2.translate(10, 10);
                        d.morePoints.subList(2, d.morePoints.size()).clear();
                        for (var p : d.morePoints) {
                            p.translate(10, 10);
                        }
                    }
                    layer.add(result);
                    break;
                }
                case "GraphicCircle": {
                    var result = (GraphicCircle) streamResult.get().copy();
                    result.center.translate(10, 10);
                    layer.add(result);
                    break;
                }
                case "GraphicFloodFill": {
                    var result = (GraphicFloodFill) streamResult.get().copy();
                    layer.add(result);
                    break;
                }
                case "GraphicImage": {
                    var result = (GraphicImage) streamResult.get().copy();
                    result.origin.translate(10, 10);
                    layer.add(result);
                    break;
                }
            }

        });

        vGroup.addPreferredGap(ComponentPlacement.RELATED);
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER, false)
                .addComponent(comboBox)
                .addComponent(addButton));
        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(comboBox)
                .addComponent(addButton));
        return panel;
    }

    public static JPanel create(GraphicObject object) {
        if (object instanceof GraphicLine) {
            return create((GraphicLine) object);
        } else if (object instanceof GraphicPolyline) {
            return create((GraphicPolyline) object);
        } else if (object instanceof GraphicPolygon) {
            return create((GraphicPolygon) object);
        } else if (object instanceof GraphicBezierCurve) {
            return create((GraphicBezierCurve) object);
        } else if (object instanceof GraphicPolyBezier) {
            return create((GraphicPolyBezier) object);
        } else if (object instanceof GraphicCircle) {
            return create((GraphicCircle) object);
        } else if (object instanceof GraphicFloodFill) {
            return create((GraphicFloodFill) object);
        } else if (object instanceof GraphicImage) {
            return create((GraphicImage) object);
        } else {
            return new JPanel();
        }
    }

    public static JPanel create(GraphicLine line) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicLine");
        var colorPanel = create("color", line.color, line, 0);
        var thicknessPanel = create("thickness", line.thickness, 1, 15, 1, line, 0);
        var p1Panel = create("p1", line.p1, line, 1);
        var p2Panel = create("p2", line.p2, line, 2);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(colorPanel)
                .addComponent(thicknessPanel)
                .addComponent(p1Panel)
                .addComponent(p2Panel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(colorPanel)
                .addGap(2)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(p1Panel)
                .addGap(2)
                .addComponent(p2Panel));

        panel.addMouseListener(new DebuggingHoverListener(line, 0));
        p1Panel.addMouseListener(new DebuggingHoverListener(line, 1));
        p2Panel.addMouseListener(new DebuggingHoverListener(line, 2));

        return panel;
    }

    public static JPanel create(GraphicPolyline polyline) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPolyline");
        var colorPanel = create("color", polyline.color, polyline, 0);
        var thicknessPanel = create("thickness", polyline.thickness, 1, 15, 1, polyline, 0);
        var closedPanel = create("closed", polyline.closed, polyline, 0);

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        addButton.addActionListener(e -> {
            polyline.points.add(new Point(polyline.points.get(polyline.points.size() - 1).x + 20,
                    polyline.points.get(polyline.points.size() - 1).y + 20));
            polyline.changed = true;
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (polyline.points.size() > 2) {
                polyline.points.remove(polyline.points.size() - 1);
                polyline.changed = true;
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(polyline.points.size() > 2);

        var pointsHGroup = layout.createParallelGroup();
        var pointsVGroup = layout.createSequentialGroup();

        for (int i = 0; i < polyline.points.size(); i++) {
            var pointPanel = create("p" + i, polyline.points.get(i), polyline, i + 1);
            pointsVGroup.addPreferredGap(ComponentPlacement.RELATED);
            pointsVGroup.addComponent(pointPanel);
            pointsHGroup.addComponent(pointPanel);
            pointPanel.addMouseListener(new DebuggingHoverListener(polyline, i + 1));
        }

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(colorPanel)
                .addComponent(thicknessPanel)
                .addComponent(closedPanel)
                .addGroup(pointsHGroup)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(colorPanel)
                .addGap(2)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(closedPanel)
                .addGap(2)
                .addGroup(pointsVGroup)
                .addGap(2)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        panel.addMouseListener(new DebuggingHoverListener(polyline, 0));

        return panel;
    }

    public static JPanel create(GraphicPolygon polygon) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPolygon");
        var colorPanel = create("color", polygon.color, polygon, 0);

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        addButton.addActionListener(e -> {
            polygon.points.add(new Point(polygon.points.get(polygon.points.size() - 1).x + 20,
                    polygon.points.get(polygon.points.size() - 1).y + 20));
            polygon.changed = true;
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (polygon.points.size() > 3) {
                polygon.points.remove(polygon.points.size() - 1);
                polygon.changed = true;
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(polygon.points.size() > 3);

        var pointsHGroup = layout.createParallelGroup();
        var pointsVGroup = layout.createSequentialGroup();

        for (int i = 0; i < polygon.points.size(); i++) {
            var pointPanel = create("p" + i, polygon.points.get(i), polygon, i + 1);
            pointsVGroup.addPreferredGap(ComponentPlacement.RELATED);
            pointsVGroup.addComponent(pointPanel);
            pointsHGroup.addComponent(pointPanel);
            pointPanel.addMouseListener(new DebuggingHoverListener(polygon, i + 1));
        }

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(colorPanel)
                .addGroup(pointsHGroup)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(colorPanel)
                .addGroup(pointsVGroup)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        panel.addMouseListener(new DebuggingHoverListener(polygon, 0));

        return panel;
    }

    public static JPanel create(GraphicBezierCurve bezierCurve) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicBezierCurve");
        var colorPanel = create("color", bezierCurve.color, bezierCurve, 0);
        var thicknessPanel = create("thickness", bezierCurve.thickness, 1, 15, 1, bezierCurve, 0);
        var p1Panel = create("p1", bezierCurve.p1, bezierCurve, 1);
        var p2Panel = create("p2", bezierCurve.p2, bezierCurve, 2);

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        addButton.addActionListener(e -> {
            Point lastP2 = bezierCurve.continuedPoints.get(bezierCurve.continuedPoints.size() - 2);
            Point lastP = bezierCurve.continuedPoints.get(bezierCurve.continuedPoints.size() - 1);

            bezierCurve.continuedPoints.add(new Point(lastP2.x + 20, lastP2.y + 20));
            bezierCurve.continuedPoints.add(new Point(lastP.x + 20, lastP.y + 20));
            bezierCurve.changed = true;
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (bezierCurve.continuedPoints.size() > 2) {
                bezierCurve.continuedPoints.remove(bezierCurve.continuedPoints.size() - 1);
                bezierCurve.continuedPoints.remove(bezierCurve.continuedPoints.size() - 1);
                bezierCurve.changed = true;
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(bezierCurve.continuedPoints.size() > 2);

        var hGroup = layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(thicknessPanel)
                .addComponent(colorPanel)
                .addComponent(p1Panel)
                .addComponent(p2Panel);

        var vGroup = layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(colorPanel)
                .addGap(2)
                .addComponent(p1Panel)
                .addGap(2)
                .addComponent(p2Panel)
                .addGap(2);

        int i = 3;
        for (Point point : bezierCurve.continuedPoints) {
            var pointPanel = create("p" + i, point, bezierCurve, i);
            hGroup.addComponent(pointPanel);
            vGroup.addGap(2);
            vGroup.addComponent(pointPanel);
            pointPanel.addMouseListener(new DebuggingHoverListener(bezierCurve, i));

            i++;
        }

        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(addButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(addButton)
                .addComponent(minusButton));

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        panel.addMouseListener(new DebuggingHoverListener(bezierCurve, 0));
        p1Panel.addMouseListener(new DebuggingHoverListener(bezierCurve, 1));
        p2Panel.addMouseListener(new DebuggingHoverListener(bezierCurve, 2));

        return panel;
    }

    public static JPanel create(PolyBezierData pbd, GraphicPolyBezier polyBezier, int pbdIndex, int debuggingStartI) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        char pointLetter = (char) (97 + (pbdIndex + 15) % 26);

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        addButton.addActionListener(e -> {
            var newCp = new Point(pbd.morePoints.get(pbd.morePoints.size() - 2).x + 20,
                    pbd.morePoints.get(pbd.morePoints.size() - 2).y + 20);
            var newPp = new Point(pbd.morePoints.get(pbd.morePoints.size() - 1).x + 20,
                    pbd.morePoints.get(pbd.morePoints.size() - 1).y + 20);
            pbd.morePoints.add(newCp);
            pbd.morePoints.add(newPp);
            polyBezier.changed = true;
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (pbd.morePoints.size() > 2) {
                pbd.morePoints.remove(pbd.morePoints.size() - 1);
                pbd.morePoints.remove(pbd.morePoints.size() - 1);
                polyBezier.changed = true;
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(pbd.morePoints.size() > 2);

        var pointsHGroup = layout.createParallelGroup();
        var pointsVGroup = layout.createSequentialGroup();

        var p2Panel = create(pointLetter + "2", pbd.p2, polyBezier, debuggingStartI);
        pointsVGroup.addPreferredGap(ComponentPlacement.RELATED);
        pointsVGroup.addComponent(p2Panel);
        pointsHGroup.addComponent(p2Panel);
        p2Panel.addMouseListener(new DebuggingHoverListener(polyBezier, debuggingStartI));

        for (int i = 0; i < pbd.morePoints.size(); i++) {
            var pointPanel = create(pointLetter + "" + (i + 3), pbd.morePoints.get(i), polyBezier,
                    i + debuggingStartI + 1);
            pointsVGroup.addPreferredGap(ComponentPlacement.RELATED);
            pointsVGroup.addComponent(pointPanel);
            pointsHGroup.addComponent(pointPanel);
            pointPanel.addMouseListener(new DebuggingHoverListener(polyBezier, i + debuggingStartI + 1));
        }

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(pointsHGroup)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(pointsVGroup)
                .addGap(2)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(addButton)
                        .addComponent(minusButton)));

        return panel;
    }

    public static JPanel create(GraphicPolyBezier polyBezier) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPolyBezier");
        var colorPanel = create("color", polyBezier.color, polyBezier, 0);
        var thicknessPanel = create("thickness", polyBezier.thickness, 1, 15, 1, polyBezier, 0);
        var p1Panel = create("p1", polyBezier.p1, polyBezier, 1);

        JButton addButton = new JButton("+++");
        JButton minusButton = new JButton("---");
        addButton.addActionListener(e -> {
            var pbd = polyBezier.data.get(polyBezier.data.size() - 1).copy();
            pbd.p2.translate(20, 20);
            for (Point point : pbd.morePoints) {
                point.translate(20, 20);
            }
            polyBezier.data.add(pbd);
            polyBezier.changed = true;
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (polyBezier.data.size() > 1) {
                polyBezier.data.remove(polyBezier.data.size() - 1);
                polyBezier.changed = true;
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(polyBezier.data.size() > 1);

        var hGroup = layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(thicknessPanel)
                .addComponent(colorPanel)
                .addComponent(p1Panel);

        var vGroup = layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(colorPanel)
                .addGap(2)
                .addComponent(p1Panel)
                .addGap(2);

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        int i = 0;
        int debuggingStartI = 2;
        for (PolyBezierData pbd : polyBezier.data) {
            var pbdPanel = create(pbd, polyBezier, i, debuggingStartI);
            hGroup.addComponent(pbdPanel);
            vGroup.addGap(2);
            vGroup.addComponent(pbdPanel);
            i++;
            debuggingStartI += pbd.morePoints.size() + 1;
        }

        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(addButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(addButton)
                .addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(polyBezier, 0));
        p1Panel.addMouseListener(new DebuggingHoverListener(polyBezier, 1));

        return panel;
    }

    public static JPanel create(GraphicCircle circle) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicCircle");
        var colorPanel = create("color", circle.color, circle, 0);
        var thicknessPanel = create("thickness", circle.thickness, 1, 15, 1, circle, 0);
        var pointPanel = create("center", circle.center, circle, 1);
        var radiusPanel = create("radius", circle.radius, 0, 50, 1, circle, 2);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(colorPanel)
                .addComponent(thicknessPanel)
                .addComponent(pointPanel)
                .addComponent(radiusPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(colorPanel)
                .addGap(2)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(pointPanel)
                .addGap(2)
                .addComponent(radiusPanel));

        panel.addMouseListener(new DebuggingHoverListener(circle, 0));
        pointPanel.addMouseListener(new DebuggingHoverListener(circle, 1));
        radiusPanel.addMouseListener(new DebuggingHoverListener(circle, 2));

        return panel;
    }

    public static JPanel create(GraphicFloodFill floodFill) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicFloodFill");
        var colorPanel = create("color", floodFill.color, floodFill, 0);
        var pointPanel = create("point", floodFill.point, floodFill, 1);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(colorPanel)
                .addComponent(pointPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(colorPanel)
                .addComponent(pointPanel));

        panel.addMouseListener(new DebuggingHoverListener(floodFill, 0));
        pointPanel.addMouseListener(new DebuggingHoverListener(floodFill, 1));

        return panel;
    }

    public static JPanel create(GraphicImage image) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicImage");

        var filePathPanel = create("file path", image.filePath, image, 0);

        var originPanel = create("origin", image.origin, image, 1);
        var sizePanel = create("size", image.size, image, 2);

        var opacityPanel = create("opacity", image.opacity, 0.0, 1.0, 0.01, image, 0);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(filePathPanel)
                .addComponent(originPanel)
                .addComponent(sizePanel)
                .addComponent(opacityPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(filePathPanel)
                .addGap(2)
                .addComponent(originPanel)
                .addGap(2)
                .addComponent(sizePanel)
                .addGap(2)
                .addComponent(opacityPanel));

        panel.addMouseListener(new DebuggingHoverListener(image, 0));
        originPanel.addMouseListener(new DebuggingHoverListener(image, 1));
        sizePanel.addMouseListener(new DebuggingHoverListener(image, 2));

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

class ImportExport {

    public static String exportString(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append(exportString(layer));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String exportString(GraphicLayer layer) {
        StringBuilder sb = new StringBuilder();
        sb.append("LAYER ");
        sb.append(layer.name);
        sb.append("\n");
        sb.append("VISIBLE ");
        sb.append(exportString(layer.shown));
        sb.append("\n");
        for (GraphicObject object : layer.objects) {
            sb.append(exportString(object));
            sb.append("\n");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(GraphicObject object) {
        if (object instanceof GraphicLine) {
            return exportString((GraphicLine) object);
        } else if (object instanceof GraphicPolyline) {
            return exportString((GraphicPolyline) object);
        } else if (object instanceof GraphicPolygon) {
            return exportString((GraphicPolygon) object);
        } else if (object instanceof GraphicBezierCurve) {
            return exportString((GraphicBezierCurve) object);
        } else if (object instanceof GraphicPolyBezier) {
            return exportString((GraphicPolyBezier) object);
        } else if (object instanceof GraphicCircle) {
            return exportString((GraphicCircle) object);
        } else if (object instanceof GraphicFloodFill) {
            return exportString((GraphicFloodFill) object);
        } else if (object instanceof GraphicImage) {
            return exportString((GraphicImage) object);
        } else {
            return "";
        }
    }

    public static String exportString(GraphicLine line) {
        StringBuilder sb = new StringBuilder();
        sb.append("LINE ");
        sb.append(exportString(line.color.value));
        sb.append(" ");
        sb.append(line.thickness.value);
        sb.append(" ");
        sb.append(exportString(line.p1));
        sb.append(" ");
        sb.append(exportString(line.p2));
        return sb.toString();
    }

    public static String exportString(GraphicPolyline polyline) {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYLINE ");
        sb.append(exportString(polyline.color.value));
        sb.append(" ");
        sb.append(polyline.thickness.value);
        sb.append(" ");
        sb.append(exportString(polyline.closed.value));
        sb.append(" ");
        for (Point point : polyline.points) {
            sb.append(exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(GraphicPolygon polygon) {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON ");
        sb.append(exportString(polygon.color.value));
        sb.append(" ");
        for (Point point : polygon.points) {
            sb.append(exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(GraphicBezierCurve bezierCurve) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEZIERCURVE ");
        sb.append(exportString(bezierCurve.color.value));
        sb.append(" ");
        sb.append(bezierCurve.thickness.value);
        sb.append(" ");
        sb.append(exportString(bezierCurve.p1));
        sb.append(" ");
        sb.append(exportString(bezierCurve.p2));
        sb.append(" ");
        for (Point point : bezierCurve.continuedPoints) {
            sb.append(exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(PolyBezierData pbd) {
        StringBuilder sb = new StringBuilder();
        sb.append(exportString(pbd.p2));
        sb.append(" ");
        for (Point point : pbd.morePoints) {
            sb.append(exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(GraphicPolyBezier polyBezier) {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYBEZIER ");
        sb.append(exportString(polyBezier.color.value));
        sb.append(" ");
        sb.append(polyBezier.thickness.value);
        sb.append(" ");
        sb.append(exportString(polyBezier.p1));
        sb.append("\n");
        for (PolyBezierData pbd : polyBezier.data) {
            sb.append(exportString(pbd));
            sb.append("\n");
        }
        sb.append("END");
        return sb.toString();
    }

    public static String exportString(GraphicCircle circle) {
        StringBuilder sb = new StringBuilder();
        sb.append("CIRCLE ");
        sb.append(exportString(circle.color.value));
        sb.append(" ");
        sb.append(circle.thickness.value);
        sb.append(" ");
        sb.append(exportString(circle.center));
        sb.append(" ");
        sb.append(circle.radius.value);
        return sb.toString();
    }

    public static String exportString(GraphicFloodFill floodFill) {
        StringBuilder sb = new StringBuilder();
        sb.append("FLOODFILL ");
        sb.append(exportString(floodFill.color.value));
        sb.append(" ");
        sb.append(exportString(floodFill.point));
        return sb.toString();
    }

    public static String exportString(Color color) {
        return ColorHexer.encode(color);
    }

    public static String exportString(Point point) {
        StringBuilder sb = new StringBuilder();
        sb.append(point.x);
        sb.append(",");
        sb.append(point.y);
        return sb.toString();
    }

    public static String exportString(Dimension dim) {
        StringBuilder sb = new StringBuilder();
        sb.append(dim.width);
        sb.append(",");
        sb.append(dim.height);
        return sb.toString();
    }

    public static String exportString(GraphicImage image) {
        StringBuilder sb = new StringBuilder();
        sb.append("IMAGE ");
        sb.append(image.filePath.value);
        sb.append("\n");
        sb.append(exportString(image.origin));
        sb.append(" ");
        sb.append(exportString(image.size));
        sb.append(" ");
        sb.append(image.opacity.value);
        return sb.toString();
    }

    public static String exportString(boolean bool) {
        return bool ? "T" : "F";
    }

    public static List<GraphicLayer> importString(String str) {
        Scanner sc = new Scanner(str);
        List<GraphicLayer> layers = importLayers(sc);
        sc.close();
        return layers;
    }

    public static List<GraphicLayer> importLayers(Scanner sc) {
        List<GraphicLayer> layers = new ArrayList<>();
        while (sc.hasNext()) {
            layers.add(importLayer(sc));
        }
        return layers;
    }

    public static GraphicLayer importLayer(Scanner sc) {
        sc.skip("LAYER ");
        String layerName = sc.nextLine();
        sc.skip("VISIBLE ");
        boolean visible = sc.nextLine().equals("T");
        List<GraphicObject> objects = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
                case "LINE":
                    objects.add(importLine(sc));
                    break;
                case "POLYLINE":
                    objects.add(importPolyline(sc));
                    break;
                case "POLYGON":
                    objects.add(importPolygon(sc));
                    break;
                case "BEZIERCURVE":
                    objects.add(importBezierCurve(sc));
                    break;
                case "POLYBEZIER":
                    objects.add(importPolyBezier(sc));
                    break;
                case "CIRCLE":
                    objects.add(importCircle(sc));
                    break;
                case "FLOODFILL":
                    objects.add(importFloodFill(sc));
                    break;
                case "IMAGE":
                    objects.add(importImage(sc));
                    break;
            }
        }
        sc.skip("[ \\n\\r]*");
        var layer = new GraphicLayer(layerName, objects);
        layer.shown = visible;
        return layer;
    }

    public static GraphicLine importLine(Scanner sc) {
        String hexColor = sc.next();
        int thickness = sc.nextInt();
        Point p1 = importPoint(sc);
        Point p2 = importPoint(sc);
        return new GraphicLine(hexColor, thickness, p1, p2);
    }

    public static GraphicPolyline importPolyline(Scanner sc) {
        String hexColor = sc.next();
        int thickness = sc.nextInt();
        boolean closed = sc.next().equals("T");
        List<Point> points = new ArrayList<>();
        while (true) {
            points.add(importPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new GraphicPolyline(hexColor, thickness, closed, points);
    }

    public static GraphicPolygon importPolygon(Scanner sc) {
        String hexColor = sc.next();
        List<Point> points = new ArrayList<>();
        while (true) {
            points.add(importPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new GraphicPolygon(hexColor, points);
    }

    public static GraphicBezierCurve importBezierCurve(Scanner sc) {
        String hexColor = sc.next();
        int thickness = sc.nextInt();
        Point p1 = importPoint(sc);
        Point p2 = importPoint(sc);
        List<Point> points = new ArrayList<>();
        while (true) {
            points.add(importPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new GraphicBezierCurve(hexColor, thickness, p1, p2, points);
    }

    public static PolyBezierData importPolyBezierData(Scanner sc) {
        Point p2 = importPoint(sc);
        List<Point> morePoints = new ArrayList<>();
        while (true) {
            morePoints.add(importPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new PolyBezierData(p2, morePoints);
    }

    public static GraphicPolyBezier importPolyBezier(Scanner sc) {
        String hexColor = sc.next();
        int thickness = sc.nextInt();
        Point p1 = importPoint(sc);
        List<PolyBezierData> data = new ArrayList<>();
        while (true) {
            data.add(importPolyBezierData(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new GraphicPolyBezier(hexColor, thickness, p1, data);
    }

    public static GraphicCircle importCircle(Scanner sc) {
        String hexColor = sc.next();
        int thickness = sc.nextInt();
        Point center = importPoint(sc);
        int radius = sc.nextInt();
        return new GraphicCircle(hexColor, thickness, center, radius);
    }

    public static Point importPoint(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    public static GraphicFloodFill importFloodFill(Scanner sc) {
        String hexColor = sc.next();
        Point point = importPoint(sc);
        return new GraphicFloodFill(hexColor, point);
    }

    public static GraphicImage importImage(Scanner sc) {
        sc.skip(" ");
        String filePath = sc.nextLine();
        Point origin = importPoint(sc);
        Dimension size = importDimension(sc);
        double opacity = sc.nextDouble();
        return new GraphicImage(filePath, origin, size, opacity);
    }

    public static Dimension importDimension(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Dimension(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
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
        BufferedImage debugBuffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = debugBuffer.createGraphics();

        for (GraphicLayer layer : instructions) {
            if (layer.shown) {
                gOuter.drawImage(layer.draw(), 0, 0, null);
            }
            if (!GlobalState.pannerPanelDragging || GlobalState.pannerShowDebugging) {
                layer.debugDraw(g);
            }
        }

        gOuter.drawImage(debugBuffer, 0, 0, null);
    }

}

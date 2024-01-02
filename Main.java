import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Main {
    public static void main(String[] args) {

        MutableInt currentLayer = new MutableInt(0);

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("base sketch", new ArrayList<>())
                .add(new GraphicImage("baseSketch.jpg", new Point(0, 0), new Dimension(600, 600), 1.0)));
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
        frame2.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        frame2.setTitle("editor");
        frame2.setSize(600, 600);

        JPanel panel2 = new JPanel();
        frame2.setContentPane(panel2);

        GroupLayout layout = new GroupLayout(panel2);
        panel2.setLayout(layout);

        JScrollPane layerScrollPane = new JScrollPane();
        layerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        layerScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        layerScrollPane.setViewportView(createLayerListPanel(instructions, frame2, currentLayer));

        JScrollPane editorScrollPane = new JScrollPane();

        JPanel editorPane = EditingPanelFactory.create(instructions.get(currentLayer.value));
        editorScrollPane.setViewportView(editorPane);
        editorScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        editorScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        JButton addLayerButton = new JButton("add layer");
        addLayerButton.addActionListener(e -> {
            instructions.add(new GraphicLayer("new layer", new ArrayList<>()));
            layerScrollPane.setViewportView(createLayerListPanel(instructions, frame2, currentLayer));
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(layerScrollPane)
                        .addComponent(addLayerButton))
                .addComponent(editorScrollPane));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(layerScrollPane)
                        .addComponent(addLayerButton))
                .addComponent(editorScrollPane));

        JMenuBar menuBar = new JMenuBar();
        frame2.setJMenuBar(menuBar);

        var fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        var saveMenuItem = fileMenu.add("Save");
        var saveAsMenuItem = fileMenu.add("Save as...");
        var loadMenuItem = fileMenu.add("Load");

        frame2.setVisible(true);

        new Timer(1000 / 60, e -> {
            panel.repaint();
        }).start();

    }

    private static JPanel createLayerListPanel(List<GraphicLayer> instructions, JFrame frame2,
            MutableInt currentLayer) {
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

        int layerI = 0;
        for (GraphicLayer layer : instructions) {
            var layerVisibleCheckbox = new JCheckBox();
            layerVisibleCheckbox.setSelected(layer.shown);
            layerVisibleCheckbox.addActionListener(e -> {
                layer.shown = layerVisibleCheckbox.isSelected();
            });

            var layerEditRadio = new JRadioButton(layer.name);
            if (layerI == currentLayer.value) {
                layerEditRadio.setSelected(true);
            }
            final int layerI2 = layerI;
            layerEditRadio.addActionListener(e -> {
                JScrollPane editorScrollPane = (JScrollPane) frame2.getContentPane().getComponent(2);
                JPanel editorPane = EditingPanelFactory.create(layer);
                editorScrollPane.setViewportView(editorPane);
                currentLayer.value = layerI2;
            });
            layerButtonGroup.add(layerEditRadio);

            layerVGroup.addGroup(
                    layerLayout.createParallelGroup(Alignment.CENTER)
                            .addComponent(layerVisibleCheckbox)
                            .addComponent(layerEditRadio));
            layerVGroup.addPreferredGap(ComponentPlacement.RELATED);
            layerCheckboxHGroup.addComponent(layerVisibleCheckbox);
            layerRadioHGroup.addComponent(layerEditRadio);
            layerI++;
        }
        return layerPane;
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
    abstract public void draw(BufferedImage buffer);
}

abstract class GraphicPlotter extends GraphicObject {
    public Color color = Color.black;

    protected void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

}

class GraphicLine extends GraphicPlotter {
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

class GraphicPolygon extends GraphicPlotter {
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

class GraphicBezierCurve extends GraphicPlotter {
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

class GraphicFloodFill extends GraphicPlotter {
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
}

class PannerPanelXListener implements MouseMotionListener, MouseListener {
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
        spinner.setValue(originX + (e.getX() - startX));
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
        spinner.setValue(originY + (e.getY() - startY));
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

class PannerPanelWListener implements MouseMotionListener, MouseListener {
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
        spinner.setValue(originX + (e.getX() - startX));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        originX = dim.width;
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
        spinner.setValue(originY + (e.getY() - startY));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startY = e.getY();
        originY = dim.height;
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

class EditingPanelFactory {
    private EditingPanelFactory() {
    }

    public static JPanel create(String labelText, Point point) {
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
        });
        JSpinner ySpinner = new JSpinner(yModel);
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

    public static JPanel create(String labelText, Dimension dim) {
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
        });

        JSpinner hSpinner = new JSpinner(hModel);
        hSpinner.addChangeListener(e -> {
            dim.height = (int) hSpinner.getValue();
        });

        JPanel pannerPanel = new JPanel();
        pannerPanel.setPreferredSize(new Dimension(20, 20));
        pannerPanel.setMaximumSize(new Dimension(20, 20));
        pannerPanel.setMinimumSize(new Dimension(20, 20));
        pannerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));

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

    public static JPanel create(String labelText, MutableString str) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JTextField textField = new JTextField(str.value);
        textField.addActionListener(e -> {
            str.value = textField.getText();
        });

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

    public static JPanel create(String labelText, MutableDouble doub, double min, double max, double stepSize) {
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
        });
        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
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

        var label = new JLabel(layer.name);
        vGroup.addComponent(label);
        hGroup.addComponent(label);
        for (var gObj : layer.objects) {
            var objPanel = create(gObj);
            vGroup.addPreferredGap(ComponentPlacement.RELATED);
            vGroup.addComponent(objPanel);
            hGroup.addComponent(objPanel);
        }

        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("GraphicLine");
        comboBox.addItem("GraphicPolygon");
        comboBox.addItem("GraphicBezierCurve");
        comboBox.addItem("GraphicFloodFill");
        comboBox.addItem("GraphicImage");

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            switch ((String) comboBox.getSelectedItem()) {
                case "GraphicLine":
                    layer.add(new GraphicLine("#000000", new Point(0, 0), new Point(50, 50)));
                    break;
                case "GraphicPolygon":
                    layer.add(new GraphicPolygon("#000000", List.of(new Point(0, 0), new Point(50, 50))));
                    break;
                case "GraphicBezierCurve":
                    layer.add(new GraphicBezierCurve("#000000", new Point(0, 0), new Point(50, 50), new Point(50, 50),
                            new Point(50, 50)));
                    break;
                case "GraphicFloodFill":
                    layer.add(new GraphicFloodFill("#000000", new Point(0, 0)));
                    break;
                case "GraphicImage":
                    layer.add(new GraphicImage("image.png", new Point(0, 0), new Dimension(50, 50), 1.0));
                    break;
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
        } else if (object instanceof GraphicPolygon) {
            return create((GraphicPolygon) object);
        } else if (object instanceof GraphicBezierCurve) {
            return create((GraphicBezierCurve) object);
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
        var p1Panel = create("p1", line.p1);
        var p2Panel = create("p2", line.p2);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(p1Panel)
                .addComponent(p2Panel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(p1Panel)
                .addGap(2)
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
                .addGap(2)
                .addComponent(p2Panel)
                .addGap(2)
                .addComponent(p3Panel)
                .addGap(2)
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

    public static JPanel create(GraphicImage image) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicImage");

        var filePathPanel = create("file path", image.filePath);

        var originPanel = create("origin", image.origin);
        var sizePanel = create("size", image.size);

        var opacityPanel = create("opacity", image.opacity, 0.0, 1.0, 0.01);

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
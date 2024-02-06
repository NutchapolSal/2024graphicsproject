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
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {

    private static void runTests() {
        AnimTest.test();
        System.out.println();
    }

    public static void main(String[] args) {
        runTests();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("bg",
                new AnimBoolean().add(0, true, EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimDouble().add(0, 0.0, EasingFunction.snap))
                .add(new GraphicPath2D(
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimColor().add(0, "#FFFFFF", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#113", EasingFunction.easeInOutPower2)
                                .add(10, "#374A71", EasingFunction.easeInOutPower2),
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-325, -325), EasingFunction.snap),
                        new Path2DLine(new AnimPoint().add(0, new Point(325, -325), EasingFunction.snap)),
                        new Path2DLine(new AnimPoint().add(0, new Point(325, 325), EasingFunction.snap)),
                        new Path2DLine(new AnimPoint().add(0, new Point(-325, 325), EasingFunction.snap))))

        );
        instructions.add(new GraphicLayer("third point six swing",
                new AnimBoolean().add(0, true, EasingFunction.snap)
                        .add(6, false, EasingFunction.snap)
                        .add(10, true, EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.easeInOutPower2)
                        .add(2, new Point(50, 50), EasingFunction.easeInOutPower2),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimDouble().add(3, 0.0, EasingFunction.easeInOutPower3).add(5, 360.0, EasingFunction.linear))
                .add(new GraphicPath2D(
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#000000", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#333333", EasingFunction.snap),
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-200, -200), EasingFunction.snap),
                        new Path2DBezier(
                                new AnimPoint().add(0, new Point(-250, -170), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-150, -170), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-210, -140), EasingFunction.easeInOutPower2)),
                        new Path2DBezier(
                                new AnimPoint().add(0, new Point(-200, 0), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-150, -250), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(0, -150), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(150, -250), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(200, -150), EasingFunction.easeInOutPower2))))
                .add(new GraphicImage(
                        "null",
                        new AnimPoint().add(0, new Point(-290, -290), EasingFunction.snap),
                        new AnimDimension().add(0, new Dimension(20, 20), EasingFunction.snap),
                        new AnimDouble().add(0, 1.0, EasingFunction.snap)))
                .add(new GraphicCircle(
                        new AnimColor().add(0, "#2266AA", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-220, -260), EasingFunction.snap),
                        new AnimInt().add(0, 20, EasingFunction.snap)))

        );

        var exx = ImEx.exportString(instructions);
        System.out.println(exx);

        System.out.println(ImEx.exportString(ImEx.importString(exx)));
        System.out.println(ImEx.exportCode(instructions));

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("😋");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new EditorFrame(frame, instructions);

        long startTime = System.nanoTime();
        new Timer(1000 / 60, e -> {
            long currTime = System.nanoTime();
            panel.time = (currTime - startTime) / 1_000_000_000.0;
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

        frame2.setLocation(frame.getLocation().x + frame.getWidth(),
                frame.getLocation().y);
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
            this.instructions.add(new GraphicLayer());
            updateLayerListPanel();
        });
        JButton layerUpButton = new JButton("^");
        layerUpButton.addActionListener(e -> {
            if (currentLayer.value > 0) {
                var temp = instructions.get(currentLayer.value - 1);
                instructions.set(currentLayer.value - 1,
                        instructions.get(currentLayer.value));
                instructions.set(currentLayer.value, temp);
                currentLayer.value--;
                updateLayerListPanel();
            }
        });

        JButton layerDownButton = new JButton("v");
        layerDownButton.addActionListener(e -> {
            if (currentLayer.value < instructions.size() - 1) {
                var temp = instructions.get(currentLayer.value + 1);
                instructions.set(currentLayer.value + 1,
                        instructions.get(currentLayer.value));
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
        fileMenu.addSeparator();
        var exportCodeMenuItem = fileMenu.add("Export code...");

        JFileChooser fileChooser = new JFileChooser(prefs.get("lastSavePath",
                System.getProperty("user.home")));

        saveMenuItem.addActionListener(e -> {
            if (this.savePath.value == null) {
                if (fileChooser.showSaveDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                    this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                    prefs.put("lastSavePath",
                            fileChooser.getCurrentDirectory().getAbsolutePath());
                }
            }
            if (this.savePath.value != null) {
                try {
                    FileWriter fw = new FileWriter(this.savePath.value);
                    fw.write(ImEx.exportString(instructions));
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
                prefs.put("lastSavePath",
                        fileChooser.getCurrentDirectory().getAbsolutePath());

                try {
                    FileWriter fw = new FileWriter(this.savePath.value);
                    fw.write(ImEx.exportString(instructions));
                    fw.close();
                    frame2.setTitle("editor - " + new File(this.savePath.value).getName() + " @ "
                            + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        loadMenuItem.addActionListener(e -> {
            if (fileChooser.showOpenDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                prefs.put("lastSavePath",
                        fileChooser.getCurrentDirectory().getAbsolutePath());

                try {
                    var file = new File(this.savePath.value);
                    Scanner scanner = new Scanner(file);
                    instructions.clear();
                    var newInstructions = ImEx.importLayers(scanner);
                    instructions.addAll(newInstructions);
                    scanner.close();
                    frame2.setTitle("editor - " + file.getName());
                    updateLayerListPanel();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        exportCodeMenuItem.addActionListener(e -> {
            if (fileChooser.showSaveDialog(frame2) == JFileChooser.APPROVE_OPTION) {
                prefs.put("lastSavePath",
                        fileChooser.getCurrentDirectory().getAbsolutePath());

                try {
                    FileWriter fw = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());
                    fw.write(ImEx.exportCode(instructions));
                    fw.close();
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
        var layerRadioHGroup = layerLayout.createParallelGroup();
        layerLayout.setHorizontalGroup(layerLayout.createSequentialGroup()
                .addGroup(layerRadioHGroup));
        layerLayout.setVerticalGroup(layerVGroup);

        ButtonGroup layerButtonGroup = new ButtonGroup();

        changeEditorPane(Math.max(0, Math.min(this.currentLayer.value,
                instructions.size() - 1)));

        int layerI = 0;
        for (GraphicLayer layer : instructions) {

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
                            .addComponent(layerEditRadio));
            layerVGroup.addPreferredGap(ComponentPlacement.RELATED);
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

enum EasingFunction implements DoubleUnaryOperator {

    linear(x -> x),
    easeOutSine(x -> Math.sin((x * Math.PI) / 2.0)),
    easeInSine(x -> 1 - Math.cos((x * Math.PI) / 2.0)),
    easeInOutSine(x -> -(Math.cos(Math.PI * x) - 1) / 2),
    /** quad */
    easeInPower2(constructEaseInPower(2)),
    /** quad */
    easeOutPower2(constructEaseOutPower(2)),
    /** quad */
    easeInOutPower2(constructEaseInOutPower(2)),
    /** cubic */
    easeInPower3(constructEaseInPower(3)),
    /** cubic */
    easeOutPower3(constructEaseOutPower(3)),
    /** cubic */
    easeInOutPower3(constructEaseInOutPower(3)),
    /** quart */
    easeInPower4(constructEaseInPower(4)),
    /** quart */
    easeOutPower4(constructEaseOutPower(4)),
    /** quart */
    easeInOutPower4(constructEaseInOutPower(4)),
    /** quint */
    easeInPower5(constructEaseInPower(5)),
    /** quint */
    easeOutPower5(constructEaseOutPower(5)),
    /** quint */
    easeInOutPower5(constructEaseInOutPower(5)),
    easeInExpo(x -> x <= 0 ? 0 : Math.pow(2, 10 * x - 10)),
    easeOutExpo(x -> 1.0 <= x ? 1.0 : 1 - Math.pow(2, -10 * x)),
    easeInOutExpo(
            x -> {
                if (x <= 0) {
                    return 0;
                }
                if (1 <= x) {
                    return 1;
                }
                return x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                        : (2 - Math.pow(2, -20 * x + 10)) / 2;
            }),

    snap(x -> x < 1 ? 0 : 1);

    private static DoubleUnaryOperator constructEaseInPower(double power) {
        return x -> Math.pow(x, power);
    }

    private static DoubleUnaryOperator constructEaseOutPower(double power) {
        return x -> 1 - Math.pow((1 - x), power);
    }

    private static DoubleUnaryOperator constructEaseInOutPower(double power) {
        return x -> x < 0.5 ? Math.pow(2, power - 1) * Math.pow(x, power) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

    private final DoubleUnaryOperator easing;

    EasingFunction(DoubleUnaryOperator easing) {
        this.easing = easing;
    }

    public double applyAsDouble(double x) {
        return easing.applyAsDouble(x);
    }

}

abstract class AnimatedValue implements Exportable {
    static class Timepoint {
        public double time;
        public EasingFunction easingToNext;

        Timepoint(double time, EasingFunction easingToNext) {
            this.time = time;
            this.easingToNext = easingToNext;
        }

    }

    static class StepValue {
        public int index;
        public double frac;

        StepValue(int index, double frac) {
            this.index = index;
            this.frac = frac;
        }

        @Override
        public String toString() {
            return "StepValue [index=" + index + ", frac=" + frac + "]";
        }
    }

    public List<Timepoint> timepoints = new ArrayList<>();

    /**
     * @return index of added timepoint or -1 if timepoint already exists
     */
    protected int addTimepoint(double time, EasingFunction easingToNext) {
        int index = Collections.binarySearch(timepoints, new Timepoint(time, null),
                (a, b) -> Double.compare(a.time, b.time));
        if (index < 0) {
            index = ~index;
        } else {
            return -1;
        }

        timepoints.add(index, new Timepoint(time, easingToNext));
        return index;
    }

    protected StepValue getValue(double time) {
        int iBeforeTime = -1; // index of timepoint before or equal time
        for (Timepoint timepoint : timepoints) {
            if (timepoint.time <= time) {
                iBeforeTime++;
            } else {
                break;
            }
        }

        if (iBeforeTime == -1) {
            return new StepValue(0, 0);
        }
        if (iBeforeTime == timepoints.size() - 1) {
            return new StepValue(iBeforeTime, 0);
        }

        double timeBefore = timepoints.get(iBeforeTime).time;
        double timeAfter = timepoints.get(iBeforeTime + 1).time;
        double frac = (time - timeBefore) / (timeAfter - timeBefore);
        return new StepValue(iBeforeTime, timepoints.get(iBeforeTime).easingToNext.applyAsDouble(frac));
    }

    protected String exportString(List<String> valuepoints) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timepoints.size(); i++) {
            sb.append(timepoints.get(i).time);
            sb.append(" ");
            sb.append(valuepoints.get(i));
            sb.append(" ");
            sb.append(timepoints.get(i).easingToNext);
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    protected String exportCode(String className, List<String> valuepoints) {
        StringBuilder sb = new StringBuilder("new ");
        sb.append(className);
        sb.append("()");
        for (int i = 0; i < timepoints.size(); i++) {
            sb.append("\n.add(");
            sb.append(timepoints.get(i).time);
            sb.append(", ");
            sb.append(valuepoints.get(i));
            sb.append(", EasingFunction.");
            sb.append(timepoints.get(i).easingToNext);
            sb.append(")");
        }
        return sb.toString();
    }
}

class AnimTest extends AnimatedValue {
    public static void test() {
        System.out.println("AnimatedValue test");
        var animvalue = new AnimTest();
        System.out.println(animvalue.getValue(0.5)); // 0, 0.0
        System.out.println("---");
        animvalue.addTimepoint(0, EasingFunction.linear);
        System.out.println(animvalue.getValue(-1)); // 0, 0.0
        System.out.println(animvalue.getValue(0)); // 0, 0.0
        System.out.println(animvalue.getValue(1)); // 0, 0.0
        System.out.println("---");
        animvalue.addTimepoint(1, EasingFunction.linear);
        System.out.println(animvalue.getValue(0)); // 0, 0.0
        System.out.println(animvalue.getValue(0.33)); // 0, ~0.33
        System.out.println(animvalue.getValue(0.66)); // 0, ~0.66
        System.out.println(animvalue.getValue(1)); // 1, 0.0
        System.out.println(animvalue.getValue(1.33)); // 1, 0.0
        System.out.println("---");
        animvalue.addTimepoint(3, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 1, 0.0
        System.out.println(animvalue.getValue(1.66)); // 1, ~0.33
        System.out.println(animvalue.getValue(2.33)); // 1, ~0.66
        System.out.println(animvalue.getValue(3)); // 2, 0.0
        System.out.println(animvalue.getValue(4)); // 2, 0.0
        System.out.println("---");
        animvalue = new AnimTest();
        animvalue.addTimepoint(0, EasingFunction.linear);
        animvalue.addTimepoint(3, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 0, ~0.33
        animvalue.addTimepoint(1, EasingFunction.linear);
        animvalue.addTimepoint(1, EasingFunction.linear);
        animvalue.addTimepoint(1, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 1, 0.0
    }

    public String exportString() {
        return "";
    }

    public String exportCode() {
        return "";
    }
}

class AnimBoolean extends AnimatedValue {
    public List<Boolean> valuepoints = new ArrayList<>();

    public AnimBoolean add(double time, boolean value, DoubleUnaryOperator easingToNext) {
        var i = super.addTimepoint(time, EasingFunction.snap);
        if (i == -1) {
            return this;
        }
        valuepoints.add(i, value);
        return this;
    }

    public AnimBoolean remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public boolean get(double time) {
        if (timepoints.isEmpty()) {
            return false;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimBoolean copy() {
        var anim = new AnimBoolean();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = valuepoints.get(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimBoolean", strings);
    }
}

class AnimDouble extends AnimatedValue {
    public List<Double> valuepoints = new ArrayList<>();

    public AnimDouble add(double time, double value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimDouble remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public double get(double time) {
        if (timepoints.isEmpty()) {
            return 0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimDouble copy() {
        var anim = new AnimDouble();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = valuepoints.get(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimDouble", strings);
    }
}

class AnimInt extends AnimatedValue {
    public List<Integer> valuepoints = new ArrayList<>();

    public AnimInt add(double time, int value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimInt remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public int get(double time) {
        if (timepoints.isEmpty()) {
            return 0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimInt copy() {
        var anim = new AnimInt();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = valuepoints.get(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimInt", strings);
    }
}

class AnimColor extends AnimatedValue {
    public List<Color> valuepoints = new ArrayList<>();

    public AnimColor add(double time, String value, EasingFunction easingToNext) {
        return add(time, ColorHexer.decode(value), easingToNext);
    }

    public AnimColor add(double time, Color value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimColor remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Color get(double time) {
        if (timepoints.isEmpty()) {
            return Color.black;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimColor copy() {
        var anim = new AnimColor();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = valuepoints.get(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimColor", strings);
    }
}

class AnimPoint extends AnimatedValue {
    public List<Point> valuepoints = new ArrayList<>();

    public AnimPoint add(double time, Point value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimPoint remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Point get(double time) {
        if (timepoints.isEmpty()) {
            return new Point(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return new Point(
                Lerp.run(valuepoints.get(stepValue.index).x, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).x),
                Lerp.run(valuepoints.get(stepValue.index).y, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).y));
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimPoint", strings);
    }
}

class AnimDimension extends AnimatedValue {
    public List<Dimension> valuepoints = new ArrayList<>();

    public AnimDimension add(double time, Dimension value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimDimension remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Dimension get(double time) {
        if (timepoints.isEmpty()) {
            return new Dimension(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return new Dimension(
                (int) Lerp.run(valuepoints.get(stepValue.index).width, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).width),
                (int) Lerp.run(valuepoints.get(stepValue.index).height, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).height));
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimDimension", strings);
    }
}

class Lerp {
    public static boolean run(boolean before, double frac, boolean after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return before;
    }

    public static double run(double before, double frac, double after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return before + (after - before) * frac;
    }

    public static int run(int before, double frac, int after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return (int) (before + (after - before) * frac);
    }

    public static float run(float before, double frac, float after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return (float) (before + (after - before) * frac);
    }

    // BRACE YOURSELF FOR COLOR PROCESSING

    // https://bottosson.github.io/posts/colorwrong/#what-can-we-do%3F
    // https://bottosson.github.io/posts/oklab/
    private static class LinearSRGB {
        public float r;
        public float g;
        public float b;

        public LinearSRGB(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        private static float linearToSRGB(float c) {
            if (c >= 0.0031308) {
                return (1.055f) * (float) Math.pow(c, 1.0f / 2.4f) - 0.055f;
            } else {
                return 12.92f * c;
            }

        }

        private static float sRGBToLinear(float c) {
            if (c >= 0.04045) {
                return (float) Math.pow(((c + 0.055) / (1 + 0.055)), 2.4);
            } else {
                return c / 12.92f;
            }
        }

        private static float clamp(float c) {
            return Math.min(Math.max(0, c), 1);
        }

        public LinearSRGB(Color c) {
            this.r = sRGBToLinear(c.getRed() / 255.0f);
            this.g = sRGBToLinear(c.getGreen() / 255.0f);
            this.b = sRGBToLinear(c.getBlue() / 255.0f);
        }

        public LinearSRGB(OkLab c) {
            float l_ = c.L + 0.3963377774f * c.a + 0.2158037573f * c.b;
            float m_ = c.L - 0.1055613458f * c.a - 0.0638541728f * c.b;
            float s_ = c.L - 0.0894841775f * c.a - 1.2914855480f * c.b;

            float l = l_ * l_ * l_;
            float m = m_ * m_ * m_;
            float s = s_ * s_ * s_;

            this.r = +4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s;
            this.g = -1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s;
            this.b = -0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s;
        }

        public Color toColor() {
            return new Color(
                    clamp(linearToSRGB(r)),
                    clamp(linearToSRGB(g)),
                    clamp(linearToSRGB(b)));
        }
    }

    private static class OkLab {
        public float L;
        public float a;
        public float b;

        public OkLab(float L, float a, float b) {
            this.L = L;
            this.a = a;
            this.b = b;
        }

        public OkLab(LinearSRGB c) {
            float l = 0.4122214708f * c.r + 0.5363325363f * c.g + 0.0514459929f * c.b;
            float m = 0.2119034982f * c.r + 0.6806995451f * c.g + 0.1073969566f * c.b;
            float s = 0.0883024619f * c.r + 0.2817188376f * c.g + 0.6299787005f * c.b;

            float l_ = (float) Math.cbrt(l);
            float m_ = (float) Math.cbrt(m);
            float s_ = (float) Math.cbrt(s);

            this.L = 0.2104542553f * l_ + 0.7936177850f * m_ - 0.0040720468f * s_;
            this.a = 1.9779984951f * l_ - 2.4285922050f * m_ + 0.4505937099f * s_;
            this.b = 0.0259040371f * l_ + 0.7827717662f * m_ - 0.8086757660f * s_;
        }

        public OkLab multiplyAlpha(float alpha) {
            this.L *= alpha;
            this.a *= alpha;
            this.b *= alpha;
            return this;
        }

        public OkLab divideAlpha(float alpha) {
            if (alpha == 0) {
                this.L = 0;
                this.a = 0;
                this.b = 0;
                return this;
            }
            this.L /= alpha;
            this.a /= alpha;
            this.b /= alpha;
            return this;
        }
    }

    /**
     * lerps in OkLab color space + correct handling of alpha
     */
    public static Color run(Color before, double frac, Color after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        var beforeOk = new OkLab(new LinearSRGB(before)).multiplyAlpha(before.getAlpha() / 255.0f);
        var afterOk = new OkLab(new LinearSRGB(after)).multiplyAlpha(after.getAlpha() / 255.0f);
        var lerpAlpha = run(before.getAlpha(), frac, after.getAlpha());
        var lerpOk = new OkLab(
                Lerp.run(beforeOk.L, frac, afterOk.L),
                Lerp.run(beforeOk.a, frac, afterOk.a),
                Lerp.run(beforeOk.b, frac, afterOk.b))
                .divideAlpha(lerpAlpha / 255.0f);
        var lerp = new LinearSRGB(lerpOk).toColor();

        return new Color(lerp.getRed(), lerp.getGreen(), lerp.getBlue(),
                lerpAlpha);

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

interface Debuggable {
    public void setDebugging(int index);

    public void unsetDebugging();

    public void debugDraw(Graphics g, double time);

    default void debugCircle(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(x - 5, y - 5, 11, 11);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 5, y - 5, 11, 11);
    }

    default void debugDot(Graphics g, int x, int y, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.fillRect(x - 2, y - 2, 5, 5);
        g2.setColor(active ? Color.green : Color.red);
        g2.fillRect(x - 1, y - 1, 3, 3);
    }

    default void debugLine(Graphics g, int x1, int y1, int x2, int y2, boolean active) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x1, y1, x2, y2);
        g2.setColor(active ? Color.green : Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x1, y1, x2, y2);
    }
}

class GraphicLayer implements Exportable, Debuggable {
    public MutableString name;
    public AnimBoolean shown;
    public AnimPoint translate;
    public AnimPoint rotateOrigin;
    public AnimDouble rotate;
    public List<GraphicObject> objects;
    private int debugging = -1;

    GraphicLayer() {
        this("new layer", new AnimBoolean(), new AnimPoint(), new AnimPoint(), new AnimDouble(),
                new ArrayList<>());
    }

    GraphicLayer(String name, AnimBoolean shown, AnimPoint translate, AnimPoint rotateOrigin, AnimDouble rotate) {
        this(name, shown, translate, rotateOrigin, rotate, new ArrayList<>());
    }

    GraphicLayer(String name, AnimBoolean shown, AnimPoint translate, AnimPoint rotateOrigin, AnimDouble rotate,
            List<GraphicObject> objects) {
        this.name = new MutableString(name);
        this.shown = shown;
        this.translate = translate;
        this.rotate = rotate;
        this.rotateOrigin = rotateOrigin;
        this.objects = objects;
    }

    Graphics2D transform(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        g.translate(translate.get(time).x, translate.get(time).y);
        g.rotate(rotate.get(time) * Math.PI / 180.0, rotateOrigin.get(time).x, rotateOrigin.get(time).y);
        return g;
    }

    void draw(Graphics gOuter, double time) {
        Graphics2D g = transform(gOuter, time);
        for (GraphicObject object : objects) {
            object.draw(g, time);
        }
    }

    @Override
    public void debugDraw(Graphics gOuter, double time) {
        Graphics2D g = transform(gOuter, time);
        if (this.debugging != -1) {
            debugDrawSelf(g, time);
        }

        for (GraphicObject object : objects) {
            if (object.debugging != -1) {
                object.debugDraw(g, time);
            }
        }
    }

    private void debugDrawSelf(Graphics2D g, double time) {
        var translate = this.translate.get(time);
        var rotateOrigin = this.rotateOrigin.get(time);
        var rotate = this.rotate.get(time);
        debugCircle(g, translate.x, translate.y, debugging == 1);
        debugDot(g, rotateOrigin.x, rotateOrigin.y, debugging == 2);
        debugLine(g, rotateOrigin.x, rotateOrigin.y,
                (int) (Math.sin(rotate * Math.PI / 180) * 20 + rotateOrigin.x),
                (int) (Math.cos(rotate * Math.PI / 180) * 20 + rotateOrigin.y),
                debugging == 3);

    }

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

    GraphicLayer remove(GraphicObject object) {
        objects.remove(object);
        return this;
    }

    @Override
    public void setDebugging(int index) {
        this.debugging = index;
    }

    @Override
    public void unsetDebugging() {
        this.debugging = -1;
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LAYER ");
        sb.append(this.name);
        sb.append("\n");
        sb.append(ImEx.exportString(this.shown));
        sb.append("\n");
        sb.append(ImEx.exportString(this.translate));
        sb.append("\n");
        sb.append(ImEx.exportString(this.rotateOrigin));
        sb.append("\n");
        sb.append(ImEx.exportString(this.rotate));
        sb.append("\n");
        for (GraphicObject object : this.objects) {
            sb.append(object.exportString());
            sb.append("\n");
        }
        sb.append("END");
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicLayer(\"");
        sb.append(this.name);
        sb.append("\", ");
        sb.append(ImEx.exportCode(this.shown));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.translate));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.rotateOrigin));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.rotate));
        sb.append(")\n");
        for (GraphicObject object : this.objects) {
            sb.append(".add(");
            sb.append(object.exportCode());
            sb.append(")\n");
        }
        return sb.toString();
    }

}

abstract class GraphicObject implements Exportable, Debuggable {
    abstract public void draw(Graphics g, double time);

    protected int debugging = -1;

    abstract public GraphicObject copy();

    @Override
    public void setDebugging(int index) {
        this.debugging = index;
    }

    @Override
    public void unsetDebugging() {
        this.debugging = -1;
    }

    // abstract public GraphicObject editorCopy();

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
    public AnimColor color;

    protected GraphicPlotter(AnimColor color) {
        this.color = color;
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }

    protected String exportParamString() {
        return ImEx.exportString(color);
    }

    protected String exportParamCode() {
        return ImEx.exportCode(color);
    }
}

abstract class GraphicLinePlotter extends GraphicPlotter {
    public AnimInt thickness;

    protected GraphicLinePlotter(AnimColor color, AnimInt thickness) {
        super(color);
        this.thickness = thickness;
    }

    protected void plotLine(Graphics g, double time, Point p1, Point p2) {
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
            plot(g, x, y, thickness.get(time));
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

            double x = Math.pow(1 - t, 3) * pA.x +
                    3 * t * Math.pow(1 - t, 2) * pB.x +
                    3 * Math.pow(t, 2) * (1 - t) * pC.x
                    + Math.pow(t, 3) * pD.x;

            double y = Math.pow(1 - t, 3) * pA.y +
                    3 * t * Math.pow(1 - t, 2) * pB.y +
                    3 * Math.pow(t, 2) * (1 - t) * pC.y
                    + Math.pow(t, 3) * pD.y;

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

abstract class GraphicDrawFiller extends GraphicObject {
    public AnimBoolean stroke;
    public AnimColor strokeColor;
    public AnimInt thickness;
    public AnimBoolean fill;
    public AnimColor fillColor;

    protected GraphicDrawFiller(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill,
            AnimColor fillColor) {
        this.stroke = stroke;
        this.strokeColor = strokeColor;
        this.thickness = thickness;
        this.fill = fill;
        this.fillColor = fillColor;
    }

    protected boolean setupStroke(Graphics2D g, double time) {
        if (!stroke.get(time)) {
            return false;
        }
        g.setColor(strokeColor.get(time));
        g.setStroke(new BasicStroke(thickness.get(time)));
        return true;
    }

    protected boolean setupFill(Graphics2D g, double time) {
        if (!fill.get(time)) {
            return false;
        }
        g.setColor(fillColor.get(time));
        return true;
    }

    protected String exportParamString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.exportString(stroke));
        sb.append(" ");
        sb.append(ImEx.exportString(strokeColor));
        sb.append(" ");
        sb.append(ImEx.exportString(thickness));
        sb.append(" ");
        sb.append(ImEx.exportString(fill));
        sb.append(" ");
        sb.append(ImEx.exportString(fillColor));
        return sb.toString();
    }

    protected String exportParamCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.exportCode(stroke));
        sb.append(", ");
        sb.append(ImEx.exportCode(strokeColor));
        sb.append(", ");
        sb.append(ImEx.exportCode(thickness));
        sb.append(", ");
        sb.append(ImEx.exportCode(fill));
        sb.append(", ");
        sb.append(ImEx.exportCode(fillColor));
        return sb.toString();
    }
}

abstract class Path2DData implements Exportable {
    abstract public void run(Path2D path, double time);

    abstract public int size();

    abstract AnimPoint lastPoint();
}

class Path2DLine extends Path2DData {
    public AnimPoint pNext;

    Path2DLine(AnimPoint pNext) {
        this.pNext = pNext;
    }

    @Override
    public void run(Path2D path, double time) {
        path.lineTo(pNext.get(time).x, pNext.get(time).y);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    AnimPoint lastPoint() {
        return pNext;
    }

    @Override
    public String exportCode() {
        return "new Path2DLine(" + ImEx.exportCode(this.pNext) + ")";
    }

    @Override
    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LINE ");
        sb.append(ImEx.exportString(this.pNext));
        return sb.toString();
    }

}

class Path2DBezier extends Path2DData {
    public AnimPoint pNext;
    public List<AnimPoint> morePoints;

    Path2DBezier(AnimPoint pNext, AnimPoint... morePoints) {
        this(pNext, new ArrayList<>(Arrays.asList(morePoints)));
    }

    Path2DBezier(AnimPoint pNext, List<AnimPoint> morePoints) {
        this.pNext = pNext;
        this.morePoints = morePoints;
    }

    @Override
    public void run(Path2D path, double time) {
        path.curveTo(pNext.get(time).x, pNext.get(time).y, morePoints.get(0).get(time).x,
                morePoints.get(0).get(time).y,
                morePoints.get(1).get(time).x,
                morePoints.get(1).get(time).y);
        for (int i = 3; i < morePoints.size(); i += 2) {
            Point pA = morePoints.get(i - 2).get(time);
            Point pBtemp = morePoints.get(i - 3).get(time);
            Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
            Point pC = morePoints.get(i - 1).get(time);
            Point pD = morePoints.get(i).get(time);

            path.curveTo(pB.x, pB.y, pC.x, pC.y, pD.x, pD.y);
        }
    }

    @Override
    public int size() {
        return morePoints.size() + 1;
    }

    @Override
    AnimPoint lastPoint() {
        return morePoints.get(morePoints.size() - 1);
    }

    @Override
    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Path2DBezier(");
        sb.append(ImEx.exportCode(this.pNext));
        for (var point : this.morePoints) {
            sb.append(", ");
            sb.append(ImEx.exportCode(point));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BEZIER ");
        sb.append(ImEx.exportString(this.pNext));
        sb.append(" ");
        for (var point : this.morePoints) {
            sb.append(ImEx.exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

}

class GraphicPath2D extends GraphicDrawFiller {
    public AnimPoint p1;
    public List<Path2DData> data;
    public AnimBoolean closed;

    GraphicPath2D() {
        this(new AnimBoolean(), new AnimColor(), new AnimInt(), new AnimBoolean(), new AnimColor(),
                new AnimBoolean(),
                new AnimPoint(), new ArrayList<>());
    }

    GraphicPath2D(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill,
            AnimColor fillColor,
            AnimBoolean closed,
            AnimPoint p1,
            Path2DData... data) {
        this(stroke, strokeColor, thickness, fill, fillColor, closed, p1, new ArrayList<>(Arrays.asList(data)));
    }

    GraphicPath2D(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill,
            AnimColor fillColor,
            AnimBoolean closed,
            AnimPoint p1,
            List<Path2DData> data) {
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
                debugLine(g, pNextA.x, pNextA.y, bezier.pNext.get(time).x, bezier.pNext.get(time).y,
                        debugging == i);
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

    Point lastPoint() {
        // TODO
        return new Point(0, 0);
    }

    @Override
    public GraphicObject copy() {
        // TODO
        return null;
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

        plotBezier(g, time, roundPoint(center.x, center.y - radius),
                roundPoint(center.x + offset, center.y - radius),
                roundPoint(center.x + radius, center.y - offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y + radius),
                roundPoint(center.x + offset, center.y + radius),
                roundPoint(center.x + radius, center.y + offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y + radius),
                roundPoint(center.x - offset, center.y + radius),
                roundPoint(center.x - radius, center.y + offset),
                roundPoint(center.x - radius, center.y), iters);

        plotBezier(g, time, roundPoint(center.x, center.y - radius),
                roundPoint(center.x - offset, center.y - radius),
                roundPoint(center.x - radius, center.y - offset),
                roundPoint(center.x - radius, center.y), iters);
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

class GraphicImage extends GraphicObject {
    public MutableString filePath;
    private String lastFilePath = "";
    public BufferedImage image;
    public AnimPoint origin;
    public AnimDimension size;
    public AnimDouble opacity;

    GraphicImage(String filePath, AnimPoint origin, AnimDimension size, AnimDouble opacity) {
        this.filePath = new MutableString(filePath);
        this.origin = origin;
        this.size = size;
        this.opacity = opacity;
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
            g.drawString("ERROR", 0, 10);
        }
        lastFilePath = filePath.value;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        var origin = this.origin.get(time);
        var size = this.size.get(time);

        updateImage();

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity.get(time));
        g.setComposite(alphaComposite);
        g.drawImage(image, origin.x, origin.y, size.width, size.height, null);
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        var origin = this.origin.get(time);
        var size = this.size.get(time);
        debugLine(g, origin.x, origin.y, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugDot(g, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugCircle(g, origin.x, origin.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicImage(filePath.value, origin, size,
                opacity);
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IMAGE ");
        sb.append(this.filePath.value);
        sb.append("\n");
        sb.append(ImEx.exportString(this.origin));
        sb.append(" ");
        sb.append(ImEx.exportString(this.size));
        sb.append(" ");
        sb.append(ImEx.exportString(this.opacity));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicImage(\"");
        sb.append(this.filePath.value);
        sb.append("\", ");
        sb.append(ImEx.exportCode(this.origin));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.size));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.opacity));
        sb.append(")");
        return sb.toString();
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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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

    public static int lastSelectedNewObjI = 0;
}

class DebuggingHoverListener implements MouseListener {
    private Debuggable obj;
    private int debugValue;

    DebuggingHoverListener(Debuggable obj, int debugValue) {
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
        obj.setDebugging(debugValue);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (GlobalState.pannerPanelDragging) {
            return;
        }
        obj.unsetDebugging();
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
    private boolean invalid = false;

    public ColorButton() {
        this.setText(" ");
    }

    public void setColor(Color color) {
        this.color = color;
        this.invalid = false;
        this.repaint();
    }

    public void setInvalid() {
        this.invalid = true;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (invalid) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.red);
            g.setFont(getFont().deriveFont(getHeight() * 0.8f));
            g.drawString("!!", (int) (getWidth() * 0.1), (int) (getHeight() * 0.9f));
            return;
        }
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

    public static JPanel create(String labelText, Point point, GraphicObject obj,
            int debugValue) {
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
        pannerPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
        pannerPanel.setFocusable(true);
        addPannerKeybinds(pannerPanel);

        JPanel pannerXPanel = new JPanel();
        pannerXPanel.setPreferredSize(new Dimension(20, 8));
        pannerXPanel.setMaximumSize(new Dimension(20, 8));
        pannerXPanel.setMinimumSize(new Dimension(20, 8));
        pannerXPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerXPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        pannerXPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
        pannerXPanel.setFocusable(true);
        addPannerKeybinds(pannerXPanel);

        JPanel pannerYPanel = new JPanel();
        pannerYPanel.setPreferredSize(new Dimension(8, 20));
        pannerYPanel.setMaximumSize(new Dimension(8, 20));
        pannerYPanel.setMinimumSize(new Dimension(8, 20));
        pannerYPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerYPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        pannerYPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
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
        pannerPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
        addPannerKeybinds(pannerPanel);

        JPanel pannerXPanel = new JPanel();
        pannerXPanel.setPreferredSize(new Dimension(20, 8));
        pannerXPanel.setMaximumSize(new Dimension(20, 8));
        pannerXPanel.setMinimumSize(new Dimension(20, 8));
        pannerXPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerXPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        pannerXPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
        addPannerKeybinds(pannerXPanel);

        JPanel pannerYPanel = new JPanel();
        pannerYPanel.setPreferredSize(new Dimension(8, 20));
        pannerYPanel.setMaximumSize(new Dimension(8, 20));
        pannerYPanel.setMinimumSize(new Dimension(8, 20));
        pannerYPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pannerYPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        pannerYPanel.addMouseListener(new PannerPanelDebuggingHoverListener(obj,
                debugValue));
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

    public static JPanel create(String labelText, MutableString str,
            GraphicObject obj, int debugValue) {
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

    public static JPanel create(String labelText, MutableDouble doub, double min,
            double max, double stepSize,
            Debuggable obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (int) ((max - min) / stepSize);
        JSlider slider = new JSlider(0, sliderSteps, (int) ((doub.value - min) /
                stepSize));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(doub.value, min, max,
                stepSize));
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

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableInt integer, int min,
            int max, int stepSize,
            Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (max - min) / stepSize;
        JSlider slider = new JSlider(0, sliderSteps, (integer.value - min) /
                stepSize);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(integer.value, min,
                Integer.MAX_VALUE, stepSize));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(2);
        spinner.addChangeListener(e -> {
            integer.value = (int) spinner.getValue();
            slider.setValue((integer.value - min) / stepSize);
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

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableColor color, Debuggable obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        ColorButton button = new ColorButton();
        button.setColor(color.value);

        JTextField textField = new JTextField(ColorHexer.encode(color.value));

        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color",
                    color.value);
            if (newColor != null) {
                color.value = newColor;
                button.setColor(color.value);
                textField.setText(ColorHexer.encode(color.value));
            }
        });

        Runnable newColorFunction = () -> {
            var parsedColor = ColorHexer.decodeOptional(textField.getText());
            if (parsedColor.isPresent()) {
                color.value = parsedColor.get();
                button.setColor(parsedColor.get());
            } else {
                button.setInvalid();
            }
        };
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newColorFunction.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newColorFunction.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newColorFunction.run();
            }
        });
        textField.addActionListener(e -> {
            var parsedColor = ColorHexer.decodeOptional(textField.getText());
            if (parsedColor.isPresent()) {
                button.setColor(color.value);
            } else {
                button.setInvalid();
            }
            color.value = parsedColor.orElse(Color.black);
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

    public static JPanel create(String labelText, MutableBoolean bool, Debuggable obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(bool.value);
        checkBox.addActionListener(e -> {
            bool.value = checkBox.isSelected();
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

    public static JPanel create(String labelText, AnimBoolean animBool, Debuggable obj, int debuggingI) { // TODO
        var panel = createPlaceholder(animBool, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel create(String labelText, AnimDouble animDouble, double min, double max, double stepSize,
            Debuggable obj, int debuggingI) { // TODO
        var panel = createPlaceholder(animDouble, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel create(String labelText, AnimInt animInt, int min, int max, int stepSize, Debuggable obj,
            int debuggingI) { // TODO
        var panel = createPlaceholder(animInt, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel create(String labelText, AnimColor animColor, Debuggable obj, int debuggingI) { // TODO
        var panel = createPlaceholder(animColor, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel create(String labelText, AnimPoint animPoint, Debuggable obj, int debuggingI) { // TODO
        var panel = createPlaceholder(animPoint, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel create(String labelText, AnimDimension animDim, Debuggable obj, int debuggingI) { // TODO
        var panel = createPlaceholder(animDim, labelText);
        panel.addMouseListener(new DebuggingHoverListener(obj, debuggingI));
        return panel;
    }

    public static JPanel createPlaceholder(Object object) {
        JPanel jPanel = new JPanel();
        JLabel label = new JLabel("⚠️\ufe0f " + object.getClass().getSimpleName());
        jPanel.add(label);
        return jPanel;
    }

    public static JPanel createPlaceholder(Object object, String message) {
        JPanel jPanel = new JPanel();
        JLabel label = new JLabel("⚠️\ufe0f " + object.getClass().getSimpleName() + ": " + message);
        jPanel.add(label);
        return jPanel;
    }

    public static JPanel create(GraphicObject object) {
        if (object instanceof GraphicPath2D) {
            return create((GraphicPath2D) object);
        } else if (object instanceof GraphicCircle) {
            return create((GraphicCircle) object);
        } else if (object instanceof GraphicImage) {
            return create((GraphicImage) object);
        } else {
            return createPlaceholder(object);
        }
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
        // TODO add the rest of layer variables
        vGroup.addComponent(layerNamePanel);
        hGroup.addComponent(layerNamePanel);
        for (var gObj : layer.objects) {
            var objPanel = create(gObj);
            JPopupMenu popupMenu = new JPopupMenu();
            var deleteItem = popupMenu.add("Delete");
            deleteItem.addActionListener(e -> {
                layer.remove(gObj);
                GlobalState.needsUpdateEditor = true;
            });
            objPanel.setComponentPopupMenu(popupMenu);

            vGroup.addPreferredGap(ComponentPlacement.RELATED);
            vGroup.addComponent(objPanel);
            hGroup.addComponent(objPanel);
        }

        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("GraphicPath2D");
        comboBox.addItem("GraphicCircle");
        comboBox.addItem("GraphicImage");

        comboBox.setSelectedIndex(GlobalState.lastSelectedNewObjI);

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            GlobalState.lastSelectedNewObjI = comboBox.getSelectedIndex();
            Predicate<? super GraphicObject> pred;
            GraphicObject defaultObj = null;
            switch ((String) comboBox.getSelectedItem()) {
                case "GraphicCircle":
                    pred = obj -> obj instanceof GraphicCircle;
                    // defaultObj = new GraphicCircle("#000000", 1, new Point(0, 0), 50);
                    break;
                case "GraphicImage":
                    pred = obj -> obj instanceof GraphicImage;
                    // defaultObj = new GraphicImage("image.png", new Point(0, 0), new Dimension(50,
                    // 50), 1.0);
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
                case "GraphicCircle": {
                    var result = (GraphicCircle) streamResult.get().copy();
                    // result.center.translate(10, 10);
                    layer.add(result);
                    break;
                }
                case "GraphicImage": {
                    var result = (GraphicImage) streamResult.get().copy();
                    // result.origin.translate(10, 10);
                    layer.add(result);
                    break;
                }
            }
        });

        addButton.setEnabled(false); // TODO

        vGroup.addPreferredGap(ComponentPlacement.RELATED);
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER, false)
                .addComponent(comboBox)
                .addComponent(addButton));
        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(comboBox)
                .addComponent(addButton));
        return panel;
    }

    static void p2DAddActionListeners(JMenuItem insertLineItem, JMenuItem insertBezierItem, JMenuItem deleteItem,
            GraphicPath2D p2d, Path2DData data, int dataIndex) {
        // insertLineItem.addActionListener(e -> {
        // Point pRef;
        // if (dataIndex == 0) {
        // pRef = p2d.p1;
        // } else {
        // pRef = p2d.data.get(dataIndex - 1).lastPoint();
        // }
        // var newData = new Path2DLine(new Point(pRef.x + 20, pRef.y + 20));
        // p2d.data.add(dataIndex, newData);
        // GlobalState.needsUpdateEditor = true;
        // });
        // insertBezierItem.addActionListener(e -> {
        // Point pRef;
        // if (dataIndex == 0) {
        // pRef = p2d.p1;
        // } else {
        // pRef = p2d.data.get(dataIndex - 1).lastPoint();
        // }
        // var newData = new Path2DBezier(new Point((pRef.x), (pRef.y + 20)),
        // new Point((pRef.x + 20), (pRef.y)),
        // new Point((pRef.x + 20), (pRef.y + 20)));
        // p2d.data.add(dataIndex, newData);
        // GlobalState.needsUpdateEditor = true;
        // });
        insertLineItem.setEnabled(false);
        insertBezierItem.setEnabled(false); // TODO
        deleteItem.addActionListener(e -> {
            p2d.data.remove(data);
            GlobalState.needsUpdateEditor = true;
        });
    }

    public static JPanel create(Path2DLine data, GraphicPath2D p2d, int dataIndex, int debuggingStartI) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        char pointLetter = (char) (97 + (dataIndex + 16) % 26);

        var pPanel = create(pointLetter + "", data.pNext, p2d, debuggingStartI);

        var popupMenu = new JPopupMenu();
        var insertLineItem = popupMenu.add("Insert Line");
        var insertBezierItem = popupMenu.add("Insert Bezier");
        var deleteItem = popupMenu.add("Delete");
        p2DAddActionListeners(insertLineItem, insertBezierItem, deleteItem, p2d,
                data, dataIndex);
        pPanel.setComponentPopupMenu(popupMenu);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(pPanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(pPanel));

        panel.addMouseListener(new DebuggingHoverListener(p2d, 0));

        return panel;
    }

    public static JPanel create(Path2DBezier data, GraphicPath2D p2d, int dataIndex, int debuggingStartI) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        char pointLetter = (char) (97 + (dataIndex + 16) % 26);

        var hGroup = layout.createParallelGroup(Alignment.LEADING);
        var vGroup = layout.createSequentialGroup();
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        var popupMenu = new JPopupMenu();
        var insertLineItem = popupMenu.add("Insert Line");
        var insertBezierItem = popupMenu.add("Insert Bezier");
        var deleteItem = popupMenu.add("Delete");
        p2DAddActionListeners(insertLineItem, insertBezierItem, deleteItem, p2d,
                data, dataIndex);

        var p1Panel = create(pointLetter + "2", data.pNext, p2d, debuggingStartI);
        hGroup.addComponent(p1Panel);
        vGroup.addComponent(p1Panel);
        p1Panel.setComponentPopupMenu(popupMenu);

        for (int i = 0; i < data.morePoints.size(); i++) {
            var pointPanel = create(pointLetter + "" + (i + 3), data.morePoints.get(i),
                    p2d,
                    i + debuggingStartI + 1);
            hGroup.addComponent(pointPanel);
            vGroup.addGap(2);
            vGroup.addComponent(pointPanel);
            pointPanel.setComponentPopupMenu(popupMenu);
        }

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        // addButton.addActionListener(e -> {
        // var newCp = new Point(data.morePoints.get(data.morePoints.size() - 2).x + 20,
        // data.morePoints.get(data.morePoints.size() - 2).y + 20);
        // var newPp = new Point(data.morePoints.get(data.morePoints.size() - 1).x + 20,
        // data.morePoints.get(data.morePoints.size() - 1).y + 20);
        // data.morePoints.add(newCp);
        // data.morePoints.add(newPp);
        // GlobalState.needsUpdateEditor = true;
        // });
        addButton.setEnabled(false); // TODO
        minusButton.addActionListener(e -> {
            if (data.morePoints.size() > 2) {
                data.morePoints.remove(data.morePoints.size() - 1);
                data.morePoints.remove(data.morePoints.size() - 1);
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(data.morePoints.size() > 2);

        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(addButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(addButton)
                .addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(p2d, 0));

        return panel;
    }

    public static JPanel create(Path2DData p2d, GraphicPath2D obj, int dataIndex,
            int debugValue) {
        if (p2d instanceof Path2DLine) {
            return create((Path2DLine) p2d, obj, dataIndex, debugValue);
        } else if (p2d instanceof Path2DBezier) {
            return create((Path2DBezier) p2d, obj, dataIndex, debugValue);
        } else {
            return createPlaceholder(p2d);
        }
    }

    public static JPanel create(GraphicPath2D path2d) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPath2D");
        var strokePanel = create("stroke", path2d.stroke, path2d, 0);
        var strokeColorPanel = create("color", path2d.strokeColor, path2d, 0);
        var thicknessPanel = create("thickness", path2d.thickness, 1, 15, 1, path2d,
                0);
        var fillPanel = create("fill", path2d.fill, path2d, 0);
        var fillColorPanel = create("color", path2d.fillColor, path2d, 0);
        var closedPanel = create("closed", path2d.closed, path2d, 0);
        var p1Panel = create("p", path2d.p1, path2d, 1);

        var hGroup = layout.createParallelGroup(Alignment.LEADING)
                .addComponent(label)
                .addComponent(strokePanel)
                .addComponent(strokeColorPanel)
                .addComponent(thicknessPanel)
                .addComponent(fillPanel)
                .addComponent(fillColorPanel)
                .addComponent(closedPanel)
                .addComponent(p1Panel);
        var vGroup = layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(strokePanel)
                .addGap(2)
                .addComponent(strokeColorPanel)
                .addGap(2)
                .addComponent(thicknessPanel)
                .addGap(2)
                .addComponent(fillPanel)
                .addGap(2)
                .addComponent(fillColorPanel)
                .addGap(2)
                .addComponent(closedPanel)
                .addGap(2)
                .addComponent(p1Panel);

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        int dataIndex = 0;
        int debuggingStartI = 2;
        for (var v : path2d.data) {
            var dataPanel = create(v, path2d, dataIndex, debuggingStartI);
            hGroup.addComponent(dataPanel);
            vGroup.addGap(2);
            vGroup.addComponent(dataPanel);
            dataIndex++;
            debuggingStartI += v.size();
        }

        JButton addLineButton = new JButton("++ Line");
        JButton addBezierButton = new JButton("++ Bezier");
        JButton minusButton = new JButton("--");
        // addLineButton.addActionListener(e -> {
        // var lastPoint = path2d.lastPoint();
        // path2d.data.add(new Path2DLine(new Point(lastPoint.x + 20, lastPoint.y +
        // 20)));
        // GlobalState.needsUpdateEditor = true;
        // });
        // addBezierButton.addActionListener(e -> {
        // var lastPoint = path2d.lastPoint();
        // path2d.data.add(new Path2DBezier(new Point(lastPoint.x, lastPoint.y + 20),
        // new Point(lastPoint.x + 20, lastPoint.y), new Point(lastPoint.x + 20,
        // lastPoint.y + 20)));
        // GlobalState.needsUpdateEditor = true;
        // });
        addLineButton.setEnabled(false);
        addBezierButton.setEnabled(false); // TODO
        minusButton.addActionListener(e -> {
            if (path2d.data.size() > 0) {
                path2d.data.remove(path2d.data.size() - 1);
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(path2d.data.size() > 0);

        hGroup.addGroup(layout.createSequentialGroup()
                .addComponent(addLineButton)
                .addComponent(addBezierButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(addLineButton)
                .addComponent(addBezierButton)
                .addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(path2d, 0));

        return panel;
    }

    public static JPanel create(GraphicCircle circle) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicCircle");
        var colorPanel = create("color", circle.color, circle, 0);
        var thicknessPanel = create("thickness", circle.thickness, 1, 15, 1, circle,
                0);
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

    public static JPanel create(GraphicImage image) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicImage");

        var filePathPanel = create("file path", image.filePath, image, 0);

        var originPanel = create("origin", image.origin, image, 1);
        var sizePanel = create("size", image.size, image, 2);

        var opacityPanel = create("opacity", image.opacity, 0.0, 1.0, 0.01, image,
                0);

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

interface Exportable {
    public String exportString();

    public String exportCode();
}

class ImEx {

    public static String exportString(Exportable obj) {
        return obj.exportString();
    }

    public static String exportString(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append(layer.exportString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String exportString(double d) {
        return Double.toString(d);
    }

    public static String exportString(int i) {
        return Integer.toString(i);
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

    public static String exportString(boolean bool) {
        return bool ? "T" : "F";
    }

    public static String exportCode(Exportable obj) {
        return obj.exportCode();
    }

    public static String exportCode(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append("instructions.add(");
            sb.append(layer.exportCode());
            sb.append(");\n");
        }
        return sb.toString();
    }

    public static String exportCode(Color color) {
        return "\"" + ColorHexer.encode(color) + "\"";
    }

    public static String exportCode(Point point) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Point(");
        sb.append(point.x);
        sb.append(",");
        sb.append(point.y);
        sb.append(")");
        return sb.toString();
    }

    public static String exportCode(Dimension dim) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Dimension(");
        sb.append(dim.width);
        sb.append(",");
        sb.append(dim.height);
        sb.append(")");
        return sb.toString();
    }

    public static String exportCode(boolean bool) {
        return bool ? "true" : "false";
    }

    public static String exportCode(double d) {
        return Double.toString(d);
    }

    public static String exportCode(int i) {
        return Integer.toString(i);
    }

    public static List<GraphicLayer> importString(String str) {
        Scanner sc = new Scanner(str);
        List<GraphicLayer> layers = importLayers(sc);
        sc.close();
        return layers;
    }

    public static boolean importBoolean(Scanner sc) {
        return sc.next().equals("T");
    }

    public static String importHexColor(Scanner sc) {
        return sc.next();
    }

    public static Point importPoint(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    public static Dimension importDimension(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Dimension(Integer.parseInt(coords[0]),
                Integer.parseInt(coords[1]));
    }

    public static int importInt(Scanner sc) {
        return sc.nextInt();
    }

    public static double importDouble(Scanner sc) {
        return sc.nextDouble();
    }

    public static String importUserString(Scanner sc) {
        sc.skip(" ");
        return sc.nextLine();
    }

    public static EasingFunction importEasingFunction(Scanner sc) {
        return EasingFunction.valueOf(sc.next());
    }

    private static class AnimValueData<T> {
        public List<AnimatedValue.Timepoint> timepoints;
        public List<T> values;

        public AnimValueData(List<AnimatedValue.Timepoint> timepoints, List<T> values) {
            this.timepoints = timepoints;
            this.values = values;
        }

    }

    private static <T> AnimValueData<T> importAnimValues(Scanner sc, Function<Scanner, T> importFunction) {
        List<AnimatedValue.Timepoint> timepoints = new ArrayList<>();
        List<T> values = new ArrayList<>();
        while (true) {
            var time = sc.nextDouble();
            values.add(importFunction.apply(sc));
            var easingToNext = importEasingFunction(sc);
            timepoints.add(new AnimatedValue.Timepoint(time, easingToNext));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new AnimValueData<>(timepoints, values);
    }

    public static AnimBoolean importAnimBoolean(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importBoolean);
        var anim = new AnimBoolean();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimColor importAnimColor(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importHexColor);
        var anim = new AnimColor();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimPoint importAnimPoint(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importPoint);
        var anim = new AnimPoint();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimDimension importAnimDimension(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importDimension);
        var anim = new AnimDimension();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimDouble importAnimDouble(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importDouble);
        var anim = new AnimDouble();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimInt importAnimInt(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importInt);
        var anim = new AnimInt();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static List<GraphicLayer> importLayers(Scanner sc) {
        List<GraphicLayer> layers = new ArrayList<>();
        while (sc.hasNext()) {
            String type = sc.next();
            switch (type) {
                case "LAYER":
                    layers.add(importLayer(sc));
                    break;
            }
        }
        return layers;
    }

    public static GraphicLayer importLayer(Scanner sc) {
        String layerName = importUserString(sc);
        AnimBoolean visible = importAnimBoolean(sc);
        AnimPoint translate = importAnimPoint(sc);
        AnimPoint rotateOrigin = importAnimPoint(sc);
        AnimDouble rotate = importAnimDouble(sc);
        List<GraphicObject> objects = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
                case "PATH2D":
                    objects.add(importPath2D(sc));
                    break;
                case "CIRCLE":
                    objects.add(importCircle(sc));
                    break;
                case "IMAGE":
                    objects.add(importImage(sc));
                    break;
            }
        }
        return new GraphicLayer(layerName, visible, translate, rotateOrigin, rotate, objects);
    }

    public static Path2DLine importPath2DLine(Scanner sc) {
        AnimPoint pNext = importAnimPoint(sc);
        return new Path2DLine(pNext);
    }

    public static Path2DBezier importPath2DBezier(Scanner sc) {
        AnimPoint pNext = importAnimPoint(sc);
        List<AnimPoint> morePoints = new ArrayList<>();
        while (true) {
            morePoints.add(importAnimPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new Path2DBezier(pNext, morePoints);
    }

    public static GraphicPath2D importPath2D(Scanner sc) {
        AnimBoolean stroke = importAnimBoolean(sc);
        AnimColor strokeColor = importAnimColor(sc);
        AnimInt thickness = importAnimInt(sc);
        AnimBoolean fill = importAnimBoolean(sc);
        AnimColor fillColor = importAnimColor(sc);
        AnimBoolean closed = importAnimBoolean(sc);
        AnimPoint p1 = importAnimPoint(sc);
        List<Path2DData> data = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
                case "LINE":
                    data.add(importPath2DLine(sc));
                    break;
                case "BEZIER":
                    data.add(importPath2DBezier(sc));
                    break;
            }
        }
        return new GraphicPath2D(stroke, strokeColor, thickness, fill, fillColor, closed, p1, data);
    }

    public static GraphicCircle importCircle(Scanner sc) {
        AnimColor color = importAnimColor(sc);
        AnimInt thickness = importAnimInt(sc);
        AnimPoint center = importAnimPoint(sc);
        AnimInt radius = importAnimInt(sc);
        return new GraphicCircle(color, thickness, center, radius);
    }

    public static GraphicImage importImage(Scanner sc) {
        String filePath = importUserString(sc);
        AnimPoint origin = importAnimPoint(sc);
        AnimDimension size = importAnimDimension(sc);
        AnimDouble opacity = importAnimDouble(sc);
        return new GraphicImage(filePath, origin, size, opacity);
    }

}

class GraphicsPanel extends JPanel {
    List<GraphicLayer> instructions;
    double time = 0;

    GraphicsPanel(List<GraphicLayer> instructions) {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        this.instructions = instructions;
    }

    @Override
    protected void paintComponent(Graphics gOuter) {
        super.paintComponent(gOuter);
        var size = this.getSize();
        Graphics g = gOuter.create();
        g.translate(size.width / 2, size.height / 2);
        var debugG = (Graphics2D) gOuter.create();
        debugG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        debugG.translate(size.width / 2, size.height / 2);

        for (GraphicLayer layer : instructions) {
            if (layer.shown.get(time)) {
                layer.draw(g, time);
            }
        }

        for (int i = 0; i < 600; i++) {
            debugG.setColor(i % 2 == 0 ? Color.white : Color.black);
            debugG.fillRect(-301 + i, -305, 1, 5);
            debugG.fillRect(-305, -301 + i, 5, 1);
            debugG.fillRect(300 - i, 300, 1, 5);
            debugG.fillRect(300, 300 - i, 5, 1);
        }

        if (!GlobalState.pannerPanelDragging || GlobalState.pannerShowDebugging) {
            for (GraphicLayer layer : instructions) {
                layer.debugDraw(debugG, time);
            }
        }
    }

}

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

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
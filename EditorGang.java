import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;

class EditorGang {

    static Preferences prefs = Preferences.userRoot().node("2024graphicsprojecteditor");

    private GraphicRoot root;

    private MutableInt currentLayer = new MutableInt(-1);
    private MutableString savePath = new MutableString(null);
    private MutableString saveFolder = new MutableString(null);

    private JScrollPane layerScrollPane = new JScrollPane();
    private JScrollPane editorScrollPane = new JScrollPane();

    private JFrame editorFrame = new JFrame();
    private JFrame timepointsFrame = new JFrame();
    private JFrame timeControlFrame = new JFrame();
    private JFrame paletteFrame = new JFrame();

    EditorGang(JFrame frame, GraphicRoot root) {
        this.root = root;

        createEditorFrame(frame);
        createPaletteFrame(editorFrame);
        createTimepointsFrame(paletteFrame);
        createTimeControlFrame(editorFrame);
        editorFrame.requestFocus();
    }

    private void createEditorFrame(JFrame frame) {
        editorFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        editorFrame.setTitle("editor");
        editorFrame.setSize(600, 600);

        JPanel editorPanel = new JPanel();
        editorFrame.setContentPane(editorPanel);

        GroupLayout layout = new GroupLayout(editorPanel);
        editorPanel.setLayout(layout);

        layerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        layerScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        editorScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        editorScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        updateLayerListPanel();

        JButton addLayerButton = new JButton("add layer");
        addLayerButton.addActionListener(e -> {
            this.root.instructions.add(new GraphicLayer());
            updateLayerListPanel();
        });
        JButton layerUpButton = new JButton("^");
        layerUpButton.addActionListener(e -> {
            if (currentLayer.value > 0) {
                var temp = root.instructions.get(currentLayer.value - 1);
                root.instructions.set(currentLayer.value - 1, root.instructions.get(currentLayer.value));
                root.instructions.set(currentLayer.value, temp);
                currentLayer.value--;
                updateLayerListPanel();
            }
        });

        JButton layerDownButton = new JButton("v");
        layerDownButton.addActionListener(e -> {
            if (currentLayer.value < root.instructions.size() - 1) {
                var temp = root.instructions.get(currentLayer.value + 1);
                root.instructions.set(currentLayer.value + 1, root.instructions.get(currentLayer.value));
                root.instructions.set(currentLayer.value, temp);
                currentLayer.value++;
                updateLayerListPanel();
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(layerScrollPane)
                        .addGroup(layout.createSequentialGroup().addComponent(addLayerButton)
                                .addComponent(layerUpButton).addComponent(layerDownButton)))
                .addComponent(editorScrollPane));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addComponent(layerScrollPane)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(addLayerButton)
                                .addComponent(layerUpButton).addComponent(layerDownButton)))
                .addComponent(editorScrollPane));

        JMenuBar menuBar = new JMenuBar();
        editorFrame.setJMenuBar(menuBar);

        var fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        var saveMenuItem = fileMenu.add("Save");
        var saveAsMenuItem = fileMenu.add("Save as...");
        var loadMenuItem = fileMenu.add("Load");
        fileMenu.addSeparator();
        var exportCodeMenuItem = fileMenu.add("Export code...");

        JFileChooser fileChooser = new JFileChooser(prefs.get("lastSavePath", System.getProperty("user.home")));

        saveMenuItem.addActionListener(e -> {
            if (this.savePath.value == null) {
                if (fileChooser.showSaveDialog(editorFrame) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
                this.saveFolder.value = fileChooser.getCurrentDirectory().getAbsolutePath();
            }

            try {
                Files.write(Path.of(this.savePath.value), ImEx.exportString(root).getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            editorFrame.setTitle("editor - " + new File(this.savePath.value).getName() + " @ "
                    + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            prefs.put("lastSavePath", this.saveFolder.value);
        });

        saveAsMenuItem.addActionListener(e -> {
            if (fileChooser.showSaveDialog(editorFrame) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
            this.saveFolder.value = fileChooser.getCurrentDirectory().getAbsolutePath();
            prefs.put("lastSavePath", this.saveFolder.value);

            try {
                Files.write(Path.of(this.savePath.value), ImEx.exportString(root).getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            editorFrame.setTitle("editor - " + new File(this.savePath.value).getName() + " @ "
                    + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        });

        loadMenuItem.addActionListener(e -> {
            if (fileChooser.showOpenDialog(editorFrame) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            this.savePath.value = fileChooser.getSelectedFile().getAbsolutePath();
            this.saveFolder.value = fileChooser.getCurrentDirectory().getAbsolutePath();
            prefs.put("lastSavePath", this.saveFolder.value);

            try {
                var file = new File(this.savePath.value);
                Scanner scanner = new Scanner(file, "UTF-8");
                var newRoot = ImEx.importRoot(scanner);
                root.instructions = newRoot.instructions;
                root.timeKeypoints = newRoot.timeKeypoints;
                root.palette = newRoot.palette;
                scanner.close();
                editorFrame.setTitle("editor - " + file.getName());
                updateLayerListPanel();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        exportCodeMenuItem.addActionListener(e -> {
            if (fileChooser.showSaveDialog(editorFrame) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            prefs.put("lastSavePath", fileChooser.getCurrentDirectory().getAbsolutePath());

            try {
                Files.write(Path.of(fileChooser.getSelectedFile().getAbsolutePath()),
                        ImEx.exportCode(root).getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        editorFrame.setVisible(true);

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
        if (layerIndex < 0 || layerIndex >= root.instructions.size()) {
            editorScrollPane.setViewportView(new JPanel());
            this.currentLayer.value = layerIndex;
            return;
        }
        boolean sameLayer = layerIndex == this.currentLayer.value;
        int scrollPos = editorScrollPane.getVerticalScrollBar().getValue();
        editorScrollPane.setViewportView(EditingPanelFactory.create(this.root.instructions.get(layerIndex)));
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
        layerLayout.setHorizontalGroup(layerLayout.createSequentialGroup().addGroup(layerRadioHGroup));
        layerLayout.setVerticalGroup(layerVGroup);

        ButtonGroup layerButtonGroup = new ButtonGroup();

        changeEditorPane(Math.max(0, Math.min(this.currentLayer.value, root.instructions.size() - 1)));

        int layerI = 0;
        for (GraphicLayer layer : root.instructions) {

            var layerEditRadio = new JRadioButton(layer.name.value);
            if (layerI == currentLayer.value) {
                layerEditRadio.setSelected(true);
            }
            final int layerI2 = layerI;
            layerEditRadio.addActionListener(e -> {
                changeEditorPane(layerI2);
            });
            layerButtonGroup.add(layerEditRadio);

            layerVGroup.addGroup(layerLayout.createParallelGroup(Alignment.CENTER).addComponent(layerEditRadio));
            layerVGroup.addPreferredGap(ComponentPlacement.RELATED);
            layerRadioHGroup.addComponent(layerEditRadio);

            JPopupMenu layerPopupMenu = new JPopupMenu();
            var layerDeleteMenuItem = layerPopupMenu.add("Delete");
            layerDeleteMenuItem.addActionListener(e -> {
                root.instructions.remove(layer);
                GlobalState.needsUpdateLayers = true;
            });

            layerEditRadio.setComponentPopupMenu(layerPopupMenu);

            layerI++;
        }
        layerScrollPane.setViewportView(layerPane);
    }

    private void createTimepointsFrame(JFrame frame) {
        timepointsFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        timepointsFrame.setTitle("timepoints");
        timepointsFrame.setSize(400, 600);
        timepointsFrame.setVisible(true);

        timepointsFrame.setContentPane(EditingPanelFactory.createPlaceholder(null, "timepoints"));
    }

    private void createTimeControlFrame(JFrame frame) {
        timeControlFrame.setLocation(frame.getLocation().x, frame.getLocation().y + frame.getHeight());
        timeControlFrame.setTitle("time control");
        timeControlFrame.setSize(1200, 150);
        timeControlFrame.setVisible(true);

        timeControlFrame.setContentPane(EditingPanelFactory.createPlaceholder(null, "timecontrol"));
    }

    private void createPaletteFrame(JFrame frame) {
        paletteFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        paletteFrame.setTitle("palette");
        paletteFrame.setSize(150, 600);
        paletteFrame.setVisible(true);

        paletteFrame.setContentPane(EditingPanelFactory.createPlaceholder(null, "palette"));
    }
}
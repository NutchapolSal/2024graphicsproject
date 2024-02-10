import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Dialog.ModalityType;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;

class EditorGang {

    static Preferences prefs = Preferences.userRoot().node("2024graphicsprojecteditor");

    private GraphicRoot root;

    private MutableInt currentLayer = new MutableInt(-1);
    private MutableString savePath = new MutableString(null);
    private MutableString saveFolder = new MutableString(null);
    private long lastSaveTime = System.currentTimeMillis();

    private JScrollPane layerScrollPane = new JScrollPane();
    private JScrollPane editorScrollPane = new JScrollPane();

    private JFrame displayFrame;
    private JFrame editorFrame = new JFrame();
    private JFrame timepointsFrame = new JFrame();
    private JFrame timeControlFrame = new JFrame();
    private JFrame paletteFrame = new JFrame();

    private boolean bringingToFront = false;
    private WeakHashMap<GraphicLayer, JPanel> layerPanels = new WeakHashMap<>();

    EditorGang(JFrame frame, GraphicRoot root) {
        this.root = root;

        displayFrame = frame;
        createEditorFrame(frame);
        createPaletteFrame(editorFrame);
        createTimepointsFrame(paletteFrame);
        createTimeControlFrame(editorFrame);

        detailData();

        timeControlFrame.setVisible(true);
        timepointsFrame.setVisible(true);
        paletteFrame.setVisible(true);
        editorFrame.setVisible(true);

        setupBringToFront();

        new Timer(250, e -> {
            if (GlobalState.needsUpdateEditor) {
                changeEditorPane(this.currentLayer.value, true);
                GlobalState.needsUpdateEditor = false;
            }
            if (GlobalState.needsUpdateLayers) {
                updateLayerListPanel();
                GlobalState.needsUpdateLayers = false;
            }
        }).start();
    }

    private void setupBringToFront() {
        var focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bringingToFront) {
                    return;
                }
                bringToFront();
            }
        };
        displayFrame.addFocusListener(focusListener);
        editorFrame.addFocusListener(focusListener);
        timepointsFrame.addFocusListener(focusListener);
        timeControlFrame.addFocusListener(focusListener);
        paletteFrame.addFocusListener(focusListener);
    }

    private void bringToFront() {
        displayFrame.setAutoRequestFocus(false);
        editorFrame.setAutoRequestFocus(false);
        timepointsFrame.setAutoRequestFocus(false);
        timeControlFrame.setAutoRequestFocus(false);
        paletteFrame.setAutoRequestFocus(false);

        displayFrame.toFront();
        editorFrame.toFront();
        timepointsFrame.toFront();
        timeControlFrame.toFront();
        paletteFrame.toFront();

        displayFrame.setAutoRequestFocus(true);
        editorFrame.setAutoRequestFocus(true);
        timepointsFrame.setAutoRequestFocus(true);
        timeControlFrame.setAutoRequestFocus(true);
        paletteFrame.setAutoRequestFocus(true);
        bringingToFront = false;
    }

    private void createEditorFrame(JFrame frame) {
        editorFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        editorFrame.setTitle("editor");
        editorFrame.setSize(600, 600);
        editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel editorPanel = new JPanel();
        editorFrame.setContentPane(editorPanel);

        GroupLayout layout = new GroupLayout(editorPanel);
        editorPanel.setLayout(layout);

        layerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        layerScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        editorScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        editorScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        JButton addLayerButton = new JButton("add layer");
        addLayerButton.addActionListener(e -> {
            this.root.instructions.add(new GraphicLayer(root.getFirstTimeKeypoint()));
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

        var newMenuItem = fileMenu.add("New");
        var saveMenuItem = fileMenu.add("Save");
        var saveAsMenuItem = fileMenu.add("Save as...");
        var loadMenuItem = fileMenu.add("Load");
        fileMenu.addSeparator();
        var exportCodeMenuItem = fileMenu.add("Export code...");

        JFileChooser fileChooser = new JFileChooser(prefs.get("lastSavePath", System.getProperty("user.home")));

        newMenuItem.addActionListener(e -> {
            if (this.savePath.value != null
                    && Duration.ofSeconds(5).toMillis() < Math.abs(System.currentTimeMillis() - this.lastSaveTime)) {
                int result = JOptionPane.showConfirmDialog(null, "This will discard the current file. Continue?",
                        "New file", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            this.root.newFile();
            this.savePath.value = null;
            this.lastSaveTime = System.currentTimeMillis();
            editorFrame.setTitle("editor");
            detailData();
        });

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
            this.lastSaveTime = System.currentTimeMillis();
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
            this.lastSaveTime = System.currentTimeMillis();
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
                this.lastSaveTime = System.currentTimeMillis();
                detailData();
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

        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        var windowsMenu = new JMenu("Windows");
        menuBar.add(windowsMenu);

        var timepointsMenuItem = new JCheckBoxMenuItem("Timepoints");
        var timeControlMenuItem = new JCheckBoxMenuItem("Time Control");
        var paletteMenuItem = new JCheckBoxMenuItem("Palette");

        windowsMenu.add(timepointsMenuItem);
        windowsMenu.add(timeControlMenuItem);
        windowsMenu.add(paletteMenuItem);

        timepointsFrame.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
                timepointsMenuItem.setState(false);
            }

            public void componentShown(ComponentEvent evt) {
                timepointsMenuItem.setState(true);
            }
        });

        timeControlFrame.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
                timeControlMenuItem.setState(false);
            }

            public void componentShown(ComponentEvent evt) {
                timeControlMenuItem.setState(true);
            }
        });

        paletteFrame.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
                paletteMenuItem.setState(false);
            }

            public void componentShown(ComponentEvent evt) {
                paletteMenuItem.setState(true);
            }
        });

        timepointsMenuItem.addActionListener(e -> {
            if (timepointsMenuItem.getState()) {
                timepointsFrame.setVisible(true);
            } else {
                timepointsFrame.setVisible(false);
            }
        });

        timeControlMenuItem.addActionListener(e -> {
            if (timeControlMenuItem.getState()) {
                timeControlFrame.setVisible(true);
            } else {
                timeControlFrame.setVisible(false);
            }
        });

        paletteMenuItem.addActionListener(e -> {
            if (paletteMenuItem.getState()) {
                paletteFrame.setVisible(true);
            } else {
                paletteFrame.setVisible(false);
            }
        });
    }

    private void changeEditorPane(int layerIndex) {
        changeEditorPane(layerIndex, false);
    }

    private void changeEditorPane(int layerIndex, boolean force) {
        if (layerIndex < 0 || layerIndex >= root.instructions.size()) {
            editorScrollPane.setViewportView(new JPanel());
            this.currentLayer.value = layerIndex;
            return;
        }
        boolean sameLayer = layerIndex == this.currentLayer.value;
        int scrollPos = editorScrollPane.getVerticalScrollBar().getValue();
        var layer = this.root.instructions.get(layerIndex);
        if (force) {
            layerPanels.remove(layer);
        }
        editorScrollPane
                .setViewportView(layerPanels.computeIfAbsent(layer, l -> EditingPanelFactory.create(l, this.root)));
        if (sameLayer) {
            editorScrollPane.getVerticalScrollBar().setValue(scrollPos);
        } else {
            editorScrollPane.getVerticalScrollBar().setValue(0);
        }
        this.currentLayer.value = layerIndex;
    }

    private void detailData() {
        updateLayerListPanel();
        detailTimepointsData();
        detailPaletteData();
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
                if (currentLayer.value != layerI2) {
                    changeEditorPane(layerI2);
                }
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

    enum TimepointSort {
        LABEL(value -> value.exportDisplayString(true, true, false, false), Comparator.comparing(v -> v.label)),
        TIME(value -> value.exportDisplayString(true, true, false, false),
                Comparator.comparingDouble(TimeKeypoint::time)),
        HIERARCHY(value -> value.exportDisplayString(true, true, true, false), TimeKeypoint::compareHierarchy),
        ID(value -> value.exportDisplayString(true, false, false, true), Comparator.comparing(v -> v.id));

        public final Function<TimeKeypoint, String> toStringFunction;
        public final Comparator<TimeKeypoint> comparator;

        TimepointSort(Function<TimeKeypoint, String> toStringFunction, Comparator<TimeKeypoint> comparator) {
            this.toStringFunction = toStringFunction;
            this.comparator = comparator;
        }
    }

    private TimepointSort currentSort = TimepointSort.TIME;
    private Optional<TimeKeypoint> changingReference = Optional.empty();
    private Runnable sortTimepointsFunc;

    private void createTimepointsFrame(JFrame frame) {
        timepointsFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        timepointsFrame.setTitle("timepoints");
        timepointsFrame.setSize(450, 600);

        JPanel timepointsPanel = new JPanel();
        timepointsFrame.setContentPane(timepointsPanel);

        GroupLayout layout = new GroupLayout(timepointsPanel);
        timepointsPanel.setLayout(layout);

        JScrollPane listScrollPane = new JScrollPane();
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        listScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        var listModel = new DefaultListModel<TimeKeypoint>();

        JList<TimeKeypoint> list = new JList<>(listModel);
        listScrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        listScrollPane.setPreferredSize(new Dimension(300, Short.MAX_VALUE));
        listScrollPane.setMinimumSize(new Dimension(150, 0));
        GroupLayout listLayout = new GroupLayout(list);
        list.setLayout(listLayout);

        var timepointsVGroup = listLayout.createSequentialGroup();
        var timepointsHGroup = listLayout.createParallelGroup();
        listLayout.setHorizontalGroup(listLayout.createSequentialGroup().addGroup(timepointsHGroup));
        listLayout.setVerticalGroup(timepointsVGroup);

        listScrollPane.setViewportView(list);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        var sortLabel = new JLabel("sort");
        var sortComboBox = new JComboBox<String>();
        for (TimepointSort sort : TimepointSort.values()) {
            sortComboBox.addItem(sort.name().toLowerCase());
        }
        sortComboBox.setSelectedItem(currentSort.name().toLowerCase());

        sortTimepointsFunc = () -> {
            var selectedTimepoint = list.getSelectedValue();
            root.timeKeypoints.sort(currentSort.comparator);
            listModel.clear();
            listModel.addAll(root.timeKeypoints);
            list.setSelectedValue(selectedTimepoint, true);
        };

        sortComboBox.addActionListener(e -> {
            var sort = TimepointSort.values()[sortComboBox.getSelectedIndex()];
            currentSort = sort;
            sortTimepointsFunc.run();
        });

        list.setCellRenderer(new ListCellRenderer<>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends TimeKeypoint> list, TimeKeypoint value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                var panel = new JPanel();
                var label = new JLabel(currentSort.toStringFunction.apply(value));
                GroupLayout layout = new GroupLayout(panel);
                panel.setLayout(layout);

                var hGroup = layout.createSequentialGroup();
                if (currentSort == TimepointSort.HIERARCHY) {
                    hGroup.addGap(5 + value.referenceDepth() * 10);
                } else {
                    hGroup.addGap(5);
                }
                hGroup.addComponent(label);
                layout.setHorizontalGroup(hGroup);
                layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(label));
                if (isSelected) {
                    panel.setBackground(list.getSelectionBackground());
                    panel.setForeground(list.getSelectionForeground());
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    panel.setBackground(list.getBackground());
                    panel.setForeground(list.getForeground());
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                return panel;
            }
        });

        var idLabel = new JLabel("id");
        var labelLabel = new JLabel("label");
        var offsetLabel = new JLabel("offset");
        var referenceLabel = new JLabel("reference");
        var idField = new JTextField();
        var labelField = new JTextField();
        var offsetField = new JTextField();
        var referenceField = new JTextField();
        var changeReferenceButton = new JButton("🔀");
        var removeReferenceButton = new JButton("❌");
        var goToTimeButton = new JButton("go to time");

        idField.setEnabled(false);
        referenceField.setEnabled(false);

        Runnable tryUpdateReference = () -> {
            var changeRef = changingReference.get();
            var selected = Optional.ofNullable(list.getSelectedValue());
            if (selected.isPresent()) {
                if (changeRef.isValidReference(selected)) {
                    changeRef.setReference(selected, changeReferenceButton);
                    changingReference = Optional.empty();
                    list.setSelectedValue(changeRef, true);
                    sortTimepointsFunc.run();
                } else {
                    referenceField.setText("<-- invalid reference");
                    list.setSelectedValue(null, false);
                }
            }
        };

        // call with null for rendering refreshes,
        // call with forged event for root tkp focus updates
        ListSelectionListener selectionChanged = e -> {
            if (changingReference.isPresent()) {
                tryUpdateReference.run();
                return;
            }

            if (list.getSelectedValue() == null) {
                idField.setText("");
                labelField.setText("");
                offsetField.setText("");
                referenceField.setText("");
                changeReferenceButton.setText("🔀");
                labelField.setEnabled(false);
                offsetField.setEnabled(false);
                goToTimeButton.setEnabled(false);
                changeReferenceButton.setEnabled(false);
                removeReferenceButton.setEnabled(false);
                return;
            }
            var selected = list.getSelectedValue();
            idField.setText(selected.id);
            labelField.setText(selected.label);
            offsetField.setText(Double.toString(selected.getOffset()));
            referenceField.setText(
                    selected.getReference().map(r -> r.exportDisplayString(true, true, false, true)).orElse(""));
            idField.setCaretPosition(0);
            referenceField.setCaretPosition(0);
            labelField.setEnabled(true);
            offsetField.setEnabled(true);
            goToTimeButton.setEnabled(true);
            changeReferenceButton.setText("🔀");
            changeReferenceButton.setEnabled(true);
            removeReferenceButton.setEnabled(selected.getReference().isPresent());

            if (e != null) {
                root.setTimeKeypointFocus(selected, e.getSource());
            }
        };
        list.addListSelectionListener(selectionChanged);

        var tkpFocusCallback = root.subscribeToTKPFocus(tkp -> {
            list.setSelectedValue(tkp, true);
            selectionChanged.valueChanged(null);
        });
        timepointsPanel.putClientProperty("tkpFocusCallback", tkpFocusCallback);

        changeReferenceButton.addActionListener(e -> {
            if (changingReference.isPresent()) {
                var selected = changingReference.get();
                changingReference = Optional.empty();
                list.setSelectedValue(selected, true);
                selectionChanged.valueChanged(null);
                return;
            }
            changingReference = Optional.ofNullable(list.getSelectedValue());
            referenceField.setText("<-- select new reference");
            referenceField.setCaretPosition(0);
            removeReferenceButton.setEnabled(false);
            changeReferenceButton.setText("⛔");
            list.setSelectedValue(null, false);
        });

        removeReferenceButton.addActionListener(e -> {
            list.getSelectedValue().setReference(Optional.empty(), removeReferenceButton);
            selectionChanged.valueChanged(null);
            sortTimepointsFunc.run();
        });

        goToTimeButton.addActionListener(e -> {
            root.setTime(list.getSelectedValue().time(), goToTimeButton);
        });

        Runnable commitChanges = () -> {
            var selected = list.getSelectedValue();
            selected.label = labelField.getText();
            try {
                selected.setOffset(Double.parseDouble(offsetField.getText()), offsetField);
            } catch (NumberFormatException e) {
                offsetField.setText(Double.toString(selected.getOffset()));
            }
            sortTimepointsFunc.run();
        };

        Runnable validateChanges = () -> {
            if (list.getSelectedValue() == null) {
                return;
            }
            try {
                Double.parseDouble(offsetField.getText());
                offsetField.setBackground(EditorColor.Static.color(offsetField));
            } catch (NumberFormatException ex) {
                offsetField.setBackground(EditorColor.Invalid.color());
            }
        };

        offsetField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateChanges.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateChanges.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateChanges.run();
            }
        });

        labelField.addActionListener(e -> {
            commitChanges.run();
        });
        offsetField.addActionListener(e -> {
            commitChanges.run();
        });
        labelField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commitChanges.run();
            }
        });
        offsetField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commitChanges.run();
            }
        });

        var addTimepointButton = new JButton("add timepoint");
        addTimepointButton.addActionListener(e -> {
            TimeKeypoint newTKP;
            if (list.getSelectedValue() == null) {
                newTKP = new TimeKeypoint(root.getTime(), null, "new timepoint");
            } else {
                newTKP = new TimeKeypoint(root.getTime() - list.getSelectedValue().time(), list.getSelectedValue(),
                        "new timepoint");
            }
            root.addTimeKeypoint(newTKP, addTimepointButton);
            sortTimepointsFunc.run();
            list.setSelectedValue(newTKP, true);
        });

        var removeTimepointButton = new JButton("🚫");

        var removeConfirmPopup = new JPopupMenu();
        var removeConfirmMenuItem = removeConfirmPopup.add("Delete Timepoint");
        var removalTkpFocusCallback = root.subscribeToTKPFocus(tkp -> {
            if (!tkp.isPresent()) {
                removeTimepointButton.setEnabled(false);
                removeTimepointButton.setToolTipText(null);
                return;
            }
            if (0 < tkp.get().children.size()) {
                removeTimepointButton.setEnabled(false);
                removeTimepointButton.setToolTipText("remove all references to this timepoint first");
            } else {
                removeTimepointButton.setEnabled(true);
                removeTimepointButton.setToolTipText(null);
            }
        });
        removeTimepointButton.putClientProperty("tkpFocusCallback", removalTkpFocusCallback);

        removeConfirmMenuItem.addActionListener(e -> {
            var selected = list.getSelectedValue();
            root.removeTimeKeypoint(selected, removeTimepointButton);
            sortTimepointsFunc.run();
        });

        removeTimepointButton.addActionListener(e -> {
            removeConfirmPopup.show(removeTimepointButton, 0, removeTimepointButton.getHeight());
        });

        var filler = Box.createGlue();

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addGap(2).addComponent(sortLabel).addGap(2)
                                .addComponent(sortComboBox))
                        .addComponent(listScrollPane))
                .addGap(10)
                .addGroup(
                        layout.createParallelGroup(Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup().addComponent(idLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(idField))
                                .addGroup(layout.createSequentialGroup().addComponent(labelLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(labelField))
                                .addGroup(layout.createSequentialGroup().addComponent(offsetLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(offsetField))
                                .addGroup(layout.createSequentialGroup().addComponent(referenceLabel)
                                        .addGap(0, 300, Short.MAX_VALUE).addComponent(changeReferenceButton)
                                        .addComponent(removeReferenceButton))
                                .addComponent(referenceField).addComponent(goToTimeButton).addComponent(filler)
                                .addGroup(layout.createSequentialGroup().addComponent(addTimepointButton)
                                        .addGap(0, 0, Short.MAX_VALUE).addComponent(removeTimepointButton)))
                .addGap(10));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(sortLabel)
                        .addComponent(sortComboBox, GroupLayout.DEFAULT_SIZE, 25, 25))
                .addComponent(listScrollPane))
                .addGroup(layout.createSequentialGroup().addGap(5)
                        .addGroup(layout
                                .createParallelGroup(Alignment.BASELINE).addComponent(idLabel).addComponent(idField))
                        .addGap(2)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(labelLabel)
                                .addComponent(labelField))
                        .addGap(2)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(offsetLabel)
                                .addComponent(offsetField))
                        .addGap(2)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(referenceLabel)
                                .addComponent(changeReferenceButton).addComponent(removeReferenceButton))
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(referenceField)).addGap(2)
                        .addComponent(goToTimeButton).addComponent(filler)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(addTimepointButton)
                                .addComponent(removeTimepointButton))));

    }

    private void detailTimepointsData() {
        sortTimepointsFunc.run();
        changingReference = Optional.empty();
    }

    private void createTimeControlFrame(JFrame frame) {
        timeControlFrame.setLocation(frame.getLocation().x, frame.getLocation().y + frame.getHeight());
        timeControlFrame.setTitle("time control");
        timeControlFrame.setSize(600, 100);

        JPanel timeControlPanel = new JPanel();
        timeControlFrame.setContentPane(timeControlPanel);

        GroupLayout layout = new GroupLayout(timeControlPanel);
        timeControlPanel.setLayout(layout);

        JButton playButton = new JButton();
        var timerStateCallback = root.subscribeToTimeRunning(running -> playButton.setText(running ? "⏸" : "▶"));
        timeControlPanel.putClientProperty("timerStateCallback", timerStateCallback);

        playButton.addActionListener(e -> {
            if (root.getTimeRunning()) {
                root.pauseTime(playButton);
            } else {
                root.startTime(playButton);
            }
        });

        JButton stopButton = new JButton("⏹");
        stopButton.addActionListener(e -> {
            root.pauseTime(stopButton);
            root.setTime(0, stopButton);
        });

        JTextField timeField = new JTextField();
        var timeCallback = root.subscribeToTime(time -> SwingUtilities.invokeLater(() -> {
            timeField.setText(Double.toString(time));
        }));
        timeControlPanel.putClientProperty("timeCallback", timeCallback);

        Runnable changedTimeFunction = () -> {
            try {
                Double.parseDouble(timeField.getText());
                timeField.setBackground(EditorColor.Static.color(timeField));
            } catch (NumberFormatException ex) {
                timeField.setBackground(EditorColor.Invalid.color());
            }
        };
        Runnable commitTimeFunction = () -> {
            try {
                root.setTime(Double.parseDouble(timeField.getText()), timeControlPanel);
            } catch (NumberFormatException ex) {
                timeField.setText(Double.toString(root.getTime()));
            }
        };
        timeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedTimeFunction.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedTimeFunction.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changedTimeFunction.run();
            }
        });
        timeField.addActionListener(e -> {
            commitTimeFunction.run();
        });
        timeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commitTimeFunction.run();
            }
        });

        SpinnerNumberModel fpsModel = new SpinnerNumberModel();
        fpsModel.setMinimum(1);
        fpsModel.setStepSize(1);
        fpsModel.setValue(60);
        JSpinner fpsSpinner = new JSpinner(fpsModel);
        JCheckBox fpsCheckBox = new JCheckBox("fps");
        fpsCheckBox.setSelected(root.getFps().isPresent());
        fpsSpinner.setEnabled(root.getFps().isPresent());

        fpsCheckBox.addActionListener(e -> {
            if (fpsCheckBox.isSelected()) {
                fpsSpinner.setEnabled(true);
                root.setFps(Optional.of((int) fpsSpinner.getValue()));
            } else {
                fpsSpinner.setEnabled(false);
                root.setFps(Optional.empty());
            }
        });
        fpsSpinner.addChangeListener(e -> {
            root.setFps(Optional.of((int) fpsSpinner.getValue()));
        });

        var filler = Box.createGlue();
        var filler2 = Box.createGlue();

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(filler)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup().addComponent(playButton).addComponent(stopButton)
                                .addComponent(timeField, GroupLayout.DEFAULT_SIZE, 150, 150)))
                .addComponent(filler2).addGroup(layout.createSequentialGroup().addComponent(fpsCheckBox)
                        .addComponent(fpsSpinner, GroupLayout.DEFAULT_SIZE, 50, 50)));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(filler)
                .addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(playButton)
                        .addComponent(stopButton).addComponent(timeField, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(filler2).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(fpsCheckBox)
                        .addComponent(fpsSpinner)));

    }

    private PaletteSlotTransferHandler transferHandler = new PaletteSlotTransferHandler();
    private JScrollPane listScrollPane = new JScrollPane();

    private void createPaletteFrame(JFrame frame) {
        paletteFrame.setLocation(frame.getLocation().x + frame.getWidth(), frame.getLocation().y);
        paletteFrame.setTitle("palette");
        paletteFrame.setSize(150, 600);

        JPanel palettePanel = new JPanel();
        paletteFrame.setContentPane(palettePanel);

        GroupLayout layout = new GroupLayout(palettePanel);
        palettePanel.setLayout(layout);

        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        listScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(listScrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(listScrollPane));
    }

    private void detailPaletteData() {
        JPanel slotsPanel = new JPanel();
        listScrollPane.setViewportView(slotsPanel);
        GroupLayout slotsLayout = new GroupLayout(slotsPanel);
        slotsPanel.setLayout(slotsLayout);

        Map<Integer, ParallelGroup> slotsXGroups = new HashMap<>();
        Map<Integer, ParallelGroup> slotsYGroups = new HashMap<>();

        int maxX = root.palette.getMaxX();
        int maxY = root.palette.getMaxY();
        for (int x = root.palette.getMinX(); x <= maxX + 2; x++) {
            for (int y = root.palette.getMinY(); y <= maxY + 5; y++) {
                var slotPanel = new PaletteSlotPanel(root.palette, x, y, transferHandler);

                slotsXGroups.computeIfAbsent(x, k -> slotsLayout.createParallelGroup()).addComponent(slotPanel);
                slotsYGroups.computeIfAbsent(y, k -> slotsLayout.createParallelGroup()).addComponent(slotPanel);
            }
        }

        var slotsXGroupsList = slotsXGroups.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey()))
                .collect(Collectors.toList());
        var slotsYGroupsList = slotsYGroups.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey()))
                .collect(Collectors.toList());

        var slotsHGroup = slotsLayout.createSequentialGroup();
        var slotsVGroup = slotsLayout.createSequentialGroup();
        for (var e : slotsXGroupsList) {
            slotsHGroup.addGap(2).addGroup(e.getValue());
        }
        for (var e : slotsYGroupsList) {
            slotsVGroup.addGap(2).addGroup(e.getValue());
        }

        slotsLayout.setHorizontalGroup(slotsHGroup);
        slotsLayout.setVerticalGroup(slotsVGroup);
    }

}
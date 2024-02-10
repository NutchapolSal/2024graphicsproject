import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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

    static class PannerPanels {
        public final JPanel main;
        public final JPanel x;
        public final JPanel y;

        public PannerPanels(JPanel main, JPanel x, JPanel y) {
            this.main = main;
            this.x = x;
            this.y = y;
        }
    }

    private static PannerPanels createPannerPanel(MouseInputListener xListener, MouseInputListener yListener,
            Debuggable obj, int debugValue) {
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

        pannerPanel.addMouseListener(xListener);
        pannerPanel.addMouseListener(yListener);
        pannerPanel.addMouseMotionListener(xListener);
        pannerPanel.addMouseMotionListener(yListener);
        pannerXPanel.addMouseListener(xListener);
        pannerXPanel.addMouseMotionListener(xListener);
        pannerYPanel.addMouseListener(yListener);
        pannerYPanel.addMouseMotionListener(yListener);

        return new PannerPanels(pannerPanel, pannerXPanel, pannerYPanel);
    }

    public static JPanel create(String labelText, Point point, Debuggable obj, int debugValue) {
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

        var xListener = new PannerPanelListener(xSpinner, () -> point.x, MouseEvent::getX);
        var yListener = new PannerPanelListener(ySpinner, () -> point.y, MouseEvent::getY);

        var pannerPanels = createPannerPanel(xListener, yListener, obj, debugValue);

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(xSpinner).addComponent(ySpinner).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(pannerPanels.y).addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(pannerPanels.main).addComponent(pannerPanels.x)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerPanels.x)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(xSpinner)
                        .addComponent(ySpinner).addComponent(pannerPanels.y).addComponent(pannerPanels.main)));

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

        var xListener = new PannerPanelListener(wSpinner, () -> dim.width, MouseEvent::getX);
        var yListener = new PannerPanelListener(hSpinner, () -> dim.height, MouseEvent::getY);

        var pannerPanels = createPannerPanel(xListener, yListener, obj, debugValue);

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(wSpinner).addComponent(hSpinner).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(pannerPanels.y).addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(pannerPanels.main).addComponent(pannerPanels.x)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerPanels.x)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(wSpinner)
                        .addComponent(hSpinner).addComponent(pannerPanels.y).addComponent(pannerPanels.main)));

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
            });
        }

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(textField));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(textField));

        return panel;
    }

    public static JPanel create(String labelText, MutableDouble doub, double min, double max, double stepSize,
            Debuggable obj, int debugValue) {
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

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(slider).addComponent(spinner));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(slider)
                .addComponent(spinner));

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, MutableInt integer, int min, int max, int stepSize, Debuggable obj,
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
        });
        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
        });

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(slider).addComponent(spinner));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(slider)
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
            Color newColor = JColorChooser.showDialog(null, "Choose a color", color.value);
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

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(button).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField));

        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(button)
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

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(checkBox));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(checkBox));

        checkBox.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    private static JPopupMenu easingPopup = getEasingPopup();
    private static Consumer<EasingFunction> easingCallback = e -> {
    };

    private static JPopupMenu getEasingPopup() {
        adjustTooltips();
        JList<EasingFunction> list = new JList<>();
        var listEasings = new EasingFunction[(int) (Math.ceil(EasingFunction.values().length / 3.0) * 3)];
        var noEases = EasingFunction.nonEases();
        int noEasesZoneLength = (int) (Math.ceil(noEases.length / 3.0) * 3);
        for (int i = 0; i < noEasesZoneLength; i++) {
            listEasings[i] = i < noEases.length ? noEases[i] : null;
        }
        var easeIns = EasingFunction.easeIns();
        var easeInOuts = EasingFunction.easeInOuts();
        var easeOuts = EasingFunction.easeOuts();
        for (int i = 0; i < easeIns.length; i++) {
            int index = noEasesZoneLength + i * 3;
            listEasings[index] = easeIns[i];
            listEasings[index + 1] = easeInOuts[i];
            listEasings[index + 2] = easeOuts[i];
        }

        list.setListData(listEasings);

        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(listEasings.length / 3);

        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setCellRenderer(new ListCellRenderer<>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends EasingFunction> list, EasingFunction value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                var panel = new JPanel();
                if (value == null) {
                    panel.setForeground(list.getForeground());
                    panel.setBackground(list.getBackground());
                    return panel;
                }
                var icon = new ImageIcon(value.icon());
                var iconLabel = new JLabel(icon);
                panel.add(iconLabel);
                panel.setToolTipText(value.name());
                if (isSelected) {
                    panel.setBackground(list.getSelectionBackground());
                    panel.setForeground(list.getSelectionForeground());
                } else {
                    panel.setBackground(list.getBackground());
                    panel.setForeground(list.getForeground());
                }
                return panel;
            }
        });

        list.addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                list.setSelectedIndex(list.locationToIndex(e.getPoint()));
            }
        });

        list.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var selected = list.getSelectedValue();
                if (selected != null) {
                    easingCallback.accept(selected);
                    easingPopup.setVisible(false);
                }
            }
        });

        var popupMenu = new JPopupMenu();
        popupMenu.add(list);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            boolean ignoreNextInvis = false;

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (ignoreNextInvis) {
                    ignoreNextInvis = false;
                    return;
                }
                var selected = list.getSelectedValue();
                if (selected != null) {
                    easingCallback.accept(list.getSelectedValue());
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                ignoreNextInvis = true;
            }
        });
        return popupMenu;
    }

    private static void askForEasing(Component locationRef, Consumer<EasingFunction> callback) {
        easingCallback = callback;
        easingPopup.show(locationRef, 0, locationRef.getHeight());
    }

    private static class AnimPanelReturns<T> {
        public final JPanel panel;
        public final Consumer<T> dataCallIn;

        public AnimPanelReturns(JPanel panel, Consumer<T> dataCallIn) {
            this.panel = panel;
            this.dataCallIn = dataCallIn;
        }
    }

    public static <T> AnimPanelReturns<T> createAnimPanel(AnimatedValue<T> animValue, GraphicRoot root,
            Supplier<T> getCurrentUIValue, DoubleConsumer stateSetter, Consumer<EditorColor> colorSetter) {

        var state = new Object() {
            public double timeTempped;
            public boolean isTempped;
        };

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JButton jumpToTKPButton = new JButton("↗");

        JPopupMenu jumpToMenu = new JPopupMenu();
        jumpToTKPButton.addActionListener(e -> {
            jumpToMenu.removeAll();
            var timepoints = animValue.getTimepoint(root.getTime());
            if (timepoints.isEmpty()) {
                return;
            }
            if (timepoints.size() == 1) {
                root.setTimeKeypointFocus(timepoints.get(0), jumpToTKPButton);
                return;
            }
            for (var tkp : timepoints) {
                var menuItem = new JMenuItem(tkp.exportDisplayString(true, false, false, true));
                menuItem.addActionListener(e2 -> {
                    root.setTimeKeypointFocus(tkp, jumpToTKPButton);
                });
                jumpToMenu.add(menuItem);
            }
            jumpToMenu.show(jumpToTKPButton, 0, jumpToTKPButton.getHeight());
        });

        JButton timeKeypointButton = new JButton();

        JButton easingsButton = new JButton(EasingFunction.emptyImageIcon);
        easingsButton.setToolTipText(null);

        var ttkpFocusCallback = root.subscribeToTimeAndTKPFocus(ttkp -> {
            if (state.isTempped && ttkp.time == state.timeTempped) {
                colorSetter.accept(EditorColor.Temporary);
            } else {
                state.isTempped = false;
                stateSetter.accept(ttkp.time);
                if (animValue.isAnimated()) {
                    colorSetter.accept(EditorColor.getTimepointTypeColor(animValue.getTimepointCount(ttkp.time),
                            animValue.hasTimepoint(ttkp.tkpFocus), ttkp.isPresent() && ttkp.get().time() == ttkp.time));
                } else {
                    colorSetter.accept(EditorColor.Static);
                }
            }

            var tpCount = animValue.getTimepointCount(ttkp.time);
            if (1 < tpCount) {
                jumpToTKPButton.setEnabled(true);
                jumpToTKPButton.setToolTipText("focus on timepoint...");
            } else if (tpCount == 1) {
                if (animValue.getTimepoint(ttkp.time).get(0) != ttkp.tkpFocus.orElse(null)) {
                    jumpToTKPButton.setEnabled(true);
                    jumpToTKPButton.setToolTipText("focus on timepoint");
                } else {
                    jumpToTKPButton.setEnabled(false);
                    jumpToTKPButton.setToolTipText("already on timepoint");
                }
            } else if (tpCount == 0) {
                jumpToTKPButton.setEnabled(false);
                jumpToTKPButton.setToolTipText(null);
            }
            timeKeypointButton.setText(tpCount == 0 ? "▪" : "🔶");

            Optional<EasingFunction> easing = null;
            timeKeypointButton.setToolTipText(null);
            easingsButton.setEnabled(false);
            timeKeypointButton.setEnabled(false);

            if (ttkp.isPresent() && ttkp.get().time() == ttkp.time) {
                timeKeypointButton.setEnabled(true);
                timeKeypointButton.setToolTipText("bind to timepoint");

                if (animValue.hasTimepoint(ttkp.get())) {
                    timeKeypointButton.setToolTipText("unbind from timepoint");
                    easingsButton.setEnabled(true);
                    easing = animValue.getEasingFunction(ttkp.get());

                    if (!animValue.isAnimated()) {
                        timeKeypointButton.setEnabled(false);
                        timeKeypointButton.setToolTipText("you cannot unbind the last timepoint");
                    }
                }
            }

            if (easing == null) {
                easing = animValue.getEasingFunction(ttkp.time);
            }
            easingsButton.setToolTipText(easing.map(e -> e.name()).orElse(null));

            if (easing.isPresent()) {
                easingsButton.setIcon(easing.get().imageIcon());
            } else {
                easingsButton.setIcon(EasingFunction.emptyImageIcon);
            }
        });
        easingsButton.addActionListener(e -> {
            askForEasing(easingsButton, easing -> {
                animValue.setEasingFunction(root.getTimeKeypointFocus().get(), easing);
                ttkpFocusCallback.accept(root.getTimeAndTKPFocus());
            });
        });
        timeKeypointButton.addActionListener(e -> {
            var tkp = root.getTimeKeypointFocus();
            if (!tkp.isPresent()) {
                return;
            }
            if (animValue.hasTimepoint(tkp)) {
                animValue.removeTimepoint(tkp.get());
            } else {
                animValue.addForEditor(tkp.get(), getCurrentUIValue.get(), EasingFunction.snap);
            }
            ttkpFocusCallback.accept(root.getTimeAndTKPFocus());
        });

        Consumer<T> dataCallIn = t -> {
            if (!animValue.isAnimated()) {
                animValue.staticSet(t);
                return;
            }
            var ttkp = root.getTimeAndTKPFocus();
            if (ttkp.isPresent() && ttkp.get().time() == ttkp.time && animValue.hasTimepoint(ttkp.get())) {
                animValue.replaceForEditor(ttkp.get(), t);
            } else {
                state.timeTempped = ttkp.time;
                state.isTempped = true;
                colorSetter.accept(EditorColor.Temporary);
            }

        };

        // new Timer(1000, e -> {
        //     colorSetter.accept(EditorColor.TestColor);
        // }).start();

        panel.putClientProperty("timeTkpFocusCallback", ttkpFocusCallback);

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(jumpToTKPButton)
                .addComponent(timeKeypointButton).addComponent(easingsButton));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jumpToTKPButton)
                .addComponent(timeKeypointButton).addComponent(easingsButton));

        return new AnimPanelReturns<>(panel, dataCallIn);
    }

    public static JPanel create(String labelText, AnimBoolean animBool, GraphicRoot root, Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JCheckBox checkBox = new JCheckBox();

        var animPanelData = createAnimPanel(animBool, root, () -> checkBox.isSelected(), t -> {
            checkBox.setSelected(animBool.get(t));
        }, c -> {
            checkBox.setBackground(c.color(checkBox));
        });
        var filler = Box.createHorizontalGlue();

        var animPanel = animPanelData.panel;
        checkBox.addActionListener(e -> {
            animPanelData.dataCallIn.accept(checkBox.isSelected());
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(checkBox).addComponent(filler).addComponent(animPanel));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label)
                .addComponent(checkBox).addComponent(filler).addComponent(animPanel));

        checkBox.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, AnimString animStr, GraphicRoot root, Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JTextField textField = new JTextField();

        var animPanelData = createAnimPanel(animStr, root, () -> textField.getText(), t -> {
            textField.setText(animStr.get(t));
        }, c -> {
            textField.setBackground(c.color(textField));
        });

        var animPanel = animPanelData.panel;
        textField.addActionListener(e -> {
            animPanelData.dataCallIn.accept(textField.getText());
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                animPanelData.dataCallIn.accept(textField.getText());
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(textField).addComponent(animPanel));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label)
                .addComponent(textField).addComponent(animPanel));

        textField.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, AnimDouble animDouble, GraphicRoot root, double min, double max,
            double stepSize, Debuggable obj, int debugValue) {
        return create(labelText, animDouble, root, min, true, max, true, stepSize, obj, debugValue);
    }

    public static JPanel create(String labelText, AnimDouble animDouble, GraphicRoot root, double min,
            boolean hardLimitMin, double max, boolean hardLimitMax, double stepSize, Debuggable obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (int) ((max - min) / stepSize);
        JSlider slider = new JSlider(0, sliderSteps, 0);
        var spinModel = new SpinnerNumberModel();
        if (hardLimitMin) {
            spinModel.setMinimum(min);
        }
        if (hardLimitMax) {
            spinModel.setMaximum(max);
        }
        spinModel.setStepSize(stepSize);
        spinModel.setValue(animDouble.get(root.getTime()));

        JSpinner spinner = new JSpinner(spinModel);

        var animPanelData = createAnimPanel(animDouble, root, () -> (double) spinner.getValue(), t -> {
            spinner.setValue(animDouble.get(t));
            slider.setValue((int) ((animDouble.get(t) - min) / stepSize));
        }, c -> {
            spinner.setBackground(c.color(spinner));
            slider.setBackground(c.color(slider));
        });

        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
        });
        spinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept((double) spinner.getValue());
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(slider).addComponent(spinner).addComponent(animPanelData.panel));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(slider)
                .addComponent(spinner).addComponent(animPanelData.panel));

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, AnimInt animInt, GraphicRoot root, int min, int max, int stepSize,
            Debuggable obj, int debugValue) {
        return create(labelText, animInt, root, min, true, max, true, stepSize, obj, debugValue);
    }

    public static JPanel create(String labelText, AnimInt animInt, GraphicRoot root, int min, boolean hardLimitMin,
            int max, boolean hardLimitMax, int stepSize, Debuggable obj, int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        int sliderSteps = (max - min) / stepSize;
        JSlider slider = new JSlider(0, sliderSteps, 0);
        var spinModel = new SpinnerNumberModel();
        if (hardLimitMin) {
            spinModel.setMinimum(min);
        }
        if (hardLimitMax) {
            spinModel.setMaximum(max);
        }
        spinModel.setStepSize(stepSize);
        spinModel.setValue(animInt.get(root.getTime()));

        JSpinner spinner = new JSpinner(spinModel);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(2);

        var animPanelData = createAnimPanel(animInt, root, () -> (int) spinner.getValue(), t -> {
            spinner.setValue(animInt.get(t));
            slider.setValue((animInt.get(t) - min) / stepSize);
        }, c -> {
            spinner.setBackground(c.color(spinner));
            slider.setBackground(c.color(slider));
        });

        slider.addChangeListener(e -> {
            spinner.setValue(slider.getValue() * stepSize + min);
        });
        spinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept((int) spinner.getValue());
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(slider).addComponent(spinner).addComponent(animPanelData.panel));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(slider)
                .addComponent(spinner).addComponent(animPanelData.panel));

        slider.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, AnimColor animColor, GraphicRoot root, Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        ColorButton button = new ColorButton();
        JTextField textField = new JTextField();
        JPopupMenu textEditConfirmPopup = new JPopupMenu();

        var confirmItem = textEditConfirmPopup.add("Unlock");

        var state = new Object() {
            public MaybePaletteValue mpv;
        };

        Consumer<Boolean> lockTextfield = b -> {
            if (b) {
                textField.setEnabled(false);
                textField.setComponentPopupMenu(textEditConfirmPopup);
                textField.setToolTipText("right click to unlock");
            } else {
                textField.setEnabled(true);
                textField.setComponentPopupMenu(null);
                textField.setToolTipText(null);
            }
        };

        Consumer<MaybePaletteValue> refreshUi = mpv -> {
            state.mpv = mpv;
            button.setColor(state.mpv.get());
            if (state.mpv.isPaletteValue) {
                var pv = state.mpv.paletteValue;
                textField.setText(pv.label + " (" + ColorHexer.encode(pv.color) + ") - " + pv.id);
                textField.setCaretPosition(0);
                lockTextfield.accept(true);
            } else {
                lockTextfield.accept(false);
                textField.setText(ColorHexer.encode(state.mpv.get()));
            }
        };

        confirmItem.addActionListener(e -> {
            lockTextfield.accept(false);
        });

        var animPanelData = createAnimPanel(animColor, root, () -> state.mpv, t -> {
            refreshUi.accept(animColor.get(t));
        }, c -> {
            textField.setBackground(c.color(textField));
        });

        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", state.mpv.get());
            if (newColor != null) {
                var mpv = new MaybePaletteValue(newColor);
                refreshUi.accept(mpv);
                animPanelData.dataCallIn.accept(mpv);
            }
        });

        button.setOnDrop(v -> {
            refreshUi.accept(v);
            animPanelData.dataCallIn.accept(v);
        });
        button.setTransferHandler(new ColorButtonTransferHandler());

        Runnable newColorFunction = () -> {
            var parsedColor = ColorHexer.decodeOptional(textField.getText());
            if (parsedColor.isPresent()) {
                button.setColor(parsedColor.get());
            } else {
                button.setInvalid();
            }
            animPanelData.dataCallIn.accept(new MaybePaletteValue(parsedColor.orElse(Color.black)));
        };
        textField.addActionListener(e -> {
            newColorFunction.run();
        });

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                newColorFunction.run();
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(button)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(textField).addComponent(animPanelData.panel));

        layout.setVerticalGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(button)
                .addComponent(textField).addComponent(animPanelData.panel));

        button.addMouseListener(new DebuggingHoverListener(obj, debugValue));

        return panel;
    }

    public static JPanel create(String labelText, AnimPoint animPoint, GraphicRoot root, Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        SpinnerNumberModel xModel = new SpinnerNumberModel();
        SpinnerNumberModel yModel = new SpinnerNumberModel();
        JSpinner xSpinner = new JSpinner(xModel);
        JSpinner ySpinner = new JSpinner(yModel);

        var xListener = new PannerPanelListener(xSpinner, () -> animPoint.get(root.getTime()).x, MouseEvent::getX);
        var yListener = new PannerPanelListener(ySpinner, () -> animPoint.get(root.getTime()).y, MouseEvent::getY);

        var pannerPanels = createPannerPanel(xListener, yListener, obj, debugValue);

        var animPanelData = createAnimPanel(animPoint, root,
                () -> new Point((int) xSpinner.getValue(), (int) ySpinner.getValue()), t -> {
                    var p = animPoint.get(t);
                    xSpinner.setValue(p.x);
                    ySpinner.setValue(p.y);
                }, c -> {
                    // xSpinner.getEditor().getComponent(0).setBackground(c.color(xSpinner));
                    // ySpinner.setBackground(c.color(ySpinner));

                    pannerPanels.x.setBackground(c.color(pannerPanels.x));
                    pannerPanels.y.setBackground(c.color(pannerPanels.y));
                    pannerPanels.main.setBackground(c.color(pannerPanels.main));
                });

        xSpinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept(new Point((int) xSpinner.getValue(), (int) ySpinner.getValue()));
        });
        ySpinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept(new Point((int) xSpinner.getValue(), (int) ySpinner.getValue()));
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(xSpinner).addComponent(ySpinner).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(pannerPanels.y).addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(pannerPanels.main).addComponent(pannerPanels.x))
                        .addComponent(animPanelData.panel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerPanels.x)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(xSpinner)
                        .addComponent(ySpinner).addComponent(pannerPanels.y).addComponent(pannerPanels.main)
                        .addComponent(animPanelData.panel)));

        return panel;
    }

    public static JPanel create(String labelText, AnimDimension animDim, GraphicRoot root, Debuggable obj,
            int debugValue) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        SpinnerNumberModel xModel = new SpinnerNumberModel();
        SpinnerNumberModel yModel = new SpinnerNumberModel();
        JSpinner xSpinner = new JSpinner(xModel);
        JSpinner ySpinner = new JSpinner(yModel);

        var animPanelData = createAnimPanel(animDim, root,
                () -> new Dimension((int) xSpinner.getValue(), (int) ySpinner.getValue()), t -> {
                    var d = animDim.get(t);
                    xSpinner.setValue(d.width);
                    ySpinner.setValue(d.height);
                }, c -> {
                    xSpinner.setBackground(c.color(xSpinner));
                    ySpinner.setBackground(c.color(ySpinner));
                });

        xSpinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept(new Dimension((int) xSpinner.getValue(), (int) ySpinner.getValue()));
        });
        ySpinner.addChangeListener(e -> {
            animPanelData.dataCallIn.accept(new Dimension((int) xSpinner.getValue(), (int) ySpinner.getValue()));
        });

        var xListener = new PannerPanelListener(xSpinner, () -> animDim.get(root.getTime()).width, MouseEvent::getX);
        var yListener = new PannerPanelListener(ySpinner, () -> animDim.get(root.getTime()).height, MouseEvent::getY);

        var pannerPanels = createPannerPanel(xListener, yListener, obj, debugValue);

        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(xSpinner).addComponent(ySpinner).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(pannerPanels.y).addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(pannerPanels.main).addComponent(pannerPanels.x))
                        .addComponent(animPanelData.panel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerPanels.x)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(xSpinner)
                        .addComponent(ySpinner).addComponent(pannerPanels.y).addComponent(pannerPanels.main)
                        .addComponent(animPanelData.panel)));

        return panel;
    }

    public static JPanel createPlaceholder(Object object) {
        return createPlaceholder(object, null);
    }

    public static JPanel createPlaceholder(Object object, String message) {
        var labelText = "⚠️\ufe0f ";
        if (object != null) {
            labelText += object.getClass().getSimpleName();
        }
        if (message != null) {
            if (object != null) {
                labelText += ": ";
            }
            labelText += message;
        }
        JPanel jPanel = new JPanel();
        JLabel label = new JLabel(labelText);
        jPanel.add(label);
        return jPanel;
    }

    public static JPanel create(GraphicObject object, GraphicRoot root) {
        if (object instanceof GraphicPath2D) {
            return create((GraphicPath2D) object, root);
        } else if (object instanceof GraphicCircle) {
            return create((GraphicCircle) object, root);
        } else if (object instanceof GraphicEllipse) {
            return create((GraphicEllipse) object, root);
        } else if (object instanceof GraphicImage) {
            return create((GraphicImage) object, root);
        } else if (object instanceof GraphicString) {
            return create((GraphicString) object, root);
        } else {
            return createPlaceholder(object);
        }
    }

    public static JPanel create(GraphicLayer layer, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        var vGroup = layout.createSequentialGroup();
        var hGroup = layout.createParallelGroup();
        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(10).addGroup(hGroup).addGap(10));
        vGroup.addGap(5);
        layout.setVerticalGroup(vGroup);

        var layerNamePanel = create("layer", layer.name, null, 0);
        var shownPanel = create("shown", layer.shown, root, layer, 0);
        var translatePanel = create("translate", layer.translate, root, layer, 1);
        var rotateOriginPanel = create("rotateOrigin", layer.rotateOrigin, root, layer, 2);
        var rotatePanel = create("rotate", layer.rotate, root, -360, false, 360, false, 1, layer, 3);
        vGroup.addComponent(layerNamePanel).addGap(2).addComponent(shownPanel).addGap(2).addComponent(translatePanel)
                .addGap(2).addComponent(rotateOriginPanel).addGap(2).addComponent(rotatePanel);
        hGroup.addComponent(layerNamePanel).addComponent(shownPanel).addComponent(translatePanel)
                .addComponent(rotateOriginPanel).addComponent(rotatePanel);
        for (var gObj : layer.objects) {
            var objPanel = create(gObj, root);
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
        comboBox.addItem("GraphicEllipse");
        comboBox.addItem("GraphicImage");
        comboBox.addItem("GraphicString");

        comboBox.setSelectedIndex(GlobalState.lastSelectedNewObjI);

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            switch ((String) comboBox.getSelectedItem()) {
            case "GraphicPath2D":
                layer.add(new GraphicPath2D(root.getFirstTimeKeypoint()));
                break;
            case "GraphicCircle":
                layer.add(new GraphicCircle(root.getFirstTimeKeypoint()));
                break;
            case "GraphicEllipse":
                layer.add(new GraphicEllipse(root.getFirstTimeKeypoint()));
                break;
            case "GraphicImage":
                layer.add(new GraphicImage(root.getFirstTimeKeypoint()));
                break;
            case "GraphicString":
                layer.add(new GraphicString(root.getFirstTimeKeypoint()));
                break;
            default:
                return;
            }

            GlobalState.needsUpdateEditor = true;

        });

        GlobalState.lastSelectedNewObjI = comboBox.getSelectedIndex();

        vGroup.addPreferredGap(ComponentPlacement.RELATED);
        vGroup.addGroup(
                layout.createParallelGroup(Alignment.CENTER, false).addComponent(comboBox).addComponent(addButton));
        hGroup.addGroup(layout.createSequentialGroup().addComponent(comboBox).addComponent(addButton));
        return panel;
    }

    static void p2DAddActionListeners(GraphicRoot root, JMenuItem insertLineItem, JMenuItem insertBezierItem,
            JMenuItem deleteItem, GraphicPath2D p2d, Path2DData data, int dataIndex) {
        insertLineItem.addActionListener(e -> {
            var newData = new Path2DLine(root.getFirstTimeKeypoint());
            p2d.data.add(dataIndex, newData);
            GlobalState.needsUpdateEditor = true;
        });
        insertBezierItem.addActionListener(e -> {
            var newData = new Path2DBezier(root.getFirstTimeKeypoint());
            p2d.data.add(dataIndex, newData);
            GlobalState.needsUpdateEditor = true;
        });
        deleteItem.addActionListener(e -> {
            p2d.data.remove(data);
            GlobalState.needsUpdateEditor = true;
        });
    }

    public static JPanel create(Path2DLine data, GraphicRoot root, GraphicPath2D p2d, int dataIndex,
            int debuggingStartI) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        char pointLetter = (char) (97 + (dataIndex + 16) % 26);

        var pPanel = create(pointLetter + "", data.pNext, root, p2d, debuggingStartI);

        var popupMenu = new JPopupMenu();
        var insertLineItem = popupMenu.add("Insert Line");
        var insertBezierItem = popupMenu.add("Insert Bezier");
        var deleteItem = popupMenu.add("Delete");
        p2DAddActionListeners(root, insertLineItem, insertBezierItem, deleteItem, p2d, data, dataIndex);
        pPanel.setComponentPopupMenu(popupMenu);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(pPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pPanel));

        panel.addMouseListener(new DebuggingHoverListener(p2d, 0));

        return panel;
    }

    public static JPanel create(Path2DBezier data, GraphicRoot root, GraphicPath2D p2d, int dataIndex,
            int debuggingStartI) {
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
        p2DAddActionListeners(root, insertLineItem, insertBezierItem, deleteItem, p2d, data, dataIndex);

        var p1Panel = create(pointLetter + "2", data.pNext, root, p2d, debuggingStartI);
        hGroup.addComponent(p1Panel);
        vGroup.addComponent(p1Panel);
        p1Panel.setComponentPopupMenu(popupMenu);

        for (int i = 0; i < data.morePoints.size(); i++) {
            var pointPanel = create(pointLetter + "" + (i + 3), data.morePoints.get(i), root, p2d,
                    i + debuggingStartI + 1);
            hGroup.addComponent(pointPanel);
            vGroup.addGap(2);
            vGroup.addComponent(pointPanel);
            pointPanel.setComponentPopupMenu(popupMenu);
        }

        JButton addButton = new JButton("+");
        JButton minusButton = new JButton("-");
        addButton.addActionListener(e -> {
            data.editorAddNewPoints(root.getFirstTimeKeypoint());
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (data.morePoints.size() > 2) {
                data.morePoints.remove(data.morePoints.size() - 1);
                data.morePoints.remove(data.morePoints.size() - 1);
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(data.morePoints.size() > 2);

        hGroup.addGroup(layout.createSequentialGroup().addComponent(addButton).addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(addButton).addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(p2d, 0));

        return panel;
    }

    public static JPanel create(Path2DData p2d, GraphicRoot root, GraphicPath2D obj, int dataIndex, int debugValue) {
        if (p2d instanceof Path2DLine) {
            return create((Path2DLine) p2d, root, obj, dataIndex, debugValue);
        } else if (p2d instanceof Path2DBezier) {
            return create((Path2DBezier) p2d, root, obj, dataIndex, debugValue);
        } else {
            return createPlaceholder(p2d);
        }
    }

    public static JPanel create(GraphicPath2D path2d) {
        return create(path2d, null);
    }

    public static JPanel create(GraphicPath2D path2d, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicPath2D");
        var strokePanel = create("stroke", path2d.stroke, root, path2d, 0);
        var strokeColorPanel = create("color", path2d.strokeColor, root, path2d, 0);
        var thicknessPanel = create("thickness", path2d.thickness, root, 1, 15, 1, path2d, 0);
        var fillPanel = create("fill", path2d.fill, root, path2d, 0);
        var fillColorPanel = create("color", path2d.fillColor, root, path2d, 0);
        var closedPanel = create("closed", path2d.closed, root, path2d, 0);
        var p1Panel = create("p", path2d.p1, root, path2d, 1);

        var hGroup = layout.createParallelGroup(Alignment.LEADING).addComponent(label).addComponent(strokePanel)
                .addComponent(strokeColorPanel).addComponent(thicknessPanel).addComponent(fillPanel)
                .addComponent(fillColorPanel).addComponent(closedPanel).addComponent(p1Panel);
        var vGroup = layout.createSequentialGroup().addComponent(label).addComponent(strokePanel).addGap(2)
                .addComponent(strokeColorPanel).addGap(2).addComponent(thicknessPanel).addGap(2).addComponent(fillPanel)
                .addGap(2).addComponent(fillColorPanel).addGap(2).addComponent(closedPanel).addGap(2)
                .addComponent(p1Panel);

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        int dataIndex = 0;
        int debuggingStartI = 2;
        for (var v : path2d.data) {
            var dataPanel = create(v, root, path2d, dataIndex, debuggingStartI);
            hGroup.addComponent(dataPanel);
            vGroup.addGap(2);
            vGroup.addComponent(dataPanel);
            dataIndex++;
            debuggingStartI += v.size();
        }

        JButton addLineButton = new JButton("++ Line");
        JButton addBezierButton = new JButton("++ Bezier");
        JButton minusButton = new JButton("--");
        addLineButton.addActionListener(e -> {
            path2d.data.add(new Path2DLine(root.getFirstTimeKeypoint()));
            GlobalState.needsUpdateEditor = true;
        });
        addBezierButton.addActionListener(e -> {
            path2d.data.add(new Path2DBezier(root.getFirstTimeKeypoint()));
            GlobalState.needsUpdateEditor = true;
        });
        minusButton.addActionListener(e -> {
            if (path2d.data.size() > 0) {
                path2d.data.remove(path2d.data.size() - 1);
                GlobalState.needsUpdateEditor = true;
            }
        });
        minusButton.setEnabled(path2d.data.size() > 0);

        hGroup.addGroup(layout.createSequentialGroup().addComponent(addLineButton).addComponent(addBezierButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(addLineButton)
                .addComponent(addBezierButton).addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(path2d, 0));

        return panel;
    }

    public static JPanel create(GraphicCircle circle, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicCircle");

        var strokePanel = create("stroke", circle.stroke, root, circle, 0);
        var strokeColorPanel = create("color", circle.strokeColor, root, circle, 0);
        var thicknessPanel = create("thickness", circle.thickness, root, 1, true, 15, false, 1, circle, 0);
        var fillPanel = create("fill", circle.fill, root, circle, 0);
        var fillColorPanel = create("color", circle.fillColor, root, circle, 0);
        var pointPanel = create("center", circle.center, root, circle, 1);
        var radiusPanel = create("radius", circle.radius, root, 0, true, 50, false, 1, circle, 2);

        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING).addComponent(label).addComponent(strokePanel)
                        .addComponent(strokeColorPanel).addComponent(thicknessPanel).addComponent(fillPanel)
                        .addComponent(fillColorPanel).addComponent(pointPanel).addComponent(radiusPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(label)

                .addComponent(strokePanel).addGap(2).addComponent(strokeColorPanel).addGap(2)
                .addComponent(thicknessPanel).addGap(2).addComponent(fillPanel).addGap(2).addComponent(fillColorPanel)
                .addGap(2).addComponent(pointPanel).addGap(2).addComponent(radiusPanel));

        panel.addMouseListener(new DebuggingHoverListener(circle, 0));
        pointPanel.addMouseListener(new DebuggingHoverListener(circle, 1));
        radiusPanel.addMouseListener(new DebuggingHoverListener(circle, 2));

        return panel;
    }

    public static JPanel create(GraphicEllipse circle, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicEllipse");
        var strokePanel = create("stroke", circle.stroke, root, circle, 0);
        var strokeColorPanel = create("color", circle.strokeColor, root, circle, 0);
        var thicknessPanel = create("thickness", circle.thickness, root, 1, true, 15, false, 1, circle, 0);
        var fillPanel = create("fill", circle.fill, root, circle, 0);
        var fillColorPanel = create("color", circle.fillColor, root, circle, 0);
        var pointPanel = create("center", circle.center, root, circle, 1);
        var radiusAPanel = create("radiusA", circle.radiusA, root, 0, true, 50, false, 1, circle, 2);
        var radiusBPanel = create("radiusB", circle.radiusB, root, 0, true, 50, false, 1, circle, 2);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(label)
                .addComponent(strokePanel).addComponent(strokeColorPanel).addComponent(thicknessPanel)
                .addComponent(fillPanel).addComponent(fillColorPanel).addComponent(pointPanel)
                .addComponent(radiusAPanel).addComponent(radiusBPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(label).addComponent(strokePanel).addGap(2)
                .addComponent(strokeColorPanel).addGap(2).addComponent(thicknessPanel).addGap(2).addComponent(fillPanel)
                .addGap(2).addComponent(fillColorPanel).addGap(2).addComponent(pointPanel).addGap(2)
                .addComponent(radiusAPanel).addGap(2).addComponent(radiusBPanel));

        panel.addMouseListener(new DebuggingHoverListener(circle, 0));
        pointPanel.addMouseListener(new DebuggingHoverListener(circle, 1));
        radiusAPanel.addMouseListener(new DebuggingHoverListener(circle, 2));
        radiusBPanel.addMouseListener(new DebuggingHoverListener(circle, 3));

        return panel;
    }

    public static JPanel create(GraphicImage image, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicImage");

        var filePathPanel = create("file path", image.filePath, image, 0);

        var originPanel = create("origin", image.origin, root, image, 1);
        var sizePanel = create("size", image.size, root, image, 2);

        var opacityPanel = create("opacity", image.opacity, root, 0.0, 1.0, 0.01, image, 0);

        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING).addComponent(label).addComponent(filePathPanel)
                        .addComponent(originPanel).addComponent(sizePanel).addComponent(opacityPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(label).addComponent(filePathPanel).addGap(2)
                .addComponent(originPanel).addGap(2).addComponent(sizePanel).addGap(2).addComponent(opacityPanel));

        panel.addMouseListener(new DebuggingHoverListener(image, 0));
        originPanel.addMouseListener(new DebuggingHoverListener(image, 1));
        sizePanel.addMouseListener(new DebuggingHoverListener(image, 2));

        return panel;
    }

    public static JPanel create(GraphicString string, GraphicRoot root) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JLabel label = new JLabel("GraphicString");

        var textPanel = create("text", string.text, root, string, 0);
        var fontFacePanel = create("font face", string.fontFace, root, string, 1);
        var fontSizePanel = create("font size", string.fontSize, root, 0, true, 100, false, 1, string, 2);
        var strokeColorPanel = create("stroke color", string.strokeColor, root, string, 3);
        var positionPanel = create("position", string.position, root, string, 4);
        var alignmentPanel = create("alignment", string.alignment, root, -1, 1, 1, string, 5);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(label)
                .addComponent(textPanel).addComponent(fontFacePanel).addComponent(fontSizePanel)
                .addComponent(strokeColorPanel).addComponent(positionPanel).addComponent(alignmentPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(label).addComponent(textPanel).addGap(2)
                .addComponent(fontFacePanel).addGap(2).addComponent(fontSizePanel).addGap(2)
                .addComponent(strokeColorPanel).addGap(2).addComponent(positionPanel).addGap(2)
                .addComponent(alignmentPanel));

        panel.addMouseListener(new DebuggingHoverListener(string, 0));
        positionPanel.addMouseListener(new DebuggingHoverListener(string, 1));
        alignmentPanel.addMouseListener(new DebuggingHoverListener(string, 2));

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

    private static void adjustTooltips() {
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setReshowDelay(500);
    }
}
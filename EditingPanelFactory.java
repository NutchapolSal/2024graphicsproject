import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(xSpinner).addComponent(ySpinner)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(pannerYPanel).addGroup(layout
                        .createParallelGroup(Alignment.CENTER).addComponent(pannerPanel).addComponent(pannerXPanel)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerXPanel)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(xSpinner)
                        .addComponent(ySpinner).addComponent(pannerYPanel).addComponent(pannerPanel)));

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

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(wSpinner).addComponent(hSpinner)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(pannerYPanel).addGroup(layout
                        .createParallelGroup(Alignment.CENTER).addComponent(pannerPanel).addComponent(pannerXPanel)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pannerXPanel)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(wSpinner)
                        .addComponent(hSpinner).addComponent(pannerYPanel).addComponent(pannerPanel)));

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
        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(10).addGroup(hGroup).addGap(10));
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
        vGroup.addGroup(
                layout.createParallelGroup(Alignment.CENTER, false).addComponent(comboBox).addComponent(addButton));
        hGroup.addGroup(layout.createSequentialGroup().addComponent(comboBox).addComponent(addButton));
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
        p2DAddActionListeners(insertLineItem, insertBezierItem, deleteItem, p2d, data, dataIndex);
        pPanel.setComponentPopupMenu(popupMenu);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(pPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pPanel));

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
        p2DAddActionListeners(insertLineItem, insertBezierItem, deleteItem, p2d, data, dataIndex);

        var p1Panel = create(pointLetter + "2", data.pNext, p2d, debuggingStartI);
        hGroup.addComponent(p1Panel);
        vGroup.addComponent(p1Panel);
        p1Panel.setComponentPopupMenu(popupMenu);

        for (int i = 0; i < data.morePoints.size(); i++) {
            var pointPanel = create(pointLetter + "" + (i + 3), data.morePoints.get(i), p2d, i + debuggingStartI + 1);
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

        hGroup.addGroup(layout.createSequentialGroup().addComponent(addButton).addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(addButton).addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(p2d, 0));

        return panel;
    }

    public static JPanel create(Path2DData p2d, GraphicPath2D obj, int dataIndex, int debugValue) {
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
        var thicknessPanel = create("thickness", path2d.thickness, 1, 15, 1, path2d, 0);
        var fillPanel = create("fill", path2d.fill, path2d, 0);
        var fillColorPanel = create("color", path2d.fillColor, path2d, 0);
        var closedPanel = create("closed", path2d.closed, path2d, 0);
        var p1Panel = create("p", path2d.p1, path2d, 1);

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

        hGroup.addGroup(layout.createSequentialGroup().addComponent(addLineButton).addComponent(addBezierButton)
                .addComponent(minusButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(addLineButton)
                .addComponent(addBezierButton).addComponent(minusButton));

        panel.addMouseListener(new DebuggingHoverListener(path2d, 0));

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

        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING).addComponent(label).addComponent(colorPanel)
                        .addComponent(thicknessPanel).addComponent(pointPanel).addComponent(radiusPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(label).addComponent(colorPanel).addGap(2)
                .addComponent(thicknessPanel).addGap(2).addComponent(pointPanel).addGap(2).addComponent(radiusPanel));

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

        var opacityPanel = create("opacity", image.opacity, 0.0, 1.0, 0.01, image, 0);

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
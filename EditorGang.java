import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Optional;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class EditorGang {

    static Preferences prefs = Preferences.userRoot().node("2024graphicsprojecteditor");

    private GraphicRoot root;

    private JFrame displayFrame;
    private JFrame timeControlFrame = new JFrame();

    private boolean bringingToFront = false;

    EditorGang(JFrame frame, GraphicRoot root) {
        this.root = root;

        displayFrame = frame;
        createTimeControlFrame(frame);

        timeControlFrame.setVisible(true);

        setupBringToFront();

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
        timeControlFrame.addFocusListener(focusListener);
    }

    private void bringToFront() {
        displayFrame.setAutoRequestFocus(false);
        timeControlFrame.setAutoRequestFocus(false);

        displayFrame.toFront();
        timeControlFrame.toFront();

        displayFrame.setAutoRequestFocus(true);
        timeControlFrame.setAutoRequestFocus(true);
        bringingToFront = false;
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

}
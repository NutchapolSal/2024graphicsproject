import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class PaletteSlotPanel extends JPanel {
    private Palette palette;
    private int x;
    private int y;

    private JPopupMenu popup = new JPopupMenu();
    private JMenuItem editLabel = new JMenuItem("Edit...");
    private JMenuItem removeItem = new JMenuItem("Mark for Removal");
    private JMenuItem addItem = new JMenuItem("New...");

    private Runnable changeCallback;

    private static JFrame pvEditor = new JFrame("Palette Value Editor");

    private static Consumer<Color> onColorChange = null;
    private static BiConsumer<String, Color> onOk = null;
    private static Runnable onCancel = null;
    private static BiConsumer<String, Color> onEdit = null;

    static {
        pvEditor.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        GroupLayout pvEditorLayout = new GroupLayout(pvEditor.getContentPane());
        pvEditor.getContentPane().setLayout(pvEditorLayout);

        JTextField labelField = new JTextField();
        JLabel labelFieldLabel = new JLabel("Label");
        JColorChooser colorChooser = new JColorChooser();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (onOk != null) {
                onOk.accept(labelField.getText(), colorChooser.getColor());
            }
            pvEditor.setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            if (onCancel != null) {
                onCancel.run();
            }
            pvEditor.setVisible(false);
        });

        colorChooser.getSelectionModel().addChangeListener(e -> {
            if (onColorChange != null) {
                onColorChange.accept(colorChooser.getColor());
            }
        });

        onEdit = (label, color) -> {
            labelField.setText(label);
            colorChooser.setColor(color);
        };

        pvEditorLayout.setHorizontalGroup(pvEditorLayout.createSequentialGroup().addGap(5).addGroup(pvEditorLayout
                .createParallelGroup()
                .addGroup(pvEditorLayout.createSequentialGroup().addComponent(labelFieldLabel).addComponent(labelField))
                .addComponent(colorChooser).addGroup(Alignment.TRAILING,
                        pvEditorLayout.createSequentialGroup().addComponent(okButton).addComponent(cancelButton)))
                .addGap(5)

        );
        pvEditorLayout.setVerticalGroup(pvEditorLayout.createSequentialGroup().addGap(5)
                .addGroup(pvEditorLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelFieldLabel)
                        .addComponent(labelField))
                .addComponent(colorChooser)
                .addGroup(pvEditorLayout.createParallelGroup().addComponent(okButton).addComponent(cancelButton))
                .addGap(5)

        );
        pvEditor.pack();
    }

    static void edit(String startLabel, Color startColor, Consumer<Color> cc, BiConsumer<String, Color> ok,
            Runnable cancel) {
        if (onOk != null) {
            onCancel.run();
        }

        onColorChange = cc;

        onOk = (label, color) -> {
            ok.accept(label, color);

            onOk = null;
            onColorChange = null;
            onCancel = null;
        };

        onCancel = () -> {
            cancel.run();
            onOk = null;
            onColorChange = null;
            onCancel = null;
        };

        onEdit.accept(startLabel, startColor);

        pvEditor.setVisible(true);
    }

    PaletteSlotPanel(Palette palette, int x, int y, PaletteSlotTransferHandler transferHandler,
            Runnable changeCallback) {
        super();
        this.palette = palette;
        this.x = x;
        this.y = y;
        this.changeCallback = changeCallback;

        this.setPreferredSize(new Dimension(25, 25));
        this.setMaximumSize(new Dimension(25, 25));

        this.setTransferHandler(transferHandler);
        this.putClientProperty("canPaletteValueMove", true);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, e, TransferHandler.MOVE);
            }
        });
        this.setComponentPopupMenu(popup);

        editLabel.addActionListener(e -> {
            var pv = palette.get(x, y).get();
            var originalColor = pv.color;
            edit(pv.label, pv.color, c -> {
                pv.color = c;
                updatePanel();
            }, (label, color) -> {
                pv.label = label;
                pv.color = color;
                updatePanel();
            }, () -> {
                pv.color = originalColor;
                updatePanel();
            });
        });

        removeItem.addActionListener(e -> {
            palette.get(x, y).ifPresent(value -> {
                value.markedForRemoval = !value.markedForRemoval;
                updatePanel();
            });
        });

        addItem.addActionListener(e -> {
            edit("a color", Color.black, c -> {
            }, (label, color) -> {
                setPaletteValue(new PaletteValue(color, label));
            }, () -> {
            });
        });

        updatePanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!palette.get(x, y).isPresent()) {
            return;
        }
        if (palette.get(x, y).get().markedForRemoval) {
            g.setColor(Color.WHITE);
            g.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawLine(getWidth() - 1, 0, 0, getHeight() - 1);
        }
    }

    private void updatePanel() {
        var value = palette.get(x, y);
        popup.removeAll();
        if (value.isPresent()) {
            this.setBackground(value.get().color);
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            String tooltipText = "<html>" + ColorHexer.encode(value.get().color) + "<br>" + value.get().label + "<br>"
                    + value.get().id;
            this.putClientProperty("paletteValue", value.get());
            if (!value.get().markedForRemoval) {
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                removeItem.setText("Mark for Removal");
            } else {
                removeItem.setText("Unmark for Removal");
                this.setBorder(BorderFactory.createLineBorder(Color.RED));
                tooltipText += "<br>Will be removed at next save if not used";
            }
            this.setToolTipText(tooltipText);
            popup.add(editLabel);
            popup.add(removeItem);
        } else {
            this.setBackground(null);
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.setCursor(null);
            this.setToolTipText(null);
            this.putClientProperty("paletteValue", null);
            popup.add(addItem);
        }
    }

    boolean selfSetted = false;

    public void setPaletteValue(PaletteValue value) {
        if (value == palette.get(x, y).orElse(null)) {
            selfSetted = true;
        } else {
            selfSetted = false;
        }
        palette.set(x, y, value);
        updatePanel();
        changeCallback.run();
    }

    public void removePaletteValue() {
        selfSetted = true;
        palette.remove(x, y);
        updatePanel();
        changeCallback.run();
    }

    public void exportDone() {
        if (selfSetted) {
            selfSetted = false;
            return;
        }
        removePaletteValue();
    }

}

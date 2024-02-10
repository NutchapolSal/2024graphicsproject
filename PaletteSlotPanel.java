import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

public class PaletteSlotPanel extends JPanel {
    private Palette palette;
    private int x;
    private int y;

    private JPopupMenu popup = new JPopupMenu();
    JMenuItem removeItem;

    PaletteSlotPanel(Palette palette, int x, int y, PaletteSlotTransferHandler transferHandler) {
        super();
        this.palette = palette;
        this.x = x;
        this.y = y;

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
        JMenuItem editLabel = popup.add("Edit Label");
        JMenuItem editColorItem = popup.add("Edit Color");
        removeItem = popup.add("Mark for Removal");

        editColorItem.addActionListener(e -> {
            var value = palette.get(x, y).get();
            var color = value.color;
            var newColor = JColorChooser.showDialog(this, "Choose Color", color);
            if (newColor != null) {
                value.color = newColor;
                updatePanel();
            }
        });

        editLabel.addActionListener(e -> {
            var value = palette.get(x, y).get();
            var label = value.label;
            var newLabel = (String) JOptionPane.showInputDialog(this, "Enter Label", "Edit Label",
                    javax.swing.JOptionPane.PLAIN_MESSAGE, null, null, label);
            if (newLabel != null) {
                value.label = newLabel;
                updatePanel();
            }
        });

        removeItem.addActionListener(e -> {
            palette.get(x, y).ifPresent(value -> {
                value.markedForRemoval = !value.markedForRemoval;
                updatePanel();
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
            g.setColor(Color.BLACK);
            g.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawLine(getWidth() - 1, 0, 0, getHeight() - 1);
        }
    }

    private void updatePanel() {
        var value = palette.get(x, y);
        if (value.isPresent()) {
            this.setBackground(value.get().color);
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            String tooltipText = "<html>" + ColorHexer.encode(value.get().color) + "<br>" + value.get().label + "<br>"
                    + value.get().id;
            this.setComponentPopupMenu(popup);
            this.putClientProperty("paletteValue", value.get());
            if (!value.get().markedForRemoval) {
                removeItem.setText("Mark for Removal");
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            } else {
                removeItem.setText("Unmark for Removal");
                this.setBorder(BorderFactory.createLineBorder(Color.RED));
                tooltipText += "<br>Will be removed at next save if not used";
            }
            this.setToolTipText(tooltipText);
        } else {
            this.setBackground(null);
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.setCursor(null);
            this.setToolTipText(null);
            this.setComponentPopupMenu(null);
            this.putClientProperty("paletteValue", null);
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
    }

    public void removePaletteValue() {
        selfSetted = true;
        palette.remove(x, y);
        updatePanel();
    }

    public void exportDone() {
        if (selfSetted) {
            selfSetted = false;
            return;
        }
        removePaletteValue();
    }

}

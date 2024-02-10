import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

    PaletteSlotPanel(Palette palette, int x, int y, PaletteSlotTransferHandler transferHandler) {
        super();
        this.palette = palette;
        this.x = x;
        this.y = y;

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.setPreferredSize(new Dimension(25, 25));
        this.setMaximumSize(new Dimension(25, 25));

        this.setTransferHandler(transferHandler);
        this.putClientProperty("canPaletteValueMove", true);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (MouseEvent.BUTTON1 != e.getButton()) {
                    return;
                }
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, e, TransferHandler.MOVE);
            }
        });
        JMenuItem editColorItem = popup.add("Edit Color");
        JMenuItem editLabel = popup.add("Edit Label");

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

        updatePanel();
    }

    private void updatePanel() {
        var value = palette.get(x, y);
        if (value.isPresent()) {
            this.setBackground(value.get().color);
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            this.setToolTipText("<html>" + ColorHexer.encode(value.get().color) + "<br>" + value.get().label + "<br>"
                    + value.get().id);
            this.setComponentPopupMenu(popup);
            this.putClientProperty("paletteValue", value.get());
        } else {
            this.setBackground(null);
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

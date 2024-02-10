import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class PaletteSlotPanel extends JPanel {
    private Palette palette;
    private int x;
    private int y;

    PaletteSlotPanel(Palette palette, int x, int y, PaletteSlotTransferHandler transferHandler) {
        super();
        this.palette = palette;
        this.x = x;
        this.y = y;
        updatePanel();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.setPreferredSize(new Dimension(25, 25));
        this.setMaximumSize(new Dimension(25, 25));

        this.setTransferHandler(transferHandler);
        this.putClientProperty("canPaletteValueMove", true);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                // handler.exportAsDrag(c, e, handler.getSourceActions(c));
                handler.exportAsDrag(c, e, TransferHandler.MOVE);
            }
        });
    }

    private void updatePanel() {
        var value = palette.get(x, y);
        if (value.isPresent()) {
            this.setBackground(value.get().color);
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            this.setToolTipText("<html>" + ColorHexer.encode(value.get().color) + "<br>" + value.get().label + "<br>"
                    + value.get().id);
            this.putClientProperty("paletteValue", value.get());
        } else {
            this.setBackground(null);
            this.setCursor(null);
            this.setToolTipText(null);
            this.putClientProperty("paletteValue", null);
        }
    }

    public void setPaletteValue(PaletteValue value) {
        palette.set(x, y, value);
        updatePanel();
    }

    public void removePaletteValue() {
        palette.remove(x, y);
        updatePanel();
    }

}

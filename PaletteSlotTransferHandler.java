import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

final class PaletteSlotTransferHandler extends TransferHandler {
    static final DataFlavor pvFlavor = new DataFlavor(PaletteValue.class,
            "application/x-2024graphicsproject.palettevalue");

    private static final class TransferablePaletteValue implements Transferable {
        private final PaletteValue pv;
        private DataFlavor lastFlavor;

        private TransferablePaletteValue(PaletteValue pv) {
            this.pv = pv;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(pvFlavor) || flavor.equals(DataFlavor.stringFlavor);
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { pvFlavor, DataFlavor.stringFlavor };
        }

        public Object getTransferData(DataFlavor flavor) {
            if (flavor.equals(pvFlavor)) {
                lastFlavor = pvFlavor;
                return pv;
            } else if (flavor.equals(DataFlavor.stringFlavor)) {
                lastFlavor = DataFlavor.stringFlavor;
                return ColorHexer.encode(pv.color);
            }
            return null;
        }

        public DataFlavor getLastUsedFlavor() {
            return lastFlavor;
        }

    }

    PaletteSlotTransferHandler() {

    }

    // Exporting
    public int getSourceActions(JComponent c) {
        return TransferHandler.LINK | TransferHandler.COPY | TransferHandler.MOVE;
    }

    protected Transferable createTransferable(JComponent c) {
        var pv = (PaletteValue) c.getClientProperty("paletteValue");
        if (pv == null) {
            return null;
        }
        return new TransferablePaletteValue(pv);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == TransferHandler.MOVE) {
            if (data != null) {
                var lastFlavor = ((TransferablePaletteValue) data).getLastUsedFlavor();
                if (lastFlavor.equals(pvFlavor)) {
                    ((PaletteSlotPanel) source).exportDone();
                }
            }
        }
    }

    // Importing

    public boolean canImport(TransferSupport support) {
        if ((MOVE & support.getDropAction()) == MOVE) {
            if (support.isDataFlavorSupported(pvFlavor) || support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return true;
            }
        }
        if ((COPY & support.getDropAction()) == COPY) {
            if (support.isDataFlavorSupported(pvFlavor) || support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return true;
            }
        }
        return false;
    }

    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        try {
            PaletteValue pv = (PaletteValue) support.getTransferable().getTransferData(pvFlavor);
            PaletteSlotPanel c = (PaletteSlotPanel) support.getComponent();
            if (support.getDropAction() == MOVE) {
                c.setPaletteValue(pv);
            } else {
                c.setPaletteValue(pv.editorCopy());
            }
            return true;
        } catch (ClassCastException | UnsupportedFlavorException | IOException e) {

        }

        try {
            String colorHex = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            Color color = ColorHexer.decode(colorHex);
            PaletteSlotPanel c = (PaletteSlotPanel) support.getComponent();
            c.setPaletteValue(new PaletteValue(color, "a color"));
            return true;
        } catch (UnsupportedFlavorException | IOException | ClassCastException ex) {
        }
        return false;
    }
}
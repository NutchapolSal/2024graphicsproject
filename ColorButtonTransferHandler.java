import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.TransferHandler;

public class ColorButtonTransferHandler extends TransferHandler {
    static final DataFlavor pvFlavor = PaletteSlotTransferHandler.pvFlavor;

    ColorButtonTransferHandler() {

    }

    public boolean canImport(TransferSupport support) {
        if ((LINK & support.getSourceDropActions()) == LINK) {
            if (support.isDataFlavorSupported(pvFlavor)) {
                support.setDropAction(LINK);
                return true;
            }
        }
        return false;
    }

    public boolean importData(TransferSupport support) {

        try {
            PaletteValue pv = (PaletteValue) support.getTransferable().getTransferData(pvFlavor);
            ColorButton c = (ColorButton) support.getComponent();
            c.sendData(pv);
            return true;
        } catch (ClassCastException | UnsupportedFlavorException | IOException e) {

        }

        return false;
    }
}

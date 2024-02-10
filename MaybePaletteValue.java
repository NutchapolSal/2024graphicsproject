import java.awt.Color;

class MaybePaletteValue implements Exportable {
    public final boolean isPaletteValue;
    public final PaletteValue paletteValue;
    public final Color color;

    public MaybePaletteValue(PaletteValue paletteValue) {
        this.isPaletteValue = true;
        this.paletteValue = paletteValue;
        this.color = null;
    }

    public MaybePaletteValue(Color color) {
        this.isPaletteValue = false;
        this.paletteValue = null;
        this.color = color;
    }

    public Color get() {
        if (isPaletteValue) {
            return paletteValue.color;
        }
        return color;
    }

    public String exportString() {
        if (isPaletteValue) {
            return "PALETTEVALUE " + ImEx.exportString(paletteValue);
        }
        return "COLOR " + ImEx.exportString(color);
    }

    public String exportCode() {
        if (isPaletteValue) {
            return ImEx.exportCode(paletteValue);
        }
        return ImEx.exportCode(color);
    }
}
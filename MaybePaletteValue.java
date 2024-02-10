import java.awt.Color;

class MaybePaletteValue {
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
}
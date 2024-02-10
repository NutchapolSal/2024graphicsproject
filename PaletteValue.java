import java.awt.Color;

class PaletteValue {
    public final String id;
    public Color color;
    public String label = "a color";

    public PaletteValue(String id, Color color, String label) {
        this.id = id;
        this.color = color;
        this.label = label;
    }

    public PaletteValue(String id, String hexColor, String label) {
        this(id, ColorHexer.decode(hexColor), label);
    }

}

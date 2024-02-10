import java.awt.Color;
import java.util.UUID;

class PaletteValue implements Referenceable {
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

    public PaletteValue(String hexColor, String label) {
        this(UUID.randomUUID().toString(), hexColor, label);
    }

    public PaletteValue(Color color, String label) {
        this(UUID.randomUUID().toString(), color, label);
    }

    public PaletteValue editorCopy() {
        return new PaletteValue(color, label + " copy");
    }

    public String exportInitString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PALETTEVALUE ");
        sb.append(ImEx.exportStringId(id));
        sb.append(" ");
        sb.append(ImEx.exportString(color));
        sb.append(" ");
        sb.append(ImEx.exportStringUser(label));
        return sb.toString();
    }

    public String exportString() {
        return id;
    }

    public String getCodeId() {
        return "pv_" + id.replace("-", "_");
    }

    public String exportInitCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        sb.append(getCodeId());
        sb.append(" = new PaletteValue(");
        sb.append(ImEx.exportCode(id));
        sb.append(", ");
        sb.append(ImEx.exportCode(color));
        sb.append(", ");
        sb.append(ImEx.exportCode(label));
        sb.append(");");
        return sb.toString();
    }

    public String exportCode() {
        return getCodeId();
    }
}

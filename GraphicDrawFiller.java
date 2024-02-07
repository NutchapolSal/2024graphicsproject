import java.awt.BasicStroke;
import java.awt.Graphics2D;

abstract class GraphicDrawFiller extends GraphicObject {
    public AnimBoolean stroke;
    public AnimColor strokeColor;
    public AnimInt thickness;
    public AnimBoolean fill;
    public AnimColor fillColor;

    protected GraphicDrawFiller(AnimBoolean stroke, AnimColor strokeColor, AnimInt thickness, AnimBoolean fill,
            AnimColor fillColor) {
        this.stroke = stroke;
        this.strokeColor = strokeColor;
        this.thickness = thickness;
        this.fill = fill;
        this.fillColor = fillColor;
    }

    protected boolean setupStroke(Graphics2D g, double time) {
        if (!stroke.get(time)) {
            return false;
        }
        g.setColor(strokeColor.get(time));
        g.setStroke(new BasicStroke(thickness.get(time)));
        return true;
    }

    protected boolean setupFill(Graphics2D g, double time) {
        if (!fill.get(time)) {
            return false;
        }
        g.setColor(fillColor.get(time));
        return true;
    }

    protected String exportParamString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.exportString(stroke));
        sb.append(" ");
        sb.append(ImEx.exportString(strokeColor));
        sb.append(" ");
        sb.append(ImEx.exportString(thickness));
        sb.append(" ");
        sb.append(ImEx.exportString(fill));
        sb.append(" ");
        sb.append(ImEx.exportString(fillColor));
        return sb.toString();
    }

    protected String exportParamCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.exportCode(stroke));
        sb.append(", ");
        sb.append(ImEx.exportCode(strokeColor));
        sb.append(", ");
        sb.append(ImEx.exportCode(thickness));
        sb.append(", ");
        sb.append(ImEx.exportCode(fill));
        sb.append(", ");
        sb.append(ImEx.exportCode(fillColor));
        return sb.toString();
    }
}
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
        g.setColor(strokeColor.get(time).get());
        g.setStroke(new BasicStroke(thickness.get(time)));
        return true;
    }

    protected boolean setupFill(Graphics2D g, double time) {
        if (!fill.get(time)) {
            return false;
        }
        g.setColor(fillColor.get(time).get());
        return true;
    }
}
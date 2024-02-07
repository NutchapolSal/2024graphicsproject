import java.awt.Graphics;

abstract class GraphicPlotter extends GraphicObject {
    public AnimColor color;

    protected GraphicPlotter(AnimColor color) {
        this.color = color;
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }

    protected String exportParamString() {
        return ImEx.exportString(color);
    }

    protected String exportParamCode() {
        return ImEx.exportCode(color);
    }
}
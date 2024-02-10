import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

class GraphicString extends GraphicObject {
    public AnimString text;
    public AnimString fontFace;
    public AnimInt fontSize;
    public AnimColor strokeColor;
    public AnimPoint position;
    public AnimInt alignment;

    private Font fontCache;
    private String lastFont = "";
    private int lastSize = -1;

    /** editor default */
    GraphicString(TimeKeypoint tkp) {
        this(new AnimString().add(tkp, "Hello, World!", EasingFunction.linear),
                new AnimString().add(tkp, "Arial", EasingFunction.linear),
                new AnimInt().add(tkp, 15, EasingFunction.linear),
                new AnimColor().add(tkp, "#000", EasingFunction.linear),
                new AnimPoint().add(tkp, new Point(0, 0), EasingFunction.linear),
                new AnimInt().add(tkp, 1, EasingFunction.linear));
    }

    public GraphicString(AnimString text, AnimString fontFace, AnimInt fontSize, AnimColor strokeColor,
            AnimPoint position, AnimInt alignment) {
        this.text = text;
        this.fontFace = fontFace;
        this.fontSize = fontSize;
        this.strokeColor = strokeColor;
        this.position = position;
        this.alignment = alignment;
    }

    private void updateFont(double time) {
        if (lastFont.equals(fontFace.get(time)) && lastSize == fontSize.get(time)) {
            return;
        }
        fontCache = new Font(fontFace.get(time), Font.PLAIN, fontSize.get(time));
        lastFont = fontFace.get(time);
        lastSize = fontSize.get(time);
    }

    @Override
    public void draw(Graphics g, double time) {
        Graphics g2 = g.create();
        updateFont(time);
        FontMetrics metrics = g2.getFontMetrics(fontCache);
        int width = metrics.stringWidth(text.get(time));

        int x = position.get(time).x;
        int y = position.get(time).y;

        switch (alignment.get(time)) {
        case -1:
            x -= width;
            break;

        case 0:
            x -= width / 2;
            break;

        case 1:
            break;
        }

        g2.setColor(strokeColor.get(time).get());
        g2.setFont(fontCache);
        g2.drawString(text.get(time), x, y);
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        var pos = this.position.get(time);
        var alignment = this.alignment.get(time);
        var lineoffset = 20;
        switch (alignment) {
        case -1:
            lineoffset = 20;
            break;
        case 0:
            lineoffset = 10;
            break;
        case 1:
            lineoffset = 0;
            break;
        }

        debugCircle(g, pos.x, pos.y, debugging == 1);
        debugLine(g, pos.x - lineoffset, pos.y, pos.x + 20 - lineoffset, pos.y, debugging == 2);
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("STRING ");
        sb.append(ImEx.exportString(text));
        sb.append(" ");
        sb.append(ImEx.exportString(fontFace));
        sb.append(" ");
        sb.append(ImEx.exportString(fontSize));
        sb.append(" ");
        sb.append(ImEx.exportString(strokeColor));
        sb.append(" ");
        sb.append(ImEx.exportString(position));
        sb.append(" ");
        sb.append(ImEx.exportString(alignment));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicString(");
        sb.append(ImEx.exportCode(text));
        sb.append(", ");
        sb.append(ImEx.exportCode(fontFace));
        sb.append(", ");
        sb.append(ImEx.exportCode(fontSize));
        sb.append(", ");
        sb.append(ImEx.exportCode(strokeColor));
        sb.append(", ");
        sb.append(ImEx.exportCode(position));
        sb.append(", ");
        sb.append(ImEx.exportCode(alignment));
        sb.append(")");
        return sb.toString();
    }
}
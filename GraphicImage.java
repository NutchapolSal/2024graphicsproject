import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

class GraphicImage extends GraphicObject {
    public MutableString filePath;
    private String lastFilePath = "";
    public BufferedImage image;
    public AnimPoint origin;
    public AnimDimension size;
    public AnimDouble opacity;

    GraphicImage(String filePath, AnimPoint origin, AnimDimension size, AnimDouble opacity) {
        this.filePath = new MutableString(filePath);
        this.origin = origin;
        this.size = size;
        this.opacity = opacity;
        updateImage();
    }

    private void updateImage() {
        if (lastFilePath.equals(filePath.value)) {
            return;
        }
        try {
            this.image = ImageIO.read(new File(this.filePath.value));
        } catch (Exception e) {
            this.image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            g.setColor(Color.red);
            g.fillRect(0, 0, 50, 50);
            g.setColor(Color.white);
            g.drawString("ERROR", 0, 10);
        }
        lastFilePath = filePath.value;
    }

    @Override
    public void draw(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        var origin = this.origin.get(time);
        var size = this.size.get(time);

        updateImage();

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                (float) (double) opacity.get(time));
        g.setComposite(alphaComposite);
        g.drawImage(image, origin.x, origin.y, size.width, size.height, null);
    }

    @Override
    public void debugDraw(Graphics g, double time) {
        var origin = this.origin.get(time);
        var size = this.size.get(time);
        debugLine(g, origin.x, origin.y, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugDot(g, origin.x + size.width, origin.y + size.height, debugging == 2);
        debugCircle(g, origin.x, origin.y, debugging == 1);
    }

    @Override
    public GraphicObject copy() {
        return new GraphicImage(filePath.value, origin, size, opacity);
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IMAGE ");
        sb.append(this.filePath.value);
        sb.append("\n");
        sb.append(ImEx.exportString(this.origin));
        sb.append(" ");
        sb.append(ImEx.exportString(this.size));
        sb.append(" ");
        sb.append(ImEx.exportString(this.opacity));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicImage(\"");
        sb.append(this.filePath.value);
        sb.append("\", ");
        sb.append(ImEx.exportCode(this.origin));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.size));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.opacity));
        sb.append(")");
        return sb.toString();
    }
}
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

class ColorButton extends JButton {
    static int squareSize = 10;
    private Color color;
    private boolean invalid = false;

    public ColorButton() {
        this.setText(" ");
    }

    public void setColor(Color color) {
        this.color = color;
        this.invalid = false;
        this.repaint();
    }

    public void setInvalid() {
        this.invalid = true;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (invalid) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.red);
            g.setFont(getFont().deriveFont(getHeight() * 0.8f));
            g.drawString("!!", (int) (getWidth() * 0.1), (int) (getHeight() * 0.9f));
            return;
        }
        for (int i = 0; i < getWidth(); i += squareSize) {
            for (int j = 0; j < getHeight(); j += squareSize) {
                g.setColor((i / squareSize + j / squareSize) % 2 == 0 ? Color.lightGray : Color.white);
                g.fillRect(i, j, squareSize, squareSize);
            }
        }
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(color.getRGB(), false));
        g.fillRect(0, 0, getWidth() / 3, getHeight());
    }
}
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

class GraphicsPanel extends JPanel {
    List<GraphicLayer> instructions;
    private double time = 0;

    GraphicsPanel(GraphicRoot root) {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        this.instructions = root.instructions;
        var timeCallback = root.subscribeToTime(t -> {
            time = t;
            repaint();
        });
        this.putClientProperty("timeCallback", timeCallback);

    }

    @Override
    protected void paintComponent(Graphics gOuter) {
        super.paintComponent(gOuter);
        var size = this.getSize();
        Graphics g = gOuter.create();
        g.translate(size.width / 2, size.height / 2);
        var debugG = (Graphics2D) gOuter.create();
        debugG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        debugG.translate(size.width / 2, size.height / 2);

        for (GraphicLayer layer : instructions) {
            if (layer.shown.get(time)) {
                layer.draw(g, time);
            }
        }

        for (int i = 0; i < 600; i++) {
            debugG.setColor(i % 2 == 0 ? Color.white : Color.black);
            debugG.fillRect(-301 + i, -305, 1, 5);
            debugG.fillRect(-305, -301 + i, 5, 1);
            debugG.fillRect(300 - i, 300, 1, 5);
            debugG.fillRect(300, 300 - i, 5, 1);
        }

        if (!GlobalState.pannerPanelDragging || GlobalState.pannerShowDebugging) {
            for (GraphicLayer layer : instructions) {
                layer.debugDraw(debugG, time);
            }
        }
    }

}
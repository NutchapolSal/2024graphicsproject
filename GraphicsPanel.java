import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

class GraphicsPanel extends JPanel {
    GraphicRoot root;
    private double time = 0;

    private Timer repainter = new Timer(1000 / 60, e -> {
        this.repaint();
    });

    GraphicsPanel(GraphicRoot root) {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        this.root = root;
        var timeCallback = root.subscribeToTime(t -> {
            time = t;
            repaint();
        });
        this.putClientProperty("timeCallback", timeCallback);

        var timeRunningCallback = root.subscribeToTimeRunning(r -> {
            if (!r) {
                repainter.start();
            } else {
                repainter.stop();
            }
        });
        this.putClientProperty("timeRunningCallback", timeRunningCallback);

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

        for (GraphicLayer layer : root.instructions) {
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
            for (GraphicLayer layer : root.instructions) {
                layer.debugDraw(debugG, time);
            }
        }
    }

}
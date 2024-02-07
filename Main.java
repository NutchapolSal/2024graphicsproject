import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;

public class Main {

    private static void runTests() {
        AnimTest.test();
        System.out.println();
    }

    public static void main(String[] args) {
        runTests();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("bg",
                new AnimBoolean().add(0, true, EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimDouble().add(0, 0.0, EasingFunction.snap))
                .add(new GraphicPath2D(
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimColor().add(0, "#FFFFFF", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#113", EasingFunction.easeInOutPower2)
                                .add(10, "#374A71", EasingFunction.easeInOutPower2),
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-325, -325), EasingFunction.snap),
                        new Path2DLine(new AnimPoint().add(0, new Point(325, -325), EasingFunction.snap)),
                        new Path2DLine(new AnimPoint().add(0, new Point(325, 325), EasingFunction.snap)),
                        new Path2DLine(new AnimPoint().add(0, new Point(-325, 325), EasingFunction.snap))))

        );
        instructions.add(new GraphicLayer("third point six swing",
                new AnimBoolean().add(0, true, EasingFunction.snap)
                        .add(6, false, EasingFunction.snap)
                        .add(10, true, EasingFunction.snap),
                new AnimPoint().add(0, new Point(), EasingFunction.easeInOutPower2)
                        .add(2, new Point(50, 50), EasingFunction.easeInOutPower2),
                new AnimPoint().add(0, new Point(), EasingFunction.snap),
                new AnimDouble().add(3, 0.0, EasingFunction.easeInOutPower3).add(5, 360.0, EasingFunction.linear))
                .add(new GraphicPath2D(
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#000000", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimBoolean().add(0, true, EasingFunction.snap),
                        new AnimColor().add(0, "#333333", EasingFunction.snap),
                        new AnimBoolean().add(0, false, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-200, -200), EasingFunction.snap),
                        new Path2DBezier(
                                new AnimPoint().add(0, new Point(-250, -170), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-150, -170), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-210, -140), EasingFunction.easeInOutPower2)),
                        new Path2DBezier(
                                new AnimPoint().add(0, new Point(-200, 0), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(-150, -250), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(0, -150), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(150, -250), EasingFunction.easeInOutPower2),
                                new AnimPoint().add(0, new Point(200, -150), EasingFunction.easeInOutPower2))))
                .add(new GraphicImage(
                        "null",
                        new AnimPoint().add(0, new Point(-290, -290), EasingFunction.snap),
                        new AnimDimension().add(0, new Dimension(20, 20), EasingFunction.snap),
                        new AnimDouble().add(0, 1.0, EasingFunction.snap)))
                .add(new GraphicCircle(
                        new AnimColor().add(0, "#2266AA", EasingFunction.snap),
                        new AnimInt().add(0, 1, EasingFunction.snap),
                        new AnimPoint().add(0, new Point(-220, -260), EasingFunction.snap),
                        new AnimInt().add(0, 20, EasingFunction.snap)))

        );

        var exx = ImEx.exportString(instructions);
        System.out.println(exx);

        System.out.println(ImEx.exportString(ImEx.importString(exx)));
        System.out.println(ImEx.exportCode(instructions));

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("😋");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new EditorFrame(frame, instructions);

        long startTime = System.nanoTime();
        new Timer(1000 / 60, e -> {
            long currTime = System.nanoTime();
            panel.time = (currTime - startTime) / 1_000_000_000.0;
            panel.repaint();
        }).start();

    }

}

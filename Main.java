import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;

public class Main {

    private static void runTests() {
        AnimTest.test();
        System.out.println("=====");
        ImEx.test();
        System.out.println();
    }

    public static void main(String[] args) {
        runTests();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GraphicRoot root = PreloadedData.create();

        GraphicsPanel panel = new GraphicsPanel(root.instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("😋");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new EditorGang(frame, root);

        long startTime = System.nanoTime();
        new Timer(1000 / 60, e -> {
            long currTime = System.nanoTime();
            panel.time = (currTime - startTime) / 1_000_000_000.0;
            panel.repaint();
        }).start();

    }

}

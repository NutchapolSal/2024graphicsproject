import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GraphicRoot root = PreloadedData.create();

        GraphicsPanel panel = new GraphicsPanel(root);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.add(panel);
            frame.setTitle("😋");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            new EditorGang(frame, root);
        });
    }

}

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

enum EditorColor {
    /** returns default background color for the component */
    Static(null), Animated("#0f0"), Timepoint("#ff0"), Invalid("#f00"), Temporary("#f70");

    private String color;
    private static Map<Class<? extends Component>, String> defaultColors = new HashMap<>();

    EditorColor(String color) {
        this.color = color;
    }

    public Color color() {
        if (color == null) {
            return null;
        }
        return ColorHexer.decode(color);
    }

    /**
     * @param component object with desired class
     */
    public static void registerDefaultColor(Component component) {
        defaultColors.computeIfAbsent(component.getClass(), k -> {
            try {
                return ColorHexer.encode(component.getClass().getDeclaredConstructor().newInstance().getBackground());
            } catch (Exception e) {
                e.printStackTrace();
                return "#fff";
            }
        });
    }

    public Color color(Component component) {
        registerDefaultColor(component);
        if (color == null) {
            return ColorHexer.decode(defaultColors.getOrDefault(component.getClass(), "#fff"));
        }

        return ColorHexer.decode(color);
    }
}

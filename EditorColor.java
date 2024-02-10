import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

enum EditorColor {
    /** static returns default background color for the component */
    Static(null),

    Invalid("#f00");

    private Color color;
    private static Map<Class<? extends Component>, Color> defaultColors = new HashMap<>();

    EditorColor(String color) {
        if (color == null) {
            this.color = null;
            return;
        }
        this.color = ColorHexer.decode(color);
    }

    EditorColor(String colorA, double frac, String colorB) {
        this.color = Lerp.run(ColorHexer.decode(colorA), frac, ColorHexer.decode(colorB));
    }

    public Color color() {
        if (color == null) {
            return null;
        }
        return color;
    }

    /**
     * @param component object with desired class
     */
    public static void registerDefaultColor(Component component) {
        defaultColors.computeIfAbsent(component.getClass(), k -> {
            try {
                return component.getClass().getDeclaredConstructor().newInstance().getBackground();
            } catch (Exception e) {
                e.printStackTrace();
                return ColorHexer.decode("#fff");
            }
        });
    }

    public Color color(Component component) {
        registerDefaultColor(component);
        if (color == null) {
            return defaultColors.getOrDefault(component.getClass(), ColorHexer.decode("#fff"));
        }

        return color;
    }
}

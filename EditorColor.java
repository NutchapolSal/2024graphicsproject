import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

enum EditorColor {
    /** static returns default background color for the component */
    Static(null),

    Animated("#0f0"),

    Timepoint("#ff0"),

    MultiTimepoint("#ff0", 0.4, "#000"),

    SelectedAnimated("#0af"),

    SelectedTimepoint("#0ff"),

    SelectedMultiTimepoint("#0ff", 0.4, "#000"),

    HereTimepoint("#f7f"),

    HereMultiTimepoint("#f7f", 0.4, "#000"),

    Invalid("#f00"),

    Temporary("#f70");

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

    public static Color getTimepointTypeColor(int countTPAtAnimValTime, boolean timelineHasTP,
            boolean editorOnFocusTP) {
        if (editorOnFocusTP && timelineHasTP) {
            return countTPAtAnimValTime <= 1 ? EditorColor.HereTimepoint.color()
                    : EditorColor.HereMultiTimepoint.color();
        }
        if (timelineHasTP) {
            switch (countTPAtAnimValTime) {
            case 0:
                return EditorColor.SelectedAnimated.color();
            case 1:
                return EditorColor.SelectedTimepoint.color();
            default:
                return EditorColor.SelectedMultiTimepoint.color();
            }
        } else {
            switch (countTPAtAnimValTime) {
            case 0:
                return EditorColor.Animated.color();
            case 1:
                return EditorColor.Timepoint.color();
            default:
                return EditorColor.MultiTimepoint.color();
            }
        }
    }
}

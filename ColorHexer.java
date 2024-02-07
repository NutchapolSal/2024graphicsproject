import java.awt.Color;
import java.util.Optional;

class ColorHexer {

    public static Color decode(String hex) {
        return decodeOptional(hex).orElse(Color.black);
    }

    // formats:
    // #RGB
    // #RGBA
    // #RRGGBB
    // #RRGGBBAA
    public static Optional<Color> decodeOptional(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        try {
            Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (hex.length() == 3) {
            return Optional.of(Color.decode("#" +
                    hex.substring(0, 1).repeat(2) +
                    hex.substring(1, 2).repeat(2) +
                    hex.substring(2, 3).repeat(2)));
        } else if (hex.length() == 4) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 1), 16) * 17,
                    Integer.parseInt(hex.substring(1, 2), 16) * 17,
                    Integer.parseInt(hex.substring(2, 3), 16) * 17,
                    Integer.parseInt(hex.substring(3, 4), 16) * 17));
        } else if (hex.length() == 6) {
            return Optional.of(Color.decode("#" + hex));
        } else if (hex.length() == 8) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16),
                    Integer.parseInt(hex.substring(6, 8), 16)));
        } else {
            return Optional.empty();
        }
    }

    public static String encode(Color c) {
        if (c.getAlpha() == 255) {
            return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        } else {
            return String.format("#%02x%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
    }
}
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

enum EasingFunction implements DoubleUnaryOperator {

    linear(x -> x), snap(x -> x < 1 ? 0 : 1), easeInSine(x -> 1 - Math.cos((x * Math.PI) / 2.0)),
    easeOutSine(x -> Math.sin((x * Math.PI) / 2.0)), easeInOutSine(x -> -(Math.cos(Math.PI * x) - 1) / 2),
    /** quad */
    easeInPower2(constructEaseInPower(2)),
    /** quad */
    easeOutPower2(constructEaseOutPower(2)),
    /** quad */
    easeInOutPower2(constructEaseInOutPower(2)),
    /** cubic */
    easeInPower3(constructEaseInPower(3)),
    /** cubic */
    easeOutPower3(constructEaseOutPower(3)),
    /** cubic */
    easeInOutPower3(constructEaseInOutPower(3)),
    /** quart */
    easeInPower4(constructEaseInPower(4)),
    /** quart */
    easeOutPower4(constructEaseOutPower(4)),
    /** quart */
    easeInOutPower4(constructEaseInOutPower(4)),
    /** quint */
    easeInPower5(constructEaseInPower(5)),
    /** quint */
    easeOutPower5(constructEaseOutPower(5)),
    /** quint */
    easeInOutPower5(constructEaseInOutPower(5)), easeInExpo(x -> x <= 0 ? 0 : Math.pow(2, 10 * x - 10)),
    easeOutExpo(x -> 1.0 <= x ? 1.0 : 1 - Math.pow(2, -10 * x)), easeInOutExpo(x -> {
        if (x <= 0) {
            return 0;
        }
        if (1 <= x) {
            return 1;
        }
        return x < 0.5 ? Math.pow(2, 20 * x - 10) / 2 : (2 - Math.pow(2, -20 * x + 10)) / 2;
    }),

    ;

    private static DoubleUnaryOperator constructEaseInPower(double power) {
        return x -> Math.pow(x, power);
    }

    private static DoubleUnaryOperator constructEaseOutPower(double power) {
        return x -> 1 - Math.pow((1 - x), power);
    }

    private static DoubleUnaryOperator constructEaseInOutPower(double power) {
        return x -> x < 0.5 ? Math.pow(2, power - 1) * Math.pow(x, power) : 1 - Math.pow(-2 * x + 2, power) / 2;
    }

    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGHT = 20;
    private static final double ICON_CALC_STEPS_FACTOR = 2.0;

    private final DoubleUnaryOperator easing;
    private Image icon;

    EasingFunction(DoubleUnaryOperator easing) {
        this.easing = easing;
    }

    private void generateIcon() {
        this.icon = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        var g = (Graphics2D) icon.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(0, ICON_HEIGHT - 1);
        g.scale(1, -1);
        g.setColor(Color.gray);
        g.drawLine(0, 0, 0, ICON_HEIGHT);
        g.drawLine(0, 0, ICON_WIDTH, 0);
        g.setColor(Color.orange);
        Path2D path = new Path2D.Double();
        path.moveTo(0, 0);
        for (int x = 0; x < ICON_WIDTH * ICON_CALC_STEPS_FACTOR; x++) {
            double time = x / (ICON_WIDTH * ICON_CALC_STEPS_FACTOR - 1.0);
            double y = easing.applyAsDouble(time);
            path.lineTo(x / ICON_CALC_STEPS_FACTOR, (y * (ICON_HEIGHT - 1)));
        }
        g.draw(path);
    }

    private static EasingFunction[] nonEases = Arrays.stream(EasingFunction.values())
            .filter(easing -> !easing.name().startsWith("ease")).toArray(EasingFunction[]::new);
    private static EasingFunction[] easeIns = Arrays.stream(EasingFunction.values())
            .filter(easing -> easing.name().startsWith("easeIn") && !easing.name().startsWith("easeInOut"))
            .toArray(EasingFunction[]::new);
    private static EasingFunction[] easeOuts = Arrays.stream(EasingFunction.values())
            .filter(easing -> easing.name().startsWith("easeOut")).toArray(EasingFunction[]::new);
    private static EasingFunction[] easeInOuts = Arrays.stream(EasingFunction.values())
            .filter(easing -> easing.name().startsWith("easeInOut")).toArray(EasingFunction[]::new);

    public static EasingFunction[] nonEases() {
        return nonEases.clone();
    }

    public static EasingFunction[] easeIns() {
        return easeIns.clone();
    }

    public static EasingFunction[] easeOuts() {
        return easeOuts.clone();
    }

    public static EasingFunction[] easeInOuts() {
        return easeInOuts.clone();
    }

    public Image icon() {
        if (icon == null) {
            generateIcon();
        }
        return icon;
    }

    public double applyAsDouble(double x) {
        return easing.applyAsDouble(x);
    }

}
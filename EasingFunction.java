import java.util.function.DoubleUnaryOperator;

enum EasingFunction implements DoubleUnaryOperator {

    linear(x -> x),
    easeOutSine(x -> Math.sin((x * Math.PI) / 2.0)),
    easeInSine(x -> 1 - Math.cos((x * Math.PI) / 2.0)),
    easeInOutSine(x -> -(Math.cos(Math.PI * x) - 1) / 2),
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
    easeInOutPower5(constructEaseInOutPower(5)),
    easeInExpo(x -> x <= 0 ? 0 : Math.pow(2, 10 * x - 10)),
    easeOutExpo(x -> 1.0 <= x ? 1.0 : 1 - Math.pow(2, -10 * x)),
    easeInOutExpo(
            x -> {
                if (x <= 0) {
                    return 0;
                }
                if (1 <= x) {
                    return 1;
                }
                return x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                        : (2 - Math.pow(2, -20 * x + 10)) / 2;
            }),

    snap(x -> x < 1 ? 0 : 1);

    private static DoubleUnaryOperator constructEaseInPower(double power) {
        return x -> Math.pow(x, power);
    }

    private static DoubleUnaryOperator constructEaseOutPower(double power) {
        return x -> 1 - Math.pow((1 - x), power);
    }

    private static DoubleUnaryOperator constructEaseInOutPower(double power) {
        return x -> x < 0.5 ? Math.pow(2, power - 1) * Math.pow(x, power) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

    private final DoubleUnaryOperator easing;

    EasingFunction(DoubleUnaryOperator easing) {
        this.easing = easing;
    }

    public double applyAsDouble(double x) {
        return easing.applyAsDouble(x);
    }

}
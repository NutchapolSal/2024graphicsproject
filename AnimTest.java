
class AnimTest extends AnimatedValue {
    public static void test() {
        System.out.println("AnimatedValue test");
        var animvalue = new AnimTest();
        System.out.println(animvalue.getValue(0.5)); // 0, 0.0
        System.out.println("---");
        animvalue.addTimepoint(0, EasingFunction.linear);
        System.out.println(animvalue.getValue(-1)); // 0, 0.0
        System.out.println(animvalue.getValue(0)); // 0, 0.0
        System.out.println(animvalue.getValue(1)); // 0, 0.0
        System.out.println("---");
        animvalue.addTimepoint(1, EasingFunction.linear);
        System.out.println(animvalue.getValue(0)); // 0, 0.0
        System.out.println(animvalue.getValue(0.33)); // 0, ~0.33
        System.out.println(animvalue.getValue(0.66)); // 0, ~0.66
        System.out.println(animvalue.getValue(1)); // 1, 0.0
        System.out.println(animvalue.getValue(1.33)); // 1, 0.0
        System.out.println("---");
        animvalue.addTimepoint(3, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 1, 0.0
        System.out.println(animvalue.getValue(1.66)); // 1, ~0.33
        System.out.println(animvalue.getValue(2.33)); // 1, ~0.66
        System.out.println(animvalue.getValue(3)); // 2, 0.0
        System.out.println(animvalue.getValue(4)); // 2, 0.0
        System.out.println("---");
        animvalue = new AnimTest();
        animvalue.addTimepoint(0, EasingFunction.linear);
        animvalue.addTimepoint(3, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 0, ~0.33
        animvalue.addTimepoint(1, EasingFunction.linear);
        animvalue.addTimepoint(1, EasingFunction.linear);
        animvalue.addTimepoint(1, EasingFunction.linear);
        System.out.println(animvalue.getValue(1)); // 1, 0.0
    }

    public String exportString() {
        return "";
    }

    public String exportCode() {
        return "";
    }
}
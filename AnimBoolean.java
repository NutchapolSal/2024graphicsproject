import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

class AnimBoolean extends AnimatedValue {
    public List<Boolean> valuepoints = new ArrayList<>();

    public AnimBoolean add(double time, boolean value, DoubleUnaryOperator easingToNext) {
        var i = super.addTimepoint(time, EasingFunction.snap);
        if (i == -1) {
            return this;
        }
        valuepoints.add(i, value);
        return this;
    }

    public AnimBoolean remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public boolean get(double time) {
        if (timepoints.isEmpty()) {
            return false;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimBoolean copy() {
        var anim = new AnimBoolean();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = valuepoints.get(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimBoolean", strings);
    }
}
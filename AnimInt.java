import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnimInt extends AnimatedValue {
    public List<Integer> valuepoints = new ArrayList<>();

    public AnimInt add(double time, int value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimInt remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public int get(double time) {
        if (timepoints.isEmpty()) {
            return 0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimInt copy() {
        var anim = new AnimInt();
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
        return super.exportCode("AnimInt", strings);
    }
}
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AnimDimension extends AnimatedValue {
    public List<Dimension> valuepoints = new ArrayList<>();

    public AnimDimension add(double time, Dimension value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimDimension remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Dimension get(double time) {
        if (timepoints.isEmpty()) {
            return new Dimension(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return new Dimension(
                (int) Lerp.run(valuepoints.get(stepValue.index).width, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).width),
                (int) Lerp.run(valuepoints.get(stepValue.index).height, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).height));
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimDimension", strings);
    }
}
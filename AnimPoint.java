import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AnimPoint extends AnimatedValue {
    public List<Point> valuepoints = new ArrayList<>();

    public AnimPoint add(double time, Point value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimPoint remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Point get(double time) {
        if (timepoints.isEmpty()) {
            return new Point(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return new Point(
                Lerp.run(valuepoints.get(stepValue.index).x, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).x),
                Lerp.run(valuepoints.get(stepValue.index).y, stepValue.frac,
                        valuepoints.get(stepValue.index + 1).y));
    }

    public String exportString() {
        var strings = valuepoints.stream().map(ImEx::exportString).collect(Collectors.toList());
        return super.exportString(strings);
    }

    public String exportCode() {
        var strings = valuepoints.stream().map(ImEx::exportCode).collect(Collectors.toList());
        return super.exportCode("AnimPoint", strings);
    }
}
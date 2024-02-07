import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AnimColor extends AnimatedValue {
    public List<Color> valuepoints = new ArrayList<>();

    public AnimColor add(double time, String value, EasingFunction easingToNext) {
        return add(time, ColorHexer.decode(value), easingToNext);
    }

    public AnimColor add(double time, Color value, EasingFunction easingToNext) {
        var i = super.addTimepoint(time, easingToNext);
        if (i == -1) {
            return this;
        }
        valuepoints.add(value);
        return this;
    }

    public AnimColor remove(double time) {
        var stepValue = getValue(time);
        valuepoints.remove(stepValue.index);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Color get(double time) {
        if (timepoints.isEmpty()) {
            return Color.black;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return valuepoints.get(stepValue.index);
        }

        return Lerp.run(valuepoints.get(stepValue.index), stepValue.frac, valuepoints.get(stepValue.index + 1));
    }

    public AnimColor copy() {
        var anim = new AnimColor();
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
        return super.exportCode("AnimColor", strings);
    }
}
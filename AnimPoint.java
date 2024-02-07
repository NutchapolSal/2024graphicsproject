import java.awt.Point;

class AnimPoint extends AnimatedValue<Point> {
    public AnimPoint add(double time, Point value, EasingFunction easingToNext) {
        super.addTimepoint(time, value, easingToNext);
        return this;
    }

    public AnimPoint remove(double time) {
        var stepValue = getValue(time);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Point get(double time) {
        if (timepoints.isEmpty()) {
            return new Point(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(0);
        }

        return new Point(
                Lerp.run(this.getIndex(stepValue.index).x, stepValue.frac, this.getIndex(stepValue.index + 1).x),
                Lerp.run(this.getIndex(stepValue.index).y, stepValue.frac, this.getIndex(stepValue.index + 1).y));
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimPoint", v -> ImEx.exportCode(v));
    }
}
import java.awt.Point;

class AnimPoint extends AnimatedValue<Point> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimPoint add(TimeKeypoint tkp, Point value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, easingToNext);
        return this;
    }

    public AnimPoint remove(TimeKeypoint tkp) {
        super.removeTimepoint(tkp);
        return this;
    }

    public Point get(double time) {
        if (timepoints.isEmpty()) {
            return new Point(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
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

    @Override
    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        super.setEasingFunction(tkp, easing);
    }

    @Override
    public AnimatedValue<Point> addForEditor(TimeKeypoint tkp, Point value, EasingFunction easingToNext) {
        return add(tkp, value, easingToNext);
    }
}

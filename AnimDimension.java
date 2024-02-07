import java.awt.Dimension;

class AnimDimension extends AnimatedValue<Dimension> {

    public AnimDimension add(double time, Dimension value, EasingFunction easingToNext) {
        super.addTimepoint(time, value, easingToNext);
        return this;
    }

    public AnimDimension remove(double time) {
        var stepValue = getValue(time);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Dimension get(double time) {
        if (timepoints.isEmpty()) {
            return new Dimension(0, 0);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return new Dimension(
                (int) Lerp.run(this.getIndex(stepValue.index).width, stepValue.frac,
                        this.getIndex(stepValue.index + 1).width),
                (int) Lerp.run(this.getIndex(stepValue.index).height, stepValue.frac,
                        this.getIndex(stepValue.index + 1).height));
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimDimension", v -> ImEx.exportCode(v));
    }
}
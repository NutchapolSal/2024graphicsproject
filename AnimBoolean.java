import java.util.function.DoubleUnaryOperator;

class AnimBoolean extends AnimatedValue<Boolean> {
    public AnimBoolean add(double time, boolean value, DoubleUnaryOperator easingToNext) {
        super.addTimepoint(time, value, EasingFunction.snap);
        return this;
    }

    public AnimBoolean remove(double time) {
        var stepValue = getValue(time);
        timepoints.remove(stepValue.index);
        return this;
    }

    public boolean get(double time) {
        if (timepoints.isEmpty()) {
            return false;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return Lerp.run(this.getIndex(stepValue.index), stepValue.frac, this.getIndex(stepValue.index + 1));
    }

    public AnimBoolean copy() {
        var anim = new AnimBoolean();
        for (Timepoint tp : timepoints) {
            anim.add(tp.time, tp.value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimBoolean", v -> ImEx.exportCode(v));
    }
}
public class AnimInt extends AnimatedValue<Integer> {

    public AnimInt add(double time, int value, EasingFunction easingToNext) {
        super.addTimepoint(time, value, easingToNext);
        return this;
    }

    public AnimInt remove(double time) {
        var stepValue = getValue(time);
        timepoints.remove(stepValue.index);
        return this;
    }

    public int get(double time) {
        if (timepoints.isEmpty()) {
            return 0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return Lerp.run(this.getIndex(stepValue.index), stepValue.frac, this.getIndex(stepValue.index + 1));
    }

    public AnimInt copy() {
        var anim = new AnimInt();
        for (Timepoint tp : timepoints) {
            anim.add(tp.time, tp.value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimInt", v -> ImEx.exportCode(v));
    }
}
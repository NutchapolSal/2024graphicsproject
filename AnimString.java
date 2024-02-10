class AnimString extends AnimatedValue<String> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimString add(TimeKeypoint tkp, String value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, EasingFunction.snap);
        return this;
    }

    public AnimString remove(TimeKeypoint tkp) {
        super.removeTimepoint(tkp);
        return this;
    }

    public String get(double time) {
        if (timepoints.isEmpty()) {
            return "";
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return Lerp.run(this.getIndex(stepValue.index), stepValue.frac, this.getIndex(stepValue.index + 1));
    }

    public AnimString copy() {
        var anim = new AnimString();
        for (Timepoint tp : timepoints) {
            anim.add(tp.tkp, tp.value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportStringUser(v));
    }

    public String exportCode() {
        return super.exportCode("AnimString", v -> ImEx.exportCode(v));
    }

    @Override
    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        super.setEasingFunction(tkp, EasingFunction.snap);
    }

    @Override
    public AnimatedValue<String> addForEditor(TimeKeypoint tkp, String value, EasingFunction easingToNext) {
        return add(tkp, value, easingToNext);
    }
}
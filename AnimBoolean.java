class AnimBoolean extends AnimatedValue<Boolean> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimBoolean add(TimeKeypoint tkp, boolean value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, EasingFunction.snap);
        return this;
    }

    public AnimBoolean remove(TimeKeypoint tkp) {
        super.removeTimepoint(tkp);
        return this;
    }

    public Boolean get(double time) {
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
            anim.add(tp.tkp, tp.value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimBoolean", v -> ImEx.exportCode(v));
    }

    @Override
    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        super.setEasingFunction(tkp, EasingFunction.snap);
    }

    @Override
    public AnimatedValue<Boolean> addForEditor(TimeKeypoint tkp, Boolean value, EasingFunction easingToNext) {
        return add(tkp, value, easingToNext);
    }
}
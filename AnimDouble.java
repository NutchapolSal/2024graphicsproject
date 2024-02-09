class AnimDouble extends AnimatedValue<Double> {

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimDouble add(TimeKeypoint tkp, double value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, easingToNext);
        return this;
    }

    public AnimDouble remove(TimeKeypoint tkp) {
        super.removeTimepoint(tkp);
        return this;
    }

    public Double get(double time) {
        if (timepoints.isEmpty()) {
            return 0.0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return Lerp.run(this.getIndex(stepValue.index), stepValue.frac, this.getIndex(stepValue.index + 1));
    }

    public AnimDouble copy() {
        var anim = new AnimDouble();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = this.getIndex(i);
            anim.add(tp.tkp, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> ImEx.exportString(v));
    }

    public String exportCode() {
        return super.exportCode("AnimDouble", v -> ImEx.exportCode(v));
    }

    @Override
    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        super.setEasingFunction(tkp, EasingFunction.snap);
    }

    @Override
    public AnimatedValue<Double> addForEditor(TimeKeypoint tkp, Double value, EasingFunction easingToNext) {
        return add(tkp, value, easingToNext);
    }
}
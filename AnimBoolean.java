class AnimBoolean extends AnimatedValue<Boolean> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimBoolean add(TimeKeypoint tkp, boolean value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, EasingFunction.snap);
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

}
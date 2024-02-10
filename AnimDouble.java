class AnimDouble extends AnimatedValue<Double> {

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimDouble add(TimeKeypoint tkp, double value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, easingToNext);
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

}

class AnimString extends AnimatedValue<String> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimString add(TimeKeypoint tkp, String value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, EasingFunction.snap);
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

}
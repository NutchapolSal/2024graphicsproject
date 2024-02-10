class AnimInt extends AnimatedValue<Integer> {

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimInt add(TimeKeypoint tkp, int value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, easingToNext);
        return this;
    }

    public Integer get(double time) {
        if (timepoints.isEmpty()) {
            return 0;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return Lerp.run(this.getIndex(stepValue.index), stepValue.frac, this.getIndex(stepValue.index + 1));
    }

}
import java.awt.Color;

class AnimColor extends AnimatedValue<MaybePaletteValue> {
    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimColor add(TimeKeypoint tkp, String value, EasingFunction easingToNext) {
        return add(tkp, ColorHexer.decode(value), easingToNext);
    }

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimColor add(TimeKeypoint tkp, Color value, EasingFunction easingToNext) {
        return add(tkp, new MaybePaletteValue(value), easingToNext);
    }

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    public AnimColor add(TimeKeypoint tkp, PaletteValue value, EasingFunction easingToNext) {
        return add(tkp, new MaybePaletteValue(value), easingToNext);
    }

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    private AnimColor add(TimeKeypoint tkp, MaybePaletteValue value, EasingFunction easingToNext) {
        super.addTimepoint(tkp, value, easingToNext);
        return this;
    }

    public AnimColor remove(TimeKeypoint tkp) {
        super.removeTimepoint(tkp);
        return this;
    }

    public MaybePaletteValue get(double time) {
        if (timepoints.isEmpty()) {
            return new MaybePaletteValue(Color.black);
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index);
        }

        return new MaybePaletteValue(Lerp.run(this.getIndex(stepValue.index).get(), stepValue.frac,
                this.getIndex(stepValue.index + 1).get()));
    }

    public AnimColor copy() {
        var anim = new AnimColor();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = this.getIndex(i);
            anim.add(tp.tkp, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> v.exportString());
    }

    public String exportCode() {
        return super.exportCode("AnimColor", v -> v.exportCode());
    }

    @Override
    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        super.setEasingFunction(tkp, EasingFunction.snap);
    }

    @Override
    public AnimatedValue<MaybePaletteValue> addForEditor(TimeKeypoint tkp, MaybePaletteValue value,
            EasingFunction easingToNext) {
        return add(tkp, value, easingToNext);
    }
}
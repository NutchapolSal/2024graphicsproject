import java.awt.Color;

class AnimColor extends AnimatedValue<AnimColor.MaybePaletteValue> {
    static class MaybePaletteValue implements Exportable {
        public final boolean isPaletteValue;
        public final PaletteValue paletteValue;
        public final Color color;

        public MaybePaletteValue(PaletteValue paletteValue) {
            this.isPaletteValue = true;
            this.paletteValue = paletteValue;
            this.color = null;
        }

        public MaybePaletteValue(Color color) {
            this.isPaletteValue = false;
            this.paletteValue = null;
            this.color = color;
        }

        public Color get() {
            if (isPaletteValue) {
                return paletteValue.color;
            }
            return color;
        }

        public String exportString() {
            if (isPaletteValue) {
                return "PALETTEVALUE " + ImEx.exportString(paletteValue);
            }
            return "COLOR " + ImEx.exportString(color);
        }

        public String exportCode() {
            if (isPaletteValue) {
                return ImEx.exportCode(paletteValue);
            }
            return ImEx.exportCode(color);
        }
    }

    public AnimColor add(double time, String value, EasingFunction easingToNext) {
        return add(time, ColorHexer.decode(value), easingToNext);
    }

    public AnimColor add(double time, Color value, EasingFunction easingToNext) {
        return add(time, new MaybePaletteValue(value), easingToNext);
    }

    public AnimColor add(double time, PaletteValue value, EasingFunction easingToNext) {
        return add(time, new MaybePaletteValue(value), easingToNext);
    }

    private AnimColor add(double time, MaybePaletteValue value, EasingFunction easingToNext) {
        super.addTimepoint(time, value, easingToNext);
        return this;
    }

    public AnimColor remove(double time) {
        var stepValue = getValue(time);
        timepoints.remove(stepValue.index);
        return this;
    }

    public Color get(double time) {
        if (timepoints.isEmpty()) {
            return Color.black;
        }
        var stepValue = getValue(time);
        if (stepValue.frac == 0) {
            return this.getIndex(stepValue.index).get();
        }

        return Lerp.run(this.getIndex(stepValue.index).get(), stepValue.frac, this.getIndex(stepValue.index + 1).get());
    }

    public AnimColor copy() {
        var anim = new AnimColor();
        for (int i = 0; i < timepoints.size(); i++) {
            var tp = timepoints.get(i);
            var value = this.getIndex(i);
            anim.add(tp.time, value, tp.easingToNext);
        }
        return anim;
    }

    public String exportString() {
        return super.exportString(v -> v.exportString());
    }

    public String exportCode() {
        return super.exportCode("AnimColor", v -> v.exportCode());
    }
}
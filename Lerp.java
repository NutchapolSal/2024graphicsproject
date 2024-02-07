import java.awt.Color;

class Lerp {
    public static boolean run(boolean before, double frac, boolean after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return before;
    }

    public static double run(double before, double frac, double after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return before + (after - before) * frac;
    }

    public static int run(int before, double frac, int after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return (int) (before + (after - before) * frac);
    }

    public static float run(float before, double frac, float after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        return (float) (before + (after - before) * frac);
    }

    // BRACE YOURSELF FOR COLOR PROCESSING

    // https://bottosson.github.io/posts/colorwrong/#what-can-we-do%3F
    // https://bottosson.github.io/posts/oklab/
    private static class LinearSRGB {
        public float r;
        public float g;
        public float b;

        public LinearSRGB(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        private static float linearToSRGB(float c) {
            if (c >= 0.0031308) {
                return (1.055f) * (float) Math.pow(c, 1.0f / 2.4f) - 0.055f;
            } else {
                return 12.92f * c;
            }

        }

        private static float sRGBToLinear(float c) {
            if (c >= 0.04045) {
                return (float) Math.pow(((c + 0.055) / (1 + 0.055)), 2.4);
            } else {
                return c / 12.92f;
            }
        }

        private static float clamp(float c) {
            return Math.min(Math.max(0, c), 1);
        }

        public LinearSRGB(Color c) {
            this.r = sRGBToLinear(c.getRed() / 255.0f);
            this.g = sRGBToLinear(c.getGreen() / 255.0f);
            this.b = sRGBToLinear(c.getBlue() / 255.0f);
        }

        public LinearSRGB(OkLab c) {
            float l_ = c.L + 0.3963377774f * c.a + 0.2158037573f * c.b;
            float m_ = c.L - 0.1055613458f * c.a - 0.0638541728f * c.b;
            float s_ = c.L - 0.0894841775f * c.a - 1.2914855480f * c.b;

            float l = l_ * l_ * l_;
            float m = m_ * m_ * m_;
            float s = s_ * s_ * s_;

            this.r = +4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s;
            this.g = -1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s;
            this.b = -0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s;
        }

        public Color toColor() {
            return new Color(
                    clamp(linearToSRGB(r)),
                    clamp(linearToSRGB(g)),
                    clamp(linearToSRGB(b)));
        }
    }

    private static class OkLab {
        public float L;
        public float a;
        public float b;

        public OkLab(float L, float a, float b) {
            this.L = L;
            this.a = a;
            this.b = b;
        }

        public OkLab(LinearSRGB c) {
            float l = 0.4122214708f * c.r + 0.5363325363f * c.g + 0.0514459929f * c.b;
            float m = 0.2119034982f * c.r + 0.6806995451f * c.g + 0.1073969566f * c.b;
            float s = 0.0883024619f * c.r + 0.2817188376f * c.g + 0.6299787005f * c.b;

            float l_ = (float) Math.cbrt(l);
            float m_ = (float) Math.cbrt(m);
            float s_ = (float) Math.cbrt(s);

            this.L = 0.2104542553f * l_ + 0.7936177850f * m_ - 0.0040720468f * s_;
            this.a = 1.9779984951f * l_ - 2.4285922050f * m_ + 0.4505937099f * s_;
            this.b = 0.0259040371f * l_ + 0.7827717662f * m_ - 0.8086757660f * s_;
        }

        public OkLab multiplyAlpha(float alpha) {
            this.L *= alpha;
            this.a *= alpha;
            this.b *= alpha;
            return this;
        }

        public OkLab divideAlpha(float alpha) {
            if (alpha == 0) {
                this.L = 0;
                this.a = 0;
                this.b = 0;
                return this;
            }
            this.L /= alpha;
            this.a /= alpha;
            this.b /= alpha;
            return this;
        }
    }

    /**
     * lerps in OkLab color space + correct handling of alpha
     */
    public static Color run(Color before, double frac, Color after) {
        if (frac <= 0) {
            return before;
        }
        if (frac >= 1) {
            return after;
        }

        var beforeOk = new OkLab(new LinearSRGB(before)).multiplyAlpha(before.getAlpha() / 255.0f);
        var afterOk = new OkLab(new LinearSRGB(after)).multiplyAlpha(after.getAlpha() / 255.0f);
        var lerpAlpha = run(before.getAlpha(), frac, after.getAlpha());
        var lerpOk = new OkLab(
                Lerp.run(beforeOk.L, frac, afterOk.L),
                Lerp.run(beforeOk.a, frac, afterOk.a),
                Lerp.run(beforeOk.b, frac, afterOk.b))
                .divideAlpha(lerpAlpha / 255.0f);
        var lerp = new LinearSRGB(lerpOk).toColor();

        return new Color(lerp.getRed(), lerp.getGreen(), lerp.getBlue(),
                lerpAlpha);

    }
}
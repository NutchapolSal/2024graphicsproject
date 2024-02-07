import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AnimatedValue<T> implements Exportable {
    class Timepoint {
        public TimeKeypoint tkp;
        public T value;
        public EasingFunction easingToNext;

        Timepoint(TimeKeypoint tkp, T value, EasingFunction easingToNext) {
            this.tkp = tkp;
            this.value = value;
            this.easingToNext = easingToNext;
        }

    }

    static class StepValue {
        public int index;
        public double frac;

        StepValue(int index, double frac) {
            this.index = index;
            this.frac = frac;
        }

        @Override
        public String toString() {
            return "StepValue [index=" + index + ", frac=" + frac + "]";
        }
    }

    public List<Timepoint> timepoints = new ArrayList<>();

    protected void sortTimepoints() {
        Collections.sort(timepoints, (tp1, tp2) -> Double.compare(tp1.tkp.time(), tp2.tkp.time()));
    }

    protected void addTimepoint(TimeKeypoint tkp, T value, EasingFunction easingToNext) {
        timepoints.add(new Timepoint(tkp, value, easingToNext));
        sortTimepoints();
    }

    protected StepValue getValue(double time) {
        int iBeforeTime = -1; // index of timepoint before or equal time
        for (Timepoint timepoint : timepoints) {
            if (timepoint.tkp.time() <= time) {
                iBeforeTime++;
            } else {
                break;
            }
        }

        if (iBeforeTime == -1) {
            return new StepValue(0, 0);
        }
        if (iBeforeTime == timepoints.size() - 1) {
            return new StepValue(iBeforeTime, 0);
        }

        double timeBefore = timepoints.get(iBeforeTime).tkp.time();
        double timeAfter = timepoints.get(iBeforeTime + 1).tkp.time();
        double frac = (time - timeBefore) / (timeAfter - timeBefore);
        return new StepValue(iBeforeTime, timepoints.get(iBeforeTime).easingToNext.applyAsDouble(frac));
    }

    protected T getIndex(int index) {
        return timepoints.get(index).value;
    }

    protected String exportString(Function<T, String> exporter) {
        return timepoints.stream().map(tp -> ImEx.exportString(tp.tkp) + " " + exporter.apply(tp.value) + " "
                + ImEx.exportString(tp.easingToNext)).collect(Collectors.joining(" ", "", " END"));
    }

    protected String exportCode(String className, Function<T, String> exporter) {
        return timepoints.stream()
                .map(tp -> ".add(" + ImEx.exportCode(tp.tkp) + ", " + exporter.apply(tp.value) + ", "
                        + ImEx.exportCode(tp.easingToNext) + ")")
                .collect(Collectors.joining(" ", "new " + className + "()\n", ""));
    }
}
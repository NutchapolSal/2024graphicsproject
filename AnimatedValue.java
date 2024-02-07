import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AnimatedValue<T> implements Exportable {
    class Timepoint {
        public double time;
        public T value;
        public EasingFunction easingToNext;

        Timepoint(double time, T value, EasingFunction easingToNext) {
            this.time = time;
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

    /**
     * @return index of added timepoint or -1 if timepoint already exists
     */
    protected int addTimepoint(double time, T value, EasingFunction easingToNext) {
        int index = Collections.binarySearch(timepoints, new Timepoint(time, null, null),
                (a, b) -> Double.compare(a.time, b.time));
        if (index < 0) {
            index = ~index;
        } else {
            return -1;
        }

        timepoints.add(index, new Timepoint(time, value, easingToNext));
        return index;
    }

    protected StepValue getValue(double time) {
        int iBeforeTime = -1; // index of timepoint before or equal time
        for (Timepoint timepoint : timepoints) {
            if (timepoint.time <= time) {
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

        double timeBefore = timepoints.get(iBeforeTime).time;
        double timeAfter = timepoints.get(iBeforeTime + 1).time;
        double frac = (time - timeBefore) / (timeAfter - timeBefore);
        return new StepValue(iBeforeTime, timepoints.get(iBeforeTime).easingToNext.applyAsDouble(frac));
    }

    protected T getIndex(int index) {
        return timepoints.get(index).value;
    }

    protected String exportString(Function<T, String> exporter) {
        return timepoints.stream().map(tp -> ImEx.exportString(tp.time) + " " + exporter.apply(tp.value) + " "
                + ImEx.exportString(tp.easingToNext)).collect(Collectors.joining(" ", "", " END"));
    }

    protected String exportCode(String className, Function<T, String> exporter) {
        return timepoints.stream()
                .map(tp -> ".add(" + ImEx.exportCode(tp.time) + ", " + exporter.apply(tp.value) + ", "
                        + ImEx.exportCode(tp.easingToNext) + ")")
                .collect(Collectors.joining(" ", "new " + className + "()\n", ""));
    }
}
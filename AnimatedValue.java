import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
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

    protected List<Timepoint> timepoints = new ArrayList<>();
    protected Map<Double, Integer> timepointCountTimes = new HashMap<>();
    protected Consumer<TimeKeypoint> tkpChangesSubscriber = tkp -> {
        if (tkp.isDeleted()) {
            removeTimepoint(tkp);
        } else {
            sortTimepoints();
        }
    };

    protected void sortTimepoints() {
        Collections.sort(timepoints, (tp1, tp2) -> Double.compare(tp1.tkp.time(), tp2.tkp.time()));
        timepointCountTimes.clear();
        timepoints.forEach(tp -> timepointCountTimes.merge(tp.tkp.time(), 1, Integer::sum));
    }

    /** @throws IllegalArgumentException if the same TimeKeypoint is added twice */
    protected void addTimepoint(TimeKeypoint tkp, T value, EasingFunction easingToNext) {
        if (timepoints.stream().anyMatch(tp -> tp.tkp == tkp)) {
            throw new IllegalArgumentException("Cannot add the same TimeKeypoint twice");
        }
        timepoints.add(new Timepoint(tkp, value, easingToNext));
        subscribeToTKPTimeChanges(tkp);
        sortTimepoints();
    }

    protected void subscribeToTKPTimeChanges(TimeKeypoint tkp) {
        tkp.subscribeToChanges(tkpChangesSubscriber);
    }

    protected void removeTimepoint(TimeKeypoint tkp) {
        timepoints.removeIf(tp -> tp.tkp == tkp);
        tkp.unsubscribeToChanges(tkpChangesSubscriber);
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

    public boolean isAnimated() {
        return timepoints.size() > 1;
    }

    abstract public T get(double time);

    public void staticSet(T value) {
        if (timepoints.size() == 1) {
            timepoints.get(0).value = value;
        } else {
            throw new IllegalStateException("Cannot staticSet on an animated value");
        }
    }

    public int getTimepointCount(double time) {
        return timepointCountTimes.getOrDefault(time, 0);
    }

    public boolean hasTimepoint(TimeKeypoint tkp) {
        return timepoints.stream().anyMatch(tp -> tp.tkp == tkp);
    }

    public boolean hasTimepoint(Optional<TimeKeypoint> tkp) {
        return tkp.isPresent() && hasTimepoint(tkp.get());
    }

    public Optional<EasingFunction> getEasingFunction(TimeKeypoint tkp) {
        return timepoints.stream().filter(tp -> tp.tkp == tkp).map(tp -> tp.easingToNext).findFirst();
    }

    public Optional<EasingFunction> getEasingFunction(Optional<TimeKeypoint> tkp) {
        return tkp.isPresent() ? getEasingFunction(tkp.get()) : Optional.empty();
    }

    public Optional<EasingFunction> getEasingFunction(double time) {
        return timepoints.stream().filter(tp -> tp.tkp.time() <= time).map(tp -> tp.easingToNext).reduce((a, b) -> b);
    }

    public void setEasingFunction(TimeKeypoint tkp, EasingFunction easing) {
        timepoints.stream().filter(tp -> tp.tkp == tkp).forEach(tp -> tp.easingToNext = easing);
    }

    /** in case multiple timepoints have the same time */
    public List<TimeKeypoint> getTimepoint(double time) {
        return timepoints.stream().filter(tp -> tp.tkp.time() == time).map(tp -> tp.tkp).collect(Collectors.toList());
    }

    abstract public AnimatedValue<T> addForEditor(TimeKeypoint tkp, T value, EasingFunction easingToNext);

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
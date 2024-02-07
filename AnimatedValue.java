import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AnimatedValue implements Exportable {
    static class Timepoint {
        public double time;
        public EasingFunction easingToNext;

        Timepoint(double time, EasingFunction easingToNext) {
            this.time = time;
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
    protected int addTimepoint(double time, EasingFunction easingToNext) {
        int index = Collections.binarySearch(timepoints, new Timepoint(time, null),
                (a, b) -> Double.compare(a.time, b.time));
        if (index < 0) {
            index = ~index;
        } else {
            return -1;
        }

        timepoints.add(index, new Timepoint(time, easingToNext));
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

    protected String exportString(List<String> valuepoints) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timepoints.size(); i++) {
            sb.append(timepoints.get(i).time);
            sb.append(" ");
            sb.append(valuepoints.get(i));
            sb.append(" ");
            sb.append(timepoints.get(i).easingToNext);
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

    protected String exportCode(String className, List<String> valuepoints) {
        StringBuilder sb = new StringBuilder("new ");
        sb.append(className);
        sb.append("()");
        for (int i = 0; i < timepoints.size(); i++) {
            sb.append("\n.add(");
            sb.append(timepoints.get(i).time);
            sb.append(", ");
            sb.append(valuepoints.get(i));
            sb.append(", EasingFunction.");
            sb.append(timepoints.get(i).easingToNext);
            sb.append(")");
        }
        return sb.toString();
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TimeKeypoint {
    public final String id;
    public String label = "a time keypoint";
    private double offset;
    private Optional<TimeKeypoint> reference = Optional.empty();
    public List<TimeKeypoint> children = new ArrayList<>();

    public TimeKeypoint(String id, double offset, TimeKeypoint reference, String label) {
        this.id = id;
        this.label = label;
        this.offset = offset;
        setReference(reference, this);
    }

    public void setReference(TimeKeypoint reference, Object source) {
        setReference(Optional.ofNullable(reference), source);
    }

    public void setReference(Optional<TimeKeypoint> newReference, Object source) {
        if (this.reference.isPresent()) {
            this.reference.get().children.remove(this);
        }
        if (newReference.isPresent()) {
            newReference.get().children.add(this);
        }
        this.reference = newReference;
    }

    public double time() {
        var time = this.offset;
        for (var current = this.reference; current.isPresent(); current = current.get().reference) {
            time += current.get().offset;
        }
        return time;
    }

}

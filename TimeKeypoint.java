import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class TimeKeypoint implements Referenceable {
    public final String id;
    public String label = "a time keypoint";
    public double offset;
    public Optional<TimeKeypoint> reference = Optional.empty();
    public List<TimeKeypoint> children = new ArrayList<>();

    public TimeKeypoint(String id, double offset, TimeKeypoint reference, String label) {
        this.id = id;
        this.label = label;
        this.offset = offset;
        setReference(reference);
    }

    public TimeKeypoint(double offset, TimeKeypoint reference, String label) {
        this(UUID.randomUUID().toString(), offset, reference, label);
    }

    void setReference(TimeKeypoint reference) {
        var newReference = Optional.ofNullable(reference);
        if (this.reference.isPresent()) {
            this.reference.get().children.remove(this);
        }
        if (newReference.isPresent()) {
            newReference.get().children.add(this);
        }
        this.reference = newReference;
    }

    public int referenceDepth() {
        var depth = 0;
        for (var current = this.reference; current.isPresent(); current = current.get().reference) {
            depth++;
        }
        return depth;
    }

    public double time() {
        var time = this.offset;
        for (var current = this.reference; current.isPresent(); current = current.get().reference) {
            time += current.get().offset;
        }
        return time;
    }

    public String exportInitString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TIMEKEYPOINT ");
        sb.append(ImEx.exportStringId(id));
        sb.append(" ");
        sb.append(ImEx.exportString(offset));
        sb.append(" ");
        sb.append(reference.isPresent() ? ImEx.exportStringId(reference.get().id) : "NULL");
        sb.append(" ");
        sb.append(ImEx.exportStringUser(label));
        return sb.toString();
    }

    public String exportString() {
        return id;
    }

    public String getCodeId() {
        return "tkp_" + id.replace("-", "_");
    }

    public String exportInitCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        sb.append(getCodeId());
        sb.append(" = new TimeKeypoint(");
        sb.append(ImEx.exportCode(id));
        sb.append(", ");
        sb.append(ImEx.exportCode(offset));
        sb.append(", ");
        sb.append(reference.isPresent() ? reference.get().getCodeId() : "null");
        sb.append(", ");
        sb.append(ImEx.exportCode(label));
        sb.append(");");
        return sb.toString();
    }

    public String exportCode() {
        return getCodeId();
    }
}
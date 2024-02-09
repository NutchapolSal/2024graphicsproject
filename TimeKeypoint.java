import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

class TimeKeypoint implements Referenceable {
    public final String id;
    public String label = "a time keypoint";
    private double offset;
    private Optional<TimeKeypoint> reference = Optional.empty();
    public List<TimeKeypoint> children = new ArrayList<>();
    private Store<TimeKeypoint> timeChangesStore = new Store<>(() -> this);

    public TimeKeypoint(String id, double offset, TimeKeypoint reference, String label) {
        this.id = id;
        this.label = label;
        this.offset = offset;
        setReference(reference);
    }

    public TimeKeypoint(double offset, TimeKeypoint reference, String label) {
        this(UUID.randomUUID().toString(), offset, reference, label);
    }

    public void setReference(TimeKeypoint reference) {
        setReference(Optional.ofNullable(reference));
    }

    public void setReference(Optional<TimeKeypoint> newReference) {
        if (this.reference.isPresent()) {
            this.reference.get().children.remove(this);
        }
        if (newReference.isPresent()) {
            newReference.get().children.add(this);
        }
        this.reference = newReference;
        timeChangesStore.broadcast();
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞𝐊𝐞𝐲𝐩𝐨𝐢𝐧𝐭 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<TimeKeypoint> subscribeToTimeChanges(Consumer<TimeKeypoint> subscriber) {
        return timeChangesStore.subscribe(subscriber);
    }

    public Optional<TimeKeypoint> getReference() {
        return reference;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
        timeChangesStore.broadcast();
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

    public boolean isValidReference(Optional<TimeKeypoint> tkp) {
        return isValidReference(tkp.orElse(null));
    }

    public boolean isValidReference(TimeKeypoint tkp) {
        if (tkp == null) {
            return true;
        }
        for (var current = tkp; current != null; current = current.reference.orElse(null)) {
            if (current == this) {
                return false;
            }
        }
        return true;
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

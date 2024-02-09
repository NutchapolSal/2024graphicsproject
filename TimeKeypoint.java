import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class TimeKeypoint implements Referenceable {
    public final String id;
    public String label = "a time keypoint";
    private double offset;
    private Optional<TimeKeypoint> reference = Optional.empty();
    public List<TimeKeypoint> children = new ArrayList<>();
    private boolean deleted = false;
    private Store<TimeKeypoint> timeChangesStore = new Store<>(() -> this);
    private Store<TimeKeypoint> animValueStore = new Store<>(() -> this);

    private BiConsumer<TimeKeypoint, Object> timeChangeSubscriber = (tkp, src) -> {
        timeChangesStore.broadcast(this);
        animValueStore.broadcast(src);
    };

    public TimeKeypoint(String id, double offset, TimeKeypoint reference, String label) {
        this.id = id;
        this.label = label;
        this.offset = offset;
        setReference(reference, this);
    }

    public TimeKeypoint(double offset, TimeKeypoint reference, String label) {
        this(UUID.randomUUID().toString(), offset, reference, label);
    }

    public void setReference(TimeKeypoint reference, Object source) {
        setReference(Optional.ofNullable(reference), source);
    }

    public void setReference(Optional<TimeKeypoint> newReference, Object source) {
        if (this.reference.isPresent()) {
            this.reference.get().children.remove(this);
            this.reference.get().timeChangesStore.unsubscribe(timeChangeSubscriber);
        }
        if (newReference.isPresent()) {
            newReference.get().children.add(this);
            this.reference = newReference;
            newReference.get().timeChangesStore.subscribe(timeChangeSubscriber);
        } else {
            this.reference = newReference;
            timeChangesStore.broadcast(source);
            animValueStore.broadcast(source);
        }
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞𝐊𝐞𝐲𝐩𝐨𝐢𝐧𝐭 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<TimeKeypoint> subscribeToChanges(Consumer<TimeKeypoint> subscriber) {
        return animValueStore.subscribe(subscriber);
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞𝐊𝐞𝐲𝐩𝐨𝐢𝐧𝐭 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<TimeKeypoint, Object> subscribeToChanges(BiConsumer<TimeKeypoint, Object> subscriber) {
        return animValueStore.subscribe(subscriber);
    }

    public void unsubscribeToChanges(Consumer<TimeKeypoint> subscriber) {
        animValueStore.unsubscribe(subscriber);
    }

    public Optional<TimeKeypoint> getReference() {
        return reference;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset, Object source) {
        this.offset = offset;
        timeChangesStore.broadcast(source);
        animValueStore.broadcast(source);
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

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(Object source) {
        deleted = true;
        animValueStore.broadcast(source);
    }

    private static String formatTime(double time) {
        return String.format("%.3f", time).replaceAll("(?<!\\.)0+$", "");
    }

    private static String formatOffset(double offset) {
        return String.format("%+.3f", offset).replaceAll("(?<!\\.)0+$", "");
    }

    public String exportDisplayString(boolean showLabel, boolean showTime, boolean showOffset, boolean showId) {
        List<String> parts = new ArrayList<>();
        if (showLabel) {
            parts.add(label);
        }
        if (showTime) {
            parts.add("@");
            parts.add(formatTime(time()));
        }
        if (showOffset) {
            parts.add("(" + formatOffset(offset) + ")");
        }
        if (showId) {
            parts.add("-");
            parts.add(id);
        }
        return String.join(" ", parts);
    }

    /** starts with first TKP after null, ends with self */
    private List<TimeKeypoint> getRefChain() {
        List<TimeKeypoint> references = new ArrayList<>();
        references.add(this);
        for (var current = this.reference; current.isPresent(); current = current.get().reference) {
            references.add(0, current.get());
        }
        return references;
    }

    public static int compareHierarchy(TimeKeypoint a, TimeKeypoint b) {
        var chainA = a.getRefChain();
        var chainB = b.getRefChain();
        TimeKeypoint uncommonA = null;
        TimeKeypoint uncommonB = null;
        for (int i = 0; i < Math.min(chainA.size(), chainB.size()); i++) {
            uncommonA = chainA.get(i);
            uncommonB = chainB.get(i);
            if (chainA.get(i) != chainB.get(i)) {
                break;
            }
        }
        return Double.compare(uncommonA.offset, uncommonB.offset);
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

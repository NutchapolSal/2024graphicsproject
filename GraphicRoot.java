import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

class GraphicRoot implements Exportable {
    public List<TimeKeypoint> timeKeypoints;
    public Palette palette;
    public List<GraphicLayer> instructions;
    private double currentTime;
    private Set<DoubleConsumer> timeSubscribers = Collections.newSetFromMap(new WeakHashMap<>());
    private Set<Consumer<TimeKeypoint>> tkpFocusSubscribers = Collections.newSetFromMap(new WeakHashMap<>());

    public GraphicRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        this.timeKeypoints = timeKeypoints;
        this.palette = palette;
        this.instructions = instructions;
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞 for free!!
     * 
     * @return the subscriber, for putting in a non-weak reference if you want to
     *         keep it around
     */
    public DoubleConsumer subscribeToTime(DoubleConsumer subscriber) {
        timeSubscribers.add(subscriber);
        subscriber.accept(currentTime);
        return subscriber;
    }

    public void setTime(double time) {
        currentTime = time;
        timeSubscribers.forEach(sub -> sub.accept(time));
    }

    /**
     * subscribe now and get one 𝐓𝐊𝐏 for free!!
     * 
     * @return the subscriber, for putting in a non-weak reference if you want to
     *         keep it around
     */
    public Consumer<TimeKeypoint> subscribeToTKP(Consumer<TimeKeypoint> subscriber) {
        tkpFocusSubscribers.add(subscriber);
        timeKeypoints.forEach(subscriber);
        return subscriber;
    }

    public void addTimeKeypoint(TimeKeypoint tkp) {
        timeKeypoints.add(tkp);
        tkpFocusSubscribers.forEach(sub -> sub.accept(tkp));
    }

    public double getTime() {
        return currentTime;
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.exportStringTKPs(timeKeypoints));
        sb.append(ImEx.exportInitStringPaletteValues(palette));
        sb.append(ImEx.exportString(palette));
        sb.append("\n");
        sb.append(ImEx.exportStringLayers(instructions));
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImEx.generateAnnotation("ImEx"));
        sb.append("\n");
        sb.append("\n");
        sb.append(ImEx.exportInitCodeTKPs(timeKeypoints));
        sb.append("\n");
        sb.append(ImEx.exportInitCodePaletteValues(palette));
        sb.append("\n");
        sb.append("List<TimeKeypoint> timeKeypoints = new ArrayList<>();\n");
        sb.append(ImEx.exportCodeTKPs(timeKeypoints));
        sb.append("\n");
        sb.append("Palette palette = ");
        sb.append(ImEx.exportCode(palette));
        sb.append(";\n");
        sb.append("List<GraphicLayer> instructions = new ArrayList<>();\n");
        sb.append(ImEx.exportCodeLayers(instructions));
        sb.append("\n");
        sb.append("return new GraphicRoot(timeKeypoints, palette, instructions);");
        return sb.toString();
    }

}

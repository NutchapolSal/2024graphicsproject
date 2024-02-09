import java.util.List;
import java.util.function.Consumer;

class GraphicRoot implements Exportable {
    public List<TimeKeypoint> timeKeypoints;
    public Palette palette;
    public List<GraphicLayer> instructions;
    private double currentTime;
    private TimeKeypoint tkpFocus;
    private Store<Double> timeStore = new Store<>(() -> this.getTime());
    private Store<TimeKeypoint> tkpFocusStore = new Store<>(() -> this.getTimeKeypointFocus());

    public GraphicRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        this.timeKeypoints = timeKeypoints;
        this.palette = palette;
        this.instructions = instructions;
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<Double> subscribeToTime(Consumer<Double> subscriber) {
        return timeStore.subscribe(subscriber);
    }

    public void setTime(double time) {
        currentTime = time;
        timeStore.broadcast();
    }

    public double getTime() {
        return currentTime;
    }

    /**
     * subscribe now and get one 𝐓𝐊𝐏 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<TimeKeypoint> subscribeToTKPFocus(Consumer<TimeKeypoint> subscriber) {
        return tkpFocusStore.subscribe(subscriber);
    }

    public void setTimeKeypointFocus(TimeKeypoint tkp) {
        tkpFocus = tkp;
        tkpFocusStore.broadcast();
    }

    public TimeKeypoint getTimeKeypointFocus() {
        return tkpFocus;
    }

    public void addTimeKeypoint(TimeKeypoint tkp) {
        timeKeypoints.add(tkp);
        setTimeKeypointFocus(tkp);
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

class GraphicRoot implements Exportable {
    public List<TimeKeypoint> timeKeypoints;
    public Palette palette;
    public List<GraphicLayer> instructions;
    private double currentTime;
    private List<DoubleConsumer> timeSubscribers = new ArrayList<>();

    public GraphicRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        this.timeKeypoints = timeKeypoints;
        this.palette = palette;
        this.instructions = instructions;
    }

    /** subscribe now and get one 𝐓𝐢𝐦𝐞 for free!! */
    public void subscribeToTime(DoubleConsumer subscriber) {
        timeSubscribers.add(subscriber);
        subscriber.accept(currentTime);
    }

    public void setTime(double time) {
        currentTime = time;
        timeSubscribers.forEach(sub -> sub.accept(time));
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

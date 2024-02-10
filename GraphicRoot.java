import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.Timer;

class GraphicRoot implements Exportable {
    public List<TimeKeypoint> timeKeypoints;
    public Palette palette;
    public List<GraphicLayer> instructions;

    private double currentTime;
    private int fps = 1;
    private boolean useFps = false;

    private double startTime;
    private long startNanoTime = System.nanoTime();
    private Timer timeTicker = new Timer(1, e -> {
        long currTime = System.nanoTime();
        if (useFps && currTime - startNanoTime < 1_000_000_000 / fps) {
            return;
        }
        double deltaSecs = (currTime - startNanoTime) / 1_000_000_000.0;

        this.setTime(useFps ? Math.round((startTime + deltaSecs) * fps) / (double) fps : startTime + deltaSecs,
                this.timeTicker);
    });
    private BiConsumer<Double, Object> timeSubscriber = (time, source) -> {
        if (source != this.timeTicker) {
            currentTime = time;
        }
    };

    static class TimeAndTKPFocus {
        public final double time;
        public final Optional<TimeKeypoint> tkpFocus;

        TimeAndTKPFocus(double time, Optional<TimeKeypoint> tkpFocus) {
            this.time = time;
            this.tkpFocus = tkpFocus;
        }

        boolean isPresent() {
            return tkpFocus.isPresent();
        }

        TimeKeypoint get() {
            return tkpFocus.get();
        }
    }

    private Optional<TimeKeypoint> tkpFocus = Optional.empty();
    private Store<Double> timeStore = new Store<>(this::getTime);
    private Store<Boolean> timeRunningStore = new Store<>(this::getTimeRunning);
    private Store<Optional<TimeKeypoint>> tkpFocusStore = new Store<>(this::getTimeKeypointFocus);

    private Store<TimeAndTKPFocus> timeAndTKPFocusStore = new Store<>(this::getTimeAndTKPFocus);

    public GraphicRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        replaceRoot(timeKeypoints, palette, instructions);

        timeStore.subscribe(timeSubscriber);
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<Double> subscribeToTime(Consumer<Double> subscriber) {
        return timeStore.subscribe(subscriber);
    }

    /**
     * subscribe now and get one 𝐓𝐢𝐦𝐞 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<Double, Object> subscribeToTime(BiConsumer<Double, Object> subscriber) {
        return timeStore.subscribe(subscriber);
    }

    public void setTime(double time, Object source) {
        if (source != timeTicker) {
            startTime = time;
            startNanoTime = System.nanoTime();
        }
        currentTime = time;
        timeStore.broadcast(source);
        timeAndTKPFocusStore.broadcast(source);
    }

    public double getTime() {
        return currentTime;
    }

    public void startTime(Object source) {
        startTime = currentTime;
        startNanoTime = System.nanoTime();
        timeTicker.start();
        timeRunningStore.broadcast(source);
    }

    public void pauseTime(Object source) {
        timeTicker.stop();
        timeRunningStore.broadcast(source);
    }

    public boolean getTimeRunning() {
        return timeTicker.isRunning();
    }

    /**
     * subscribe now and get one 𝐓𝐊𝐏 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<Optional<TimeKeypoint>> subscribeToTKPFocus(Consumer<Optional<TimeKeypoint>> subscriber) {
        return tkpFocusStore.subscribe(subscriber);
    }

    /**
     * subscribe now and get one 𝐓𝐊𝐏 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<Optional<TimeKeypoint>, Object> subscribeToTKPFocus(
            BiConsumer<Optional<TimeKeypoint>, Object> subscriber) {
        return tkpFocusStore.subscribe(subscriber);
    }

    public void setTimeKeypointFocus(TimeKeypoint tkp, Object source) {
        setTimeKeypointFocus(Optional.ofNullable(tkp), source);
    }

    public void setTimeKeypointFocus(Optional<TimeKeypoint> tkp, Object source) {
        tkpFocus = tkp;
        tkpFocusStore.broadcast(source);
        timeAndTKPFocusStore.broadcast(source);
    }

    public Optional<TimeKeypoint> getTimeKeypointFocus() {
        return tkpFocus;
    }

    public void addTimeKeypoint(TimeKeypoint tkp, Object source) {
        timeKeypoints.add(tkp);
        setTimeKeypointFocus(tkp, source);
    }

    public void removeTimeKeypoint(TimeKeypoint tkp, Object source) {
        tkp.delete(source);
        timeKeypoints.remove(tkp);
        if (tkpFocus.orElseGet(null) == tkp) {
            tkpFocus = null;
        }
        tkpFocusStore.broadcast(source);
        timeAndTKPFocusStore.broadcast(source);
    }

    /**
     * is your 𝐓𝐢𝐦𝐞 𝐑𝐮𝐧𝐧𝐢𝐧𝐠? well then go catch it!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<Boolean> subscribeToTimeRunning(Consumer<Boolean> subscriber) {
        return timeRunningStore.subscribe(subscriber);
    }

    /**
     * is your 𝐓𝐢𝐦𝐞 𝐑𝐮𝐧𝐧𝐢𝐧𝐠? well then go catch it!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<Boolean, Object> subscribeToTimeRunning(BiConsumer<Boolean, Object> subscriber) {
        return timeRunningStore.subscribe(subscriber);
    }

    /**
     * limited 𝐓𝐢𝐦𝐞 deal!!: free 𝐓𝐊𝐏 for every order!!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<TimeAndTKPFocus> subscribeToTimeAndTKPFocus(Consumer<TimeAndTKPFocus> subscriber) {
        return timeAndTKPFocusStore.subscribe(subscriber);
    }

    /**
     * limited 𝐓𝐢𝐦𝐞 deal!!: free 𝐓𝐊𝐏 for every order!!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<TimeAndTKPFocus, Object> subscribeToTimeAndTKPFocus(
            BiConsumer<TimeAndTKPFocus, Object> subscriber) {
        return timeAndTKPFocusStore.subscribe(subscriber);
    }

    public TimeAndTKPFocus getTimeAndTKPFocus() {
        return new TimeAndTKPFocus(getTime(), getTimeKeypointFocus());
    }

    public TimeKeypoint getFirstTimeKeypoint() {
        return timeKeypoints.get(0);
    }

    public Optional<Integer> getFps() {
        if (useFps) {
            return Optional.of(fps);
        } else {
            return Optional.empty();
        }
    }

    public void setFps(Optional<Integer> fps) {
        if (fps.isPresent()) {
            this.fps = fps.get();
            useFps = true;
        } else {
            useFps = false;
        }
    }

    /** editor defaults */
    public void newFile() {
        List<TimeKeypoint> timeKeypoints = new ArrayList<>();
        timeKeypoints.add(new TimeKeypoint(0.0, null, "origin"));
        Palette palette = new Palette();
        List<GraphicLayer> instructions = new ArrayList<>();
        replaceRoot(timeKeypoints, palette, instructions);
    }

    public void replaceRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        this.timeKeypoints = timeKeypoints;
        this.palette = palette;
        this.instructions = instructions;

        setTimeKeypointFocus(Optional.empty(), this);
        pauseTime(this);
        setTime(0, this);
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

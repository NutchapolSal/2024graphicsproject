import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.Timer;

class GraphicRoot {
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

    private Store<Double> timeStore = new Store<>(this::getTime);
    private Store<Boolean> timeRunningStore = new Store<>(this::getTimeRunning);

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

    public void replaceRoot(List<TimeKeypoint> timeKeypoints, Palette palette, List<GraphicLayer> instructions) {
        this.timeKeypoints = timeKeypoints;
        this.palette = palette;
        this.instructions = instructions;

        pauseTime(this);
        setTime(0, this);
    }
}

import java.awt.geom.Path2D;

class Path2DLine extends Path2DData {
    public AnimPoint pNext;

    Path2DLine(AnimPoint pNext) {
        this.pNext = pNext;
    }

    @Override
    public void run(Path2D path, double time) {
        path.lineTo(pNext.get(time).x, pNext.get(time).y);
    }

    @Override
    public int size() {
        return 1;
    }
}
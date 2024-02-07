import java.awt.geom.Path2D;

abstract class Path2DData implements Exportable {
    abstract public void run(Path2D path, double time);

    abstract public int size();

    abstract AnimPoint lastPoint();
}
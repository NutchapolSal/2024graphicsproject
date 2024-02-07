import java.awt.Graphics;

abstract class GraphicObject implements Exportable, Debuggable {
    abstract public void draw(Graphics g, double time);

    protected int debugging = -1;

    abstract public GraphicObject copy();

    @Override
    public void setDebugging(int index) {
        this.debugging = index;
    }

    @Override
    public void unsetDebugging() {
        this.debugging = -1;
    }

    // abstract public GraphicObject editorCopy();

}
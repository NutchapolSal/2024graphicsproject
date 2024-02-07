import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

class GraphicLayer implements Exportable, Debuggable {
    public MutableString name;
    public AnimBoolean shown;
    public AnimPoint translate;
    public AnimPoint rotateOrigin;
    public AnimDouble rotate;
    public List<GraphicObject> objects;
    private int debugging = -1;

    GraphicLayer() {
        this("new layer", new AnimBoolean(), new AnimPoint(), new AnimPoint(), new AnimDouble(),
                new ArrayList<>());
    }

    GraphicLayer(String name, AnimBoolean shown, AnimPoint translate, AnimPoint rotateOrigin, AnimDouble rotate) {
        this(name, shown, translate, rotateOrigin, rotate, new ArrayList<>());
    }

    GraphicLayer(String name, AnimBoolean shown, AnimPoint translate, AnimPoint rotateOrigin, AnimDouble rotate,
            List<GraphicObject> objects) {
        this.name = new MutableString(name);
        this.shown = shown;
        this.translate = translate;
        this.rotate = rotate;
        this.rotateOrigin = rotateOrigin;
        this.objects = objects;
    }

    Graphics2D transform(Graphics gOuter, double time) {
        Graphics2D g = (Graphics2D) gOuter.create();
        g.translate(translate.get(time).x, translate.get(time).y);
        g.rotate(rotate.get(time) * Math.PI / 180.0, rotateOrigin.get(time).x, rotateOrigin.get(time).y);
        return g;
    }

    void draw(Graphics gOuter, double time) {
        Graphics2D g = transform(gOuter, time);
        for (GraphicObject object : objects) {
            object.draw(g, time);
        }
    }

    @Override
    public void debugDraw(Graphics gOuter, double time) {
        Graphics2D g = transform(gOuter, time);
        if (this.debugging != -1) {
            debugDrawSelf(g, time);
        }

        for (GraphicObject object : objects) {
            if (object.debugging != -1) {
                object.debugDraw(g, time);
            }
        }
    }

    private void debugDrawSelf(Graphics2D g, double time) {
        var translate = this.translate.get(time);
        var rotateOrigin = this.rotateOrigin.get(time);
        var rotate = this.rotate.get(time);
        debugCircle(g, translate.x, translate.y, debugging == 1);
        debugDot(g, rotateOrigin.x, rotateOrigin.y, debugging == 2);
        debugLine(g, rotateOrigin.x, rotateOrigin.y,
                (int) (Math.sin(rotate * Math.PI / 180) * 20 + rotateOrigin.x),
                (int) (Math.cos(rotate * Math.PI / 180) * 20 + rotateOrigin.y),
                debugging == 3);

    }

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

    GraphicLayer remove(GraphicObject object) {
        objects.remove(object);
        return this;
    }

    @Override
    public void setDebugging(int index) {
        this.debugging = index;
    }

    @Override
    public void unsetDebugging() {
        this.debugging = -1;
    }

    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LAYER ");
        sb.append(this.name);
        sb.append("\n");
        sb.append(ImEx.exportString(this.shown));
        sb.append("\n");
        sb.append(ImEx.exportString(this.translate));
        sb.append("\n");
        sb.append(ImEx.exportString(this.rotateOrigin));
        sb.append("\n");
        sb.append(ImEx.exportString(this.rotate));
        sb.append("\n");
        for (GraphicObject object : this.objects) {
            sb.append(object.exportString());
            sb.append("\n");
        }
        sb.append("END");
        return sb.toString();
    }

    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new GraphicLayer(\"");
        sb.append(this.name);
        sb.append("\", ");
        sb.append(ImEx.exportCode(this.shown));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.translate));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.rotateOrigin));
        sb.append(", ");
        sb.append(ImEx.exportCode(this.rotate));
        sb.append(")\n");
        for (GraphicObject object : this.objects) {
            sb.append(".add(");
            sb.append(object.exportCode());
            sb.append(")\n");
        }
        return sb.toString();
    }

}
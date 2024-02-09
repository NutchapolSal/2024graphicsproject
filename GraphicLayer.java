import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
        this("new layer", new AnimBoolean(), new AnimPoint(), new AnimPoint(), new AnimDouble(), new ArrayList<>());
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
        Graphics2D g = (Graphics2D) gOuter.create();
        if (this.debugging != -1) {
            debugDrawSelf(g, time);
        }

        Graphics2D gt = transform(g, time);
        for (GraphicObject object : objects) {
            if (object.debugging != -1) {
                object.debugDraw(gt, time);
            }
        }
    }

    private void debugDrawSelf(Graphics2D g, double time) {
        var translate = this.translate.get(time);
        var rotateOrigin = this.rotateOrigin.get(time);
        var rotateOriginLocal = new Point(rotateOrigin.x + translate.x, rotateOrigin.y + translate.y);
        var rotate = this.rotate.get(time);
        var rotateOriginVectorEnd = new Point((int) (Math.sin(rotate * Math.PI / 180) * 30 + rotateOriginLocal.x),
                (int) (Math.cos(rotate * Math.PI / 180) * -30 + rotateOriginLocal.y));
        debugCircle(g, translate.x, translate.y, debugging == 1);
        debugLine(g, rotateOriginLocal.x, rotateOriginLocal.y, rotateOriginVectorEnd.x, rotateOriginVectorEnd.y,
                debugging == 3);
        debugDot(g, rotateOriginVectorEnd.x, rotateOriginVectorEnd.y, debugging == 3);
        debugDot(g, rotateOriginLocal.x, rotateOriginLocal.y, debugging == 2);

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
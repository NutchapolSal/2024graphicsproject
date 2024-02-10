import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

class GraphicLayer {
    public MutableString name;
    public AnimBoolean shown;
    public AnimPoint translate;
    public AnimPoint rotateOrigin;
    public AnimDouble rotate;
    public List<GraphicObject> objects;

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

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

}
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ImEx {

    public static String exportString(Exportable obj) {
        return obj.exportString();
    }

    public static String exportString(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append(layer.exportString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String exportString(double d) {
        return Double.toString(d);
    }

    public static String exportString(int i) {
        return Integer.toString(i);
    }

    public static String exportString(Color color) {
        return ColorHexer.encode(color);
    }

    public static String exportString(Point point) {
        StringBuilder sb = new StringBuilder();
        sb.append(point.x);
        sb.append(",");
        sb.append(point.y);
        return sb.toString();
    }

    public static String exportString(Dimension dim) {
        StringBuilder sb = new StringBuilder();
        sb.append(dim.width);
        sb.append(",");
        sb.append(dim.height);
        return sb.toString();
    }

    public static String exportString(boolean bool) {
        return bool ? "T" : "F";
    }

    public static String exportCode(Exportable obj) {
        return obj.exportCode();
    }

    public static String exportCode(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append("instructions.add(");
            sb.append(layer.exportCode());
            sb.append(");\n");
        }
        return sb.toString();
    }

    public static String exportCode(Color color) {
        return "\"" + ColorHexer.encode(color) + "\"";
    }

    public static String exportCode(Point point) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Point(");
        sb.append(point.x);
        sb.append(",");
        sb.append(point.y);
        sb.append(")");
        return sb.toString();
    }

    public static String exportCode(Dimension dim) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Dimension(");
        sb.append(dim.width);
        sb.append(",");
        sb.append(dim.height);
        sb.append(")");
        return sb.toString();
    }

    public static String exportCode(boolean bool) {
        return bool ? "true" : "false";
    }

    public static String exportCode(double d) {
        return Double.toString(d);
    }

    public static String exportCode(int i) {
        return Integer.toString(i);
    }

    public static List<GraphicLayer> importString(String str) {
        Scanner sc = new Scanner(str);
        List<GraphicLayer> layers = importLayers(sc);
        sc.close();
        return layers;
    }

    public static boolean importBoolean(Scanner sc) {
        return sc.next().equals("T");
    }

    public static String importHexColor(Scanner sc) {
        return sc.next();
    }

    public static Point importPoint(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    public static Dimension importDimension(Scanner sc) {
        String[] coords = sc.next().split(",");
        return new Dimension(Integer.parseInt(coords[0]),
                Integer.parseInt(coords[1]));
    }

    public static int importInt(Scanner sc) {
        return sc.nextInt();
    }

    public static double importDouble(Scanner sc) {
        return sc.nextDouble();
    }

    public static String importUserString(Scanner sc) {
        sc.skip(" ");
        return sc.nextLine();
    }

    public static EasingFunction importEasingFunction(Scanner sc) {
        return EasingFunction.valueOf(sc.next());
    }

    private static class AnimValueData<T> {
        public List<AnimatedValue.Timepoint> timepoints;
        public List<T> values;

        public AnimValueData(List<AnimatedValue.Timepoint> timepoints, List<T> values) {
            this.timepoints = timepoints;
            this.values = values;
        }

    }

    private static <T> AnimValueData<T> importAnimValues(Scanner sc, Function<Scanner, T> importFunction) {
        List<AnimatedValue.Timepoint> timepoints = new ArrayList<>();
        List<T> values = new ArrayList<>();
        while (true) {
            var time = sc.nextDouble();
            values.add(importFunction.apply(sc));
            var easingToNext = importEasingFunction(sc);
            timepoints.add(new AnimatedValue.Timepoint(time, easingToNext));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new AnimValueData<>(timepoints, values);
    }

    public static AnimBoolean importAnimBoolean(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importBoolean);
        var anim = new AnimBoolean();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimColor importAnimColor(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importHexColor);
        var anim = new AnimColor();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimPoint importAnimPoint(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importPoint);
        var anim = new AnimPoint();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimDimension importAnimDimension(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importDimension);
        var anim = new AnimDimension();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimDouble importAnimDouble(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importDouble);
        var anim = new AnimDouble();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static AnimInt importAnimInt(Scanner sc) {
        var data = importAnimValues(sc, ImEx::importInt);
        var anim = new AnimInt();
        for (int i = 0; i < data.timepoints.size(); i++) {
            var tp = data.timepoints.get(i);
            anim.add(tp.time, data.values.get(i), tp.easingToNext);
        }
        return anim;
    }

    public static List<GraphicLayer> importLayers(Scanner sc) {
        List<GraphicLayer> layers = new ArrayList<>();
        while (sc.hasNext()) {
            String type = sc.next();
            switch (type) {
                case "LAYER":
                    layers.add(importLayer(sc));
                    break;
            }
        }
        return layers;
    }

    public static GraphicLayer importLayer(Scanner sc) {
        String layerName = importUserString(sc);
        AnimBoolean visible = importAnimBoolean(sc);
        AnimPoint translate = importAnimPoint(sc);
        AnimPoint rotateOrigin = importAnimPoint(sc);
        AnimDouble rotate = importAnimDouble(sc);
        List<GraphicObject> objects = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
                case "PATH2D":
                    objects.add(importPath2D(sc));
                    break;
                case "CIRCLE":
                    objects.add(importCircle(sc));
                    break;
                case "IMAGE":
                    objects.add(importImage(sc));
                    break;
            }
        }
        return new GraphicLayer(layerName, visible, translate, rotateOrigin, rotate, objects);
    }

    public static Path2DLine importPath2DLine(Scanner sc) {
        AnimPoint pNext = importAnimPoint(sc);
        return new Path2DLine(pNext);
    }

    public static Path2DBezier importPath2DBezier(Scanner sc) {
        AnimPoint pNext = importAnimPoint(sc);
        List<AnimPoint> morePoints = new ArrayList<>();
        while (true) {
            morePoints.add(importAnimPoint(sc));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new Path2DBezier(pNext, morePoints);
    }

    public static GraphicPath2D importPath2D(Scanner sc) {
        AnimBoolean stroke = importAnimBoolean(sc);
        AnimColor strokeColor = importAnimColor(sc);
        AnimInt thickness = importAnimInt(sc);
        AnimBoolean fill = importAnimBoolean(sc);
        AnimColor fillColor = importAnimColor(sc);
        AnimBoolean closed = importAnimBoolean(sc);
        AnimPoint p1 = importAnimPoint(sc);
        List<Path2DData> data = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
                case "LINE":
                    data.add(importPath2DLine(sc));
                    break;
                case "BEZIER":
                    data.add(importPath2DBezier(sc));
                    break;
            }
        }
        return new GraphicPath2D(stroke, strokeColor, thickness, fill, fillColor, closed, p1, data);
    }

    public static GraphicCircle importCircle(Scanner sc) {
        AnimColor color = importAnimColor(sc);
        AnimInt thickness = importAnimInt(sc);
        AnimPoint center = importAnimPoint(sc);
        AnimInt radius = importAnimInt(sc);
        return new GraphicCircle(color, thickness, center, radius);
    }

    public static GraphicImage importImage(Scanner sc) {
        String filePath = importUserString(sc);
        AnimPoint origin = importAnimPoint(sc);
        AnimDimension size = importAnimDimension(sc);
        AnimDouble opacity = importAnimDouble(sc);
        return new GraphicImage(filePath, origin, size, opacity);
    }

}
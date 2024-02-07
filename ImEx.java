import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class ImEx {

    public static void test() {
        var root = PreloadedData.create();
        var exx = ImEx.exportString(root);
        var reimport = ImEx.exportString(ImEx.importString(exx));
        System.out.println("ImEx test");
        System.out.println("string export:");
        System.out.println(exx);
        System.out.println();
        System.out.println("---");
        System.out.println();
        System.out.println("re-imported:" + (exx.equals(reimport) ? "ok" : "FAIL"));
        System.out.println();
        if (!exx.equals(reimport)) {
            System.out.println(reimport);
        }
        System.out.println("---");
        System.out.println();
        System.out.println("code export:");
        System.out.println(ImEx.exportCode(root));
    }

    public static String exportString(Exportable obj) {
        return obj.exportString();
    }

    public static String exportString(Referenceable obj) {
        return obj.exportString();
    }

    public static String exportInitString(Referenceable obj) {
        return obj.exportInitString();
    }

    public static String exportInitStringPaletteValues(Palette palette) {
        return palette.exportInitStringPaletteValues();
    }

    public static String exportStringLayers(List<GraphicLayer> instructions) {
        return instructions.stream().map(ImEx::exportString).collect(Collectors.joining("\n"));
    }

    public static String exportStringTKPs(List<TimeKeypoint> timeKeypoints) {
        var tkps = new ArrayList<>(timeKeypoints);
        tkps.sort(Comparator.comparingInt(TimeKeypoint::referenceDepth));
        return tkps.stream().map(ImEx::exportInitString).collect(Collectors.joining("\n"));
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

    public static String exportStringUser(String str) {
        return str + "\n";
    }

    public static String exportStringId(String id) {
        return id;
    }

    public static String getCodeId(Referenceable obj) {
        return obj.getCodeId();
    }

    public static String exportCode(Exportable obj) {
        return obj.exportCode();
    }

    public static String exportCode(Referenceable obj) {
        return obj.exportCode();
    }

    public static String exportInitCode(Referenceable obj) {
        return obj.exportInitCode();
    }

    public static String exportCodeLayers(List<GraphicLayer> instructions) {
        StringBuilder sb = new StringBuilder();
        for (GraphicLayer layer : instructions) {
            sb.append("instructions.add(");
            sb.append(ImEx.exportCode(layer));
            sb.append(");\n");
        }
        return sb.toString();
    }

    public static String exportInitCodePaletteValues(Palette palette) {
        return StreamSupport.stream(palette.sparseIterator().spliterator(), true).map(v -> v.value.exportInitCode())
                .collect(Collectors.joining("\n"));
    }

    public static String exportInitCodeTKPs(List<TimeKeypoint> timeKeypoints) {
        return timeKeypoints.stream().map(TimeKeypoint::exportInitCode).collect(Collectors.joining("\n"));
    }

    public static String exportCodeTKPs(List<TimeKeypoint> timeKeypoints) {
        return timeKeypoints.stream().map(TimeKeypoint::exportCode)
                .collect(Collectors.joining(");\ntimeKeypoints.add(", "timeKeypoints.add(", ");"));
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

    /** wrap string in quotes */
    public static String exportCode(String str) {
        return "\"" + str.replace("\n", "\\n") + "\"";
    }

    public static GraphicRoot importString(String str) {
        Scanner sc = new Scanner(str);
        GraphicRoot root = importRoot(sc);
        sc.close();
        return root;
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
        return new Dimension(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
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

    public static String importStringId(Scanner sc) {
        return sc.next();
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

    public static GraphicRoot importRoot(Scanner sc) {
        HashMap<String, TimeKeypoint> timeKeypoints = new HashMap<>();
        HashMap<String, Set<String>> timeAwaits = new HashMap<>();
        HashMap<String, PaletteValue> paletteValues = new HashMap<>();
        Palette palette = new Palette();
        List<GraphicLayer> layers = new ArrayList<>();
        while (sc.hasNext()) {
            String type = sc.next();
            switch (type) {
            case "TIMEKEYPOINT":
                importTimeKeypoint(sc, timeKeypoints, timeAwaits);
                break;
            case "PALETTEVALUE":
                var pv = importPaletteValue(sc);
                paletteValues.put(pv.id, pv);
                break;
            case "PALETTE":
                palette = importPalette(sc, paletteValues);
                break;
            case "LAYER":
                layers.add(importLayer(sc));
                break;
            }
        }
        return new GraphicRoot(new ArrayList<>(timeKeypoints.values()), palette, layers);
    }

    public static void importTimeKeypoint(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, Set<String>> timeAwaits) {
        String id = importStringId(sc);
        double offset = importDouble(sc);
        TimeKeypoint tkp = new TimeKeypoint(id, offset, null);
        timeKeypoints.put(id, tkp);
        if (timeAwaits.containsKey(id)) {
            for (String awaitId : timeAwaits.get(id)) {
                timeKeypoints.get(awaitId).setReference(tkp);
            }
            timeAwaits.remove(id);
        }
        if (sc.hasNext("NULL")) {
            sc.next();
            return;
        }

        String referenceId = importStringId(sc);
        if (timeKeypoints.containsKey(referenceId)) {
            tkp.setReference(timeKeypoints.get(referenceId));
        } else {
            timeAwaits.computeIfAbsent(referenceId, k -> new HashSet<>()).add(id);
        }

    }

    public static PaletteValue importPaletteValue(Scanner sc) {
        String id = importStringId(sc);
        String color = importHexColor(sc);
        String label = importUserString(sc);
        return new PaletteValue(id, color, label);
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

    public static Palette importPalette(Scanner sc, HashMap<String, PaletteValue> paletteValues) {
        Palette palette = new Palette();
        while (sc.hasNext()) {
            int y = importInt(sc);
            int x = importInt(sc);
            String valueId = importStringId(sc);
            if (paletteValues.containsKey(valueId)) {
                palette.set(x, y, paletteValues.get(valueId));
            }
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return palette;
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
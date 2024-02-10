import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        System.out.println("ImEx test");
        System.out.println("string export:");
        System.out.println(exx);
        System.out.println();
        System.out.println("---");
        System.out.println();
        var reimport = ImEx.exportString(ImEx.importString(exx));
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
        tkps.sort(Comparator.comparing(v -> v.id));
        return tkps.stream().map(ImEx::exportInitString).collect(Collectors.joining());
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

    public static String exportString(EasingFunction easingFunction) {
        return easingFunction.name();
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

    public static String exportCode(EasingFunction easingFunction) {
        return "EasingFunction." + easingFunction.name();
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

    public static String generateAnnotation(String className) {
        return generateAnnotation(className, ZonedDateTime.now());
    }

    public static String generateAnnotation(String className, ZonedDateTime time) {
        return "@Generated(value = " + exportCode(className) + ", date = "
                + exportCode(time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) + ")";
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

    interface TimepointCreator<T> {
        public void apply(TimeKeypoint tkp, T value, EasingFunction easingToNext);
    }

    private static <T> void importAnimValues(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            Function<Scanner, T> importFunction, TimepointCreator<T> creator) {
        while (true) {
            var tkpId = importStringId(sc);
            var value = importFunction.apply(sc);
            var easingToNext = importEasingFunction(sc);
            creator.apply(timeKeypoints.get(tkpId), value, easingToNext);
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
    }

    public static AnimBoolean importAnimBoolean(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimBoolean();
        importAnimValues(sc, timeKeypoints, ImEx::importBoolean, anim::add);
        return anim;
    }

    public static AnimString importAnimString(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimString();
        importAnimValues(sc, timeKeypoints, ImEx::importUserString, anim::add);
        return anim;
    }

    private static class MaybePaletteData {
        public boolean isPaletteValue;
        public String str;

        public MaybePaletteData(String str, boolean isPaletteValue) {
            this.str = str;
            this.isPaletteValue = isPaletteValue;
        }

        public String get() {
            return str;
        }

        public PaletteValue get(HashMap<String, PaletteValue> paletteValues) {
            return paletteValues.get(str);
        }
    }

    public static MaybePaletteData importMaybePaletteValue(Scanner sc) {
        String type = sc.next();
        switch (type) {
        case "COLOR":
            return new MaybePaletteData(importHexColor(sc), false);
        case "PALETTEVALUE":
            return new MaybePaletteData(importStringId(sc), true);
        }
        throw new IllegalArgumentException("invalid type: " + type);
    }

    public static AnimColor importAnimColor(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, PaletteValue> paletteValues) {
        var anim = new AnimColor();
        importAnimValues(sc, timeKeypoints, s -> importMaybePaletteValue(s), (time, value, easingToNext) -> {
            if (value.isPaletteValue) {
                anim.add(time, value.get(paletteValues), easingToNext);
            } else {
                anim.add(time, value.get(), easingToNext);
            }
        });
        return anim;
    }

    public static AnimPoint importAnimPoint(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimPoint();
        importAnimValues(sc, timeKeypoints, ImEx::importPoint, anim::add);
        return anim;
    }

    public static AnimDimension importAnimDimension(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimDimension();
        importAnimValues(sc, timeKeypoints, ImEx::importDimension, anim::add);
        return anim;
    }

    public static AnimDouble importAnimDouble(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimDouble();
        importAnimValues(sc, timeKeypoints, ImEx::importDouble, anim::add);
        return anim;
    }

    public static AnimInt importAnimInt(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        var anim = new AnimInt();
        importAnimValues(sc, timeKeypoints, ImEx::importInt, anim::add);
        return anim;
    }

    public static GraphicRoot importRoot(Scanner sc) {
        HashMap<String, TimeKeypoint> timeKeypoints = new HashMap<>();
        HashMap<String, Set<String>> timeAwaits = new HashMap<>();
        HashMap<String, PaletteValue> paletteValues = new HashMap<>();
        Palette palette = new Palette();
        List<GraphicLayer> layers = new ArrayList<>();
        outer: while (sc.hasNext()) {
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
                layers.add(importLayer(sc, timeKeypoints, paletteValues));
                break;
            case "###":
                break outer;
            }
        }
        return new GraphicRoot(new ArrayList<>(timeKeypoints.values()), palette, layers);
    }

    public static void importTimeKeypoint(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, Set<String>> timeAwaits) {
        String id = importStringId(sc);
        double offset = importDouble(sc);
        TimeKeypoint tkp = new TimeKeypoint(id, offset, null, "");
        timeKeypoints.put(id, tkp);
        if (timeAwaits.containsKey(id)) {
            for (String awaitId : timeAwaits.get(id)) {
                timeKeypoints.get(awaitId).setReference(tkp, null);
            }
            timeAwaits.remove(id);
        }
        if (sc.hasNext("NULL")) {
            sc.next();
        } else {
            String referenceId = importStringId(sc);
            if (timeKeypoints.containsKey(referenceId)) {
                tkp.setReference(timeKeypoints.get(referenceId), null);
            } else {
                timeAwaits.computeIfAbsent(referenceId, k -> new HashSet<>()).add(id);
            }
        }
        tkp.label = importUserString(sc);

    }

    public static PaletteValue importPaletteValue(Scanner sc) {
        String id = importStringId(sc);
        String color = importHexColor(sc);
        String label = importUserString(sc);
        return new PaletteValue(id, color, label);
    }

    public static GraphicLayer importLayer(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, PaletteValue> paletteValues) {
        String layerName = importUserString(sc);
        AnimBoolean visible = importAnimBoolean(sc, timeKeypoints);
        AnimPoint translate = importAnimPoint(sc, timeKeypoints);
        AnimPoint rotateOrigin = importAnimPoint(sc, timeKeypoints);
        AnimDouble rotate = importAnimDouble(sc, timeKeypoints);
        List<GraphicObject> objects = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
            case "PATH2D":
                objects.add(importPath2D(sc, timeKeypoints, paletteValues));
                break;
            case "CIRCLE":
                objects.add(importCircle(sc, timeKeypoints, paletteValues));
                break;
            case "ELLIPSE":
                objects.add(importEllipse(sc, timeKeypoints, paletteValues));
                break;
            case "IMAGE":
                objects.add(importImage(sc, timeKeypoints));
                break;
            case "STRING":
                objects.add(importGString(sc, timeKeypoints));
                break;
            }
        }
        return new GraphicLayer(layerName, visible, translate, rotateOrigin, rotate, objects);
    }

    public static Palette importPalette(Scanner sc, HashMap<String, PaletteValue> paletteValues) {
        Palette palette = new Palette();
        while (sc.hasNext()) {
            int x = importInt(sc);
            int y = importInt(sc);
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

    public static Path2DLine importPath2DLine(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        AnimPoint pNext = importAnimPoint(sc, timeKeypoints);
        return new Path2DLine(pNext);
    }

    public static Path2DBezier importPath2DBezier(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        AnimPoint pNext = importAnimPoint(sc, timeKeypoints);
        List<AnimPoint> morePoints = new ArrayList<>();
        while (true) {
            morePoints.add(importAnimPoint(sc, timeKeypoints));
            if (sc.hasNext("END")) {
                sc.next();
                break;
            }
        }
        return new Path2DBezier(pNext, morePoints);
    }

    public static GraphicPath2D importPath2D(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, PaletteValue> paletteValues) {
        AnimBoolean stroke = importAnimBoolean(sc, timeKeypoints);
        AnimColor strokeColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimInt thickness = importAnimInt(sc, timeKeypoints);
        AnimBoolean fill = importAnimBoolean(sc, timeKeypoints);
        AnimColor fillColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimBoolean closed = importAnimBoolean(sc, timeKeypoints);
        AnimPoint p1 = importAnimPoint(sc, timeKeypoints);
        List<Path2DData> data = new ArrayList<>();
        while (true) {
            String type = sc.next();
            if (type.equals("END")) {
                break;
            }
            switch (type) {
            case "LINE":
                data.add(importPath2DLine(sc, timeKeypoints));
                break;
            case "BEZIER":
                data.add(importPath2DBezier(sc, timeKeypoints));
                break;
            }
        }
        return new GraphicPath2D(stroke, strokeColor, thickness, fill, fillColor, closed, p1, data);
    }

    public static GraphicCircle importCircle(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, PaletteValue> paletteValues) {
        AnimBoolean stroke = importAnimBoolean(sc, timeKeypoints);
        AnimColor strokeColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimInt thickness = importAnimInt(sc, timeKeypoints);
        AnimBoolean fill = importAnimBoolean(sc, timeKeypoints);
        AnimColor fillColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimPoint center = importAnimPoint(sc, timeKeypoints);
        AnimInt radius = importAnimInt(sc, timeKeypoints);
        return new GraphicCircle(stroke, strokeColor, thickness, fill, fillColor, center, radius);
    }

    public static GraphicEllipse importEllipse(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints,
            HashMap<String, PaletteValue> paletteValues) {
        AnimBoolean stroke = importAnimBoolean(sc, timeKeypoints);
        AnimColor strokeColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimInt thickness = importAnimInt(sc, timeKeypoints);
        AnimBoolean fill = importAnimBoolean(sc, timeKeypoints);
        AnimColor fillColor = importAnimColor(sc, timeKeypoints, paletteValues);
        AnimPoint center = importAnimPoint(sc, timeKeypoints);
        AnimInt radiusA = importAnimInt(sc, timeKeypoints);
        AnimInt radiusB = importAnimInt(sc, timeKeypoints);
        return new GraphicEllipse(stroke, strokeColor, thickness, fill, fillColor, center, radiusA, radiusB);
    }

    public static GraphicImage importImage(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        String filePath = importUserString(sc);
        AnimPoint origin = importAnimPoint(sc, timeKeypoints);
        AnimDimension size = importAnimDimension(sc, timeKeypoints);
        AnimDouble opacity = importAnimDouble(sc, timeKeypoints);
        return new GraphicImage(filePath, origin, size, opacity);
    }

    public static GraphicString importGString(Scanner sc, HashMap<String, TimeKeypoint> timeKeypoints) {
        AnimString text = importAnimString(sc, timeKeypoints);
        AnimString fontFace = importAnimString(sc, timeKeypoints);
        AnimInt fontSize = importAnimInt(sc, timeKeypoints);
        AnimColor strokeColor = importAnimColor(sc, timeKeypoints, new HashMap<>());
        AnimPoint position = importAnimPoint(sc, timeKeypoints);
        AnimInt alignment = importAnimInt(sc, timeKeypoints);
        return new GraphicString(text, fontFace, fontSize, strokeColor, position, alignment);
    }

}
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class PreloadedData {
    static void thing() {
        for (int i = 0; i < 20; i++) {
            System.out.println("wow");
        }
    }

    static void thing2() {
        for (int i = 0; i < 20; i++) {
            System.out.println("wow");
        }
    }

    static void thing3() {
        for (int i = 0; i < 20; i++) {
            System.out.println("wow");
        }
    }

    static void thing4() {
        for (int i = 0; i < 20; i++) {
            System.out.println("wow");
        }
    }

    static GraphicRoot create() {
        var tkp_0qwe = new TimeKeypoint("0qwe", 0.0, null, "origin");
        var tkp_1asd = new TimeKeypoint("1asd", 10.0, tkp_0qwe, "sunrise");
        var tkp_2zxc = new TimeKeypoint("2zxc", 2.0, tkp_0qwe, "moveitmoveit");
        var tkp_3muk = new TimeKeypoint("3muk", 1.0, tkp_2zxc, "rota");
        var tkp_4vwr = new TimeKeypoint("4vwr", 2.0, tkp_3muk, "rotato");
        var tkp_5rty = new TimeKeypoint("5rty", 6.0, null, "disappearance");
        var pv_0fgh = new PaletteValue("0fgh", "#113", "bgDark");
        var pv_1vbn = new PaletteValue("1vbn", "#374A71", "bgSunrise");
        var pv_2uio = new PaletteValue("2uio", "#2266AA", "path thing");
        List<TimeKeypoint> timeKeypoints = new ArrayList<>();
        timeKeypoints.add(tkp_0qwe);
        timeKeypoints.add(tkp_1asd);
        timeKeypoints.add(tkp_2zxc);
        timeKeypoints.add(tkp_3muk);
        timeKeypoints.add(tkp_4vwr);
        timeKeypoints.add(tkp_5rty);
        Palette palette = new Palette().set(0, 0, pv_0fgh).set(1, 0, pv_1vbn).set(0, 1, pv_2uio);
        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("bg", new AnimBoolean().add(tkp_0qwe, true, EasingFunction.snap),
                new AnimPoint().add(tkp_0qwe, new Point(), EasingFunction.snap),
                new AnimPoint().add(tkp_0qwe, new Point(), EasingFunction.snap),
                new AnimDouble().add(tkp_0qwe, 0.0, EasingFunction.snap))
                        .add(new GraphicPath2D(new AnimBoolean().add(tkp_0qwe, false, EasingFunction.snap),
                                new AnimColor().add(tkp_0qwe, "#FFFFFF", EasingFunction.snap),
                                new AnimInt().add(tkp_0qwe, 1, EasingFunction.snap),
                                new AnimBoolean().add(tkp_0qwe, true, EasingFunction.snap),
                                new AnimColor().add(tkp_0qwe, pv_0fgh, EasingFunction.easeInOutPower2).add(tkp_1asd,
                                        pv_1vbn, EasingFunction.easeInOutPower2),
                                new AnimBoolean().add(tkp_0qwe, false, EasingFunction.snap),
                                new AnimPoint().add(tkp_0qwe, new Point(-325, -325), EasingFunction.snap),
                                new Path2DLine(
                                        new AnimPoint().add(tkp_0qwe, new Point(325, -325), EasingFunction.snap)),
                                new Path2DLine(new AnimPoint().add(tkp_0qwe, new Point(325, 325), EasingFunction.snap)),
                                new Path2DLine(
                                        new AnimPoint().add(tkp_0qwe, new Point(-325, 325), EasingFunction.snap))))

        );
        instructions.add(new GraphicLayer("third point six swing",
                new AnimBoolean().add(tkp_0qwe, true, EasingFunction.snap).add(tkp_5rty, false, EasingFunction.snap)
                        .add(tkp_1asd, true, EasingFunction.snap),
                new AnimPoint().add(tkp_0qwe, new Point(), EasingFunction.easeInOutPower2).add(tkp_2zxc,
                        new Point(50, 50), EasingFunction.easeInOutPower2),
                new AnimPoint().add(tkp_0qwe, new Point(), EasingFunction.snap),
                new AnimDouble().add(tkp_3muk, 0.0, EasingFunction.easeInOutPower3).add(tkp_4vwr, 360.0,
                        EasingFunction.linear))
                                .add(new GraphicPath2D(new AnimBoolean().add(tkp_0qwe, true, EasingFunction.snap),
                                        new AnimColor().add(tkp_0qwe, "#000000", EasingFunction.snap),
                                        new AnimInt().add(tkp_0qwe, 1, EasingFunction.snap),
                                        new AnimBoolean().add(tkp_0qwe, true, EasingFunction.snap),
                                        new AnimColor().add(tkp_0qwe, "#333333", EasingFunction.snap),
                                        new AnimBoolean().add(tkp_0qwe, false, EasingFunction.snap),
                                        new AnimPoint().add(tkp_0qwe, new Point(-200, -200), EasingFunction.snap),
                                        new Path2DBezier(
                                                new AnimPoint().add(tkp_0qwe, new Point(-250, -170),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(-150, -170),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(-210, -140),
                                                        EasingFunction.easeInOutPower2)),
                                        new Path2DBezier(
                                                new AnimPoint().add(tkp_0qwe, new Point(-200, 0),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(-150, -250),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(0, -150),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(150, -250),
                                                        EasingFunction.easeInOutPower2),
                                                new AnimPoint().add(tkp_0qwe, new Point(200, -150),
                                                        EasingFunction.easeInOutPower2))))
                                .add(new GraphicImage("null",
                                        new AnimPoint().add(tkp_0qwe, new Point(-290, -290), EasingFunction.snap),
                                        new AnimDimension().add(tkp_0qwe, new Dimension(20, 20), EasingFunction.snap),
                                        new AnimDouble().add(tkp_0qwe, 1.0, EasingFunction.snap)))
                                .add(new GraphicCircle(new AnimColor().add(tkp_0qwe, pv_2uio, EasingFunction.snap),
                                        new AnimInt().add(tkp_0qwe, 1, EasingFunction.snap),
                                        new AnimPoint().add(tkp_0qwe, new Point(-220, -260), EasingFunction.snap),
                                        new AnimInt().add(tkp_0qwe, 20, EasingFunction.snap)))

        );
        return new GraphicRoot(timeKeypoints, palette, instructions);
    }
}

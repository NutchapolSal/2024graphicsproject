import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaCombiner {

    public static String generateAnnotation(String className) {
        return generateAnnotation(className, ZonedDateTime.now());
    }

    public static String generateAnnotation(String className, ZonedDateTime time) {
        return "@Generated(value = \"" + (className) + "\", date = \""
                + (time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) + "\")";
    }

    public static void main(String[] args) {
        var className = JavaCombiner.class.getSimpleName();
        var folder = new File(".");
        var fileList = folder.listFiles(
                (f) -> f.isFile() && f.getName().endsWith(".java") && !f.getName().equals(className + ".java"));

        System.out.println(fileList.length + " files");

        Set<String> imports = new HashSet<>();
        imports.add("import javax.annotation.processing.Generated;");
        List<List<String>> body = new ArrayList<>();

        for (var file : fileList) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                boolean isMain = file.getName().equals("Main.java");
                List<String> bodyPart = new ArrayList<>();
                boolean importSection = true;
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (importSection) {
                        if (line.equals("")) {
                            continue;
                        }

                        if (line.startsWith("import")) {
                            imports.add(line);
                            continue;
                        } else {
                            importSection = false;
                        }
                    }
                    if (line.startsWith("public class ") && !line.startsWith("public class Main ")) {
                        System.out.println("non main public class: " + line);
                        bodyPart.add(line.substring(7));
                    } else {
                        bodyPart.add(line);
                    }

                }
                if (isMain) {
                    bodyPart.add(0, generateAnnotation(className));
                    body.add(0, bodyPart);
                } else {
                    body.add(bodyPart);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var lines = Stream.concat(imports.stream().sorted(), body.stream().reduce(new ArrayList<String>().stream(),
                (a, b) -> Stream.concat(Stream.concat(a, Stream.of("")), b.stream()), (a, b) -> Stream.concat(a, b)))
                .collect(Collectors.toList());

        try {
            Files.write(Path.of("Main.java.out"), lines, StandardCharsets.UTF_8);
            System.out.println("output at Main.java.out");
            System.out.println(lines.size() + " lines");
            System.out.println((lines.stream().mapToInt(String::length).sum() + lines.size() * 2)
                    + " characters (windows line endings)");
            System.out.println(
                    (lines.stream().mapToInt(String::length).sum() + lines.size()) + " characters (unix line endings)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaCombiner {
    public static void main(String[] args) {
        var folder = new File(".");
        var fileList = folder.listFiles(
                (f) -> f.isFile() &&
                        f.getName().endsWith(".java") &&
                        !f.getName().equals(JavaCombiner.class.getSimpleName() + ".java"));

        System.out.println(fileList.length + " files");

        Set<String> imports = new HashSet<>();
        List<String> body = new ArrayList<>();

        for (var file : fileList) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
                            body.add("");
                        }
                    }
                    body.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var lines = Stream.concat(imports.stream().sorted(), body.stream()).collect(Collectors.toList());

        try {
            Files.write(Paths.get("Main.java.out"), lines, StandardCharsets.UTF_8);
            System.out.println("output at Main.java.out");
            System.out.println(lines.size() + " lines");
            System.out.println(
                    (lines.stream().mapToInt(String::length).sum() + lines.size() * 2)
                            + " characters (windows line endings)");
            System.out.println(
                    (lines.stream().mapToInt(String::length).sum() + lines.size()) + " characters (unix line endings)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

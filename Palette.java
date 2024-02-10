import java.util.HashMap;

class Palette {
    private HashMap<Integer, HashMap<Integer, PaletteValue>> values = new HashMap<>();

    public Palette set(int x, int y, PaletteValue value) {
        values.computeIfAbsent(y, k -> new HashMap<>()).put(x, value);
        return this;
    }

}

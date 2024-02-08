import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Palette implements Exportable {
    private HashMap<Integer, HashMap<Integer, PaletteValue>> values = new HashMap<>();

    public Optional<PaletteValue> get(int x, int y) {
        if (values.containsKey(y)) {
            var row = values.get(y);
            return Optional.ofNullable(row.get(x));
        }
        return Optional.empty();
    }

    public Palette set(int x, int y, PaletteValue value) {
        values.computeIfAbsent(y, k -> new HashMap<>()).put(x, value);
        return this;
    }

    static class SparsePaletteSlot {
        public final int x;
        public final int y;
        public final PaletteValue value;

        public SparsePaletteSlot(int x, int y, PaletteValue value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    public Iterable<SparsePaletteSlot> sparseIterator() {
        return () -> new Iterator<SparsePaletteSlot>() {
            private Iterator<Integer> yIterator = values.keySet().iterator();
            private Iterator<Integer> xIterator = null;
            private int y = 0;
            private int x = 0;

            @Override
            public boolean hasNext() {
                if (xIterator != null && xIterator.hasNext()) {
                    return true;
                }

                if (yIterator.hasNext()) {
                    return true;
                }

                return false;

            }

            @Override
            public SparsePaletteSlot next() {
                if (xIterator == null || !xIterator.hasNext()) {
                    y = yIterator.next();
                    xIterator = values.get(y).keySet().iterator();
                }
                x = xIterator.next();
                return new SparsePaletteSlot(x, y, values.get(y).get(x));
            }
        };
    }

    static class PaletteSlot {
        public final int x;
        public final int y;
        public final Optional<PaletteValue> value;

        public PaletteSlot(int x, int y, Optional<PaletteValue> value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    public Iterable<PaletteSlot> iterator() {
        return () -> new Iterator<PaletteSlot>() {
            private int xMin = Math.min(values.keySet().stream().min(Integer::compareTo).orElse(0), 0);
            private int xMax = values.keySet().stream().max(Integer::compareTo).orElse(0);
            private int yMin = Math.min(values.values().stream()
                    .mapToInt(row -> row.keySet().stream().min(Integer::compareTo).orElse(0)).min().orElse(0), 0);
            private int yMax = values.values().stream()
                    .mapToInt(row -> row.keySet().stream().max(Integer::compareTo).orElse(0)).max().orElse(0);
            private int x = xMin;
            private int y = yMin;

            @Override
            public boolean hasNext() {
                return x <= xMax && y <= yMax;
            }

            @Override
            public PaletteSlot next() {
                var value = get(x, y);
                var slot = new PaletteSlot(x, y, value);
                if (x < xMax) {
                    x++;
                } else {
                    y++;
                    x = xMin;
                }
                return slot;
            }
        };
    }

    private Stream<SparsePaletteSlot> sparseStreamSorted() {
        return StreamSupport.stream(this.sparseIterator().spliterator(), true)
                .sorted(Comparator.comparing(slot -> slot.value.id));
    }

    @Override
    public String exportString() {
        return sparseStreamSorted().map(slot -> {
            var sb2 = new StringBuilder();
            sb2.append(ImEx.exportString(slot.x));
            sb2.append(" ");
            sb2.append(ImEx.exportString(slot.y));
            sb2.append(" ");
            sb2.append(ImEx.exportString(slot.value));
            return sb2.toString();
        }).collect(Collectors.joining("\n", "PALETTE\n", "\nEND"));
    }

    public String exportInitStringPaletteValues() {
        return sparseStreamSorted().map(slot -> slot.value.exportInitString()).collect(Collectors.joining());
    }

    @Override
    public String exportCode() {
        return sparseStreamSorted().map(slot -> {
            var sb2 = new StringBuilder();
            sb2.append(".set(");
            sb2.append(ImEx.exportCode(slot.x));
            sb2.append(", ");
            sb2.append(ImEx.exportCode(slot.y));
            sb2.append(", ");
            sb2.append(ImEx.exportCode(slot.value));
            sb2.append(")");
            return sb2.toString();
        }).collect(Collectors.joining("\n", "new Palette()\n", ""));
    }

}

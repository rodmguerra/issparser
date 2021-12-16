package io.github.rodmguerra.issparser.model.tiles;


import java.util.stream.Stream;

public class TeamNameInGame {
    private final Color[][] matrix;

    public TeamNameInGame(Color[][] matrix) {
        this.matrix = matrix;
    }

    public static Color[][] newColorArray() {
        return new Color[16][24];
    }

    public enum Color {
        COLOR_1((byte) 0b01, "Color 1"),
        COLOR_2((byte) 0b10, "Color 2"),
        COLOR_3((byte) 0b11, "Color 3"),
        TRANSPARENT((byte) 0b00, "Transparent");

        private final byte code;
        private final String name;

        private Color(byte code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Color forCode(byte code) {
            return Stream.of(Color.values()).filter(color -> color.code == code).findFirst().get();
        }

        public byte getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static Color at(int colorIndex) {
            for (Color color : Color.values()) {
                if(color.ordinal() == colorIndex) {
                    return color;
                }
            }
            return null;
        }


        @Override
        public String toString() {
            if(ordinal() == 3) return " ";
            if(ordinal() == 0) return ".";
            if(ordinal() == 1) return "*";
            if(ordinal() == 2) return "#";
            return "" + ordinal();
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Color[] colors : matrix) {
            for (Color color : colors) {
                out.append(color).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public Color[][] getMatrix() {
        return matrix;
    }
}

package io.github.rodmguerra.issparser.model;


import java.util.Arrays;
import java.util.stream.Stream;

public class FlagDesign {
    private final Color[][] matrix;

    public FlagDesign(Color[][] matrix) {
        this.matrix = matrix;
    }

    public static Color[][] newColorArray() {
        return new Color[16][24];
    }

    public enum Color {
        COLOR_1((byte) 0b1100),
        COLOR_2((byte) 0b1101),
        COLOR_3((byte) 0b1110),
        COLOR_4((byte) 0b1111),
        TRANSPARENT((byte) 0b0000);

        private final byte code;

        private Color(byte code) {
            this.code = code;
        }

        public static Color forCode(byte code) {
            return Stream.of(Color.values()).filter(color -> color.code == code).findFirst().get();
        }

        public byte getCode() {
            return code;
        }

        /*
        @Override
        public String toString() {
            //return this.equals(TRANSPARENT) ? " " : String.valueOf(this.code - 11);
            return this.
        }      */
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

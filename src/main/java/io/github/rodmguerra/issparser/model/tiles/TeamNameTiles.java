package io.github.rodmguerra.issparser.model.tiles;


import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.stripAccents;

public class TeamNameTiles {
    public static final int ROWS = 8;
    public static final int COLS = 32;
    private final Color[][] matrix;

    public TeamNameTiles(Color[][] matrix) {
        this.matrix = matrix;
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
                if (color.ordinal() == colorIndex) {
                    return color;
                }
            }
            return null;
        }


        @Override
        public String toString() {

            if (ordinal() == 0) return "§";
            if (ordinal() == 1) return "~";
            if (ordinal() == 2) return " ";
            if (ordinal() == 3) return ".";
            return "" + ordinal();
        }

        public static Color deserialize(char c) {
            if (c == '§') return COLOR_1;
            if (c == '~') return COLOR_2;
            if (c == ' ') return COLOR_3;
            if (c == '.') return TRANSPARENT;
            throw new IllegalArgumentException("Invalid color for char: " + c);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Color[] colors : matrix) {
            for (Color color : colors) {
                out.append(color).append("");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public Color[][] getMatrix() {
        return matrix;
    }

    private static Color[][] forLetter(char letter) {
        String s = stripAccents("" + letter).toUpperCase().replaceAll("[^A-Z0-9\\. ]", "A");
        String fileName = s.matches("[A-Z]") ? s : s.equals(".") ? "dot" : s.equals(" ") ? "space": s;
        URL resource = Resources.getResource("teamnametiles/" + fileName + ".txt");
        CharSource source = Resources.asCharSource(resource, Charsets.UTF_8);
        List<String> strings = null;
        try {
            strings = source.readLines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int cols = strings.get(0).length();
        Color[][] matrix = new Color[ROWS][cols];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.println(strings.get(i).charAt(j));
                matrix[i][j] = Color.deserialize(strings.get(i).charAt(j));
            }
        }
        return matrix;
    }


    private static List<Color[][]> letters(String text) {
        List<Color[][]> letters = new ArrayList<>(text.length());
        for (int i = 0; i < text.length(); i++) {
            letters.add(forLetter(text.charAt(i)));
        }
        return letters;
    }

    public static TeamNameTiles forText(String text) {
        System.out.println(text);
        List<Color[][]> letters = letters(text);
        Color[][] matrix = empty();
        int cols = cols(letters);
        System.out.println("cols = " + cols);
        int position = cols > COLS ? 0 : (COLS - cols) / 2;
        for (Color[][] letter : letters) {
            position = addLetter(matrix, letter, position);
        }
        return new TeamNameTiles(matrix);
    }

    private static int cols(List<Color[][]> letters) {
        int cols = 0;
        int i = 0;
        for (Color[][] letter : letters) {
            cols += letter[0].length;
            if (i++ > 0) cols--;
        }
        return cols;
    }

    private static Color[][] empty() {
        Color[][] matrix = new Color[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = Color.TRANSPARENT;
            }
        }
        return matrix;
    }

    private static int addLetter(Color[][] matrix, Color[][] letter, int position) {
        for (int i = 0; i < letter.length; i++) {
            for (int j = 0; j < letter[i].length; j++) {
                if (j + position < matrix[i].length) {
                    Color color = max(matrix[i][j + position], letter[i][j]);
                    matrix[i][j + position] = color;
                }
            }
        }
        return position + letter[0].length - 1;
    }

    private static Color max(Color color1, Color color2) {
        if(color1.code > color2.code) return color1;
        else return color2;
    }

    public static void main(String[] args) {
        System.out.println(TeamNameTiles.forText("FRANCA18"));
    }
}

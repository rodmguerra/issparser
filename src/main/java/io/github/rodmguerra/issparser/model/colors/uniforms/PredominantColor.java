package io.github.rodmguerra.issparser.model.colors.uniforms;

public enum PredominantColor {
    WHITE("White"), BLUE("Blue"), RED("Red"), YELLOW("Yellow"), GREEN("Green");

    private final String name;
    PredominantColor(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PredominantColor at(int ordinal) {
        return PredominantColor.values()[ordinal];
    }
}

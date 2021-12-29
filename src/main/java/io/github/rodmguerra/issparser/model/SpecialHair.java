package io.github.rodmguerra.issparser.model;


public enum SpecialHair {
    REGULAR("Regular"), BLOND("Blond"), LIGHT_BROWN("Light brown"), DARK_BROWN("Dark brown"), BLACK("Black");

    private final String name;

    private SpecialHair(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }

    public static SpecialHair at(int index) {
        for (SpecialHair color : SpecialHair.values()) {
            if (color.ordinal() == index) return color;
        }
        throw new IllegalArgumentException("SpecialHair " + index);
    }
}

package io.github.rodmguerra.issparser.model;


public enum SpecialSkin {
    REGULAR("Regular"),
    WHITE("White"),
    BROWN("Brown"),
    BLACK("Black");

    private final String name;
    private SpecialSkin(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static SpecialSkin at(int index) {
        for (SpecialSkin color : SpecialSkin.values()) {
            if(color.ordinal() == index) return color;
        }
        throw new IllegalArgumentException("SpecialSkin " + index );
    }
}

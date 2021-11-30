package issparser.kits.model;

public enum ColorType {
    WHITE("White"), BLUE("Blue"), RED("Red"), YELLOW("Yellow"), GREEN("Green");

    private final String name;
    ColorType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ColorType at(int ordinal) {
        return ColorType.values()[ordinal];
    }
}

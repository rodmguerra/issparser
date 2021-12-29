package io.github.rodmguerra.issparser.model;

public enum HairStyle {

    SHORT("Short hair"),
    CURLY("Curly hair"),
    LONG_CURLY("Long curly"),
    LONG_WITH_BEARD("Long with beard"),
    LONG_STRAIGHT("Long straight"),
    DREADLOCKS("Dreadlocks"),
    AFRO("Afro"),
    PONYTAIL("Ponytail"),
    BALD("Bald"),
    MID_LENGTH("Mid length"),
    LONG_WITH_RIBBON("Long with ribbon");

    private final String text;

    private HairStyle(String text) {
        this.text = text;
    }

    public static HairStyle fromByte(byte code) {
        int ordinal = code % 0x10;
        for (HairStyle style : HairStyle.values()) {
            if (style.ordinal() == ordinal) {
                return style;
            }
        }
        throw new IllegalArgumentException("Hair style code = " + code);
    }

    @Override
    public String toString() {
        return text;
    }
}

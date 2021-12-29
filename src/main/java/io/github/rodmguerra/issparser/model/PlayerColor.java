package io.github.rodmguerra.issparser.model;

public enum PlayerColor {
    NORMAL("Normal"),
    SPECIAL("Special");

    private final String text;

    private PlayerColor(String text) {
        this.text = text;
    }

    public static PlayerColor at(int index) {
        for (PlayerColor color : PlayerColor.values()) {
            if(color.ordinal() == index) return color;
        }
        throw new IllegalArgumentException("PlayerColor " + index );
    }

    @Override
    public String toString() {
        return text;
    }
}

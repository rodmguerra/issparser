package io.github.rodmguerra.issparser.model;

public class PlayerBasic {
    private final String name;
    private final int number;
    private final PlayerColor color;
    private final HairStyle hairStyle;

    public PlayerBasic(String name, int number, PlayerColor color, HairStyle hairStyle) {
        this.name = name;
        this.number = number;
        this.color = color;
        this.hairStyle = hairStyle;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public PlayerColor getColor() {
        return color;
    }

    public HairStyle getHairStyle() {
        return hairStyle;
    }
}

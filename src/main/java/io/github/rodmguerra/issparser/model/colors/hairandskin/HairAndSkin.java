package io.github.rodmguerra.issparser.model.colors.hairandskin;

import io.github.rodmguerra.issparser.model.colors.ColoredPart;

public class HairAndSkin {
    private final ColoredPart hair;
    private final ColoredPart skin;

    public HairAndSkin(ColoredPart hair, ColoredPart skin) {
        this.hair = hair;
        this.skin = skin;
    }

    public ColoredPart getHair() {
        return hair;
    }

    public ColoredPart getSkin() {
        return skin;
    }
}

package io.github.rodmguerra.issparser.model;


import io.github.rodmguerra.issparser.model.colors.ColoredPart;

public class Flag {
    private final FlagDesign design;
    private final ColoredPart colors;

    public Flag(FlagDesign design, ColoredPart colors) {
        this.design = design;
        this.colors = colors;
    }

    public FlagDesign getDesign() {
        return design;
    }

    public ColoredPart getColors() {
        return colors;
    }
}

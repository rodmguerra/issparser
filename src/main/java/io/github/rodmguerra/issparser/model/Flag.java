package io.github.rodmguerra.issparser.model;


import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.util.List;

public class Flag {
    private final FlagDesign design;
    private final ColoredPart colors;
    private final List<RomHandler.Team> teamsUsing;

    public Flag(FlagDesign design, ColoredPart colors, List<RomHandler.Team> teamsUsing) {
        this.design = design;
        this.colors = colors;
        this.teamsUsing = teamsUsing;
    }

    public FlagDesign getDesign() {
        return design;
    }

    public ColoredPart getColors() {
        return colors;
    }

    public List<RomHandler.Team> getTeamsUsing() {
        return teamsUsing;
    }
}

package io.github.rodmguerra.issparser.model;


import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class Flag {
    private final FlagDesign design;
    private final ColoredPart colors;
    private final Set<RomHandler.Team> teamsUsing;

    public Flag(FlagDesign design, ColoredPart colors, Iterable<RomHandler.Team> teamsUsing) {
        this.design = design;
        this.colors = colors;
        this.teamsUsing = newLinkedHashSet(teamsUsing);
    }

    public FlagDesign getDesign() {
        return design;
    }

    public ColoredPart getColors() {
        return colors;
    }

    public Set<RomHandler.Team> getTeamsUsing() {
        return teamsUsing;
    }
}

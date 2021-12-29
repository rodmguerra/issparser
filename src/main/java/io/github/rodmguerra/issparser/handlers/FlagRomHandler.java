package io.github.rodmguerra.issparser.handlers;


import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.tiles.FlagDesignRomHandler;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.io.IOException;

public class FlagRomHandler implements RomHandler<Flag> {

    private final FlagDesignRomHandler designHandler;
    private final RomHandler<ColoredPart> colorHandler;

    public FlagRomHandler(FlagDesignRomHandler designHandler, RomHandler<ColoredPart> colorHandler) {
        this.designHandler = designHandler;
        this.colorHandler = colorHandler;
    }

    @Override
    public Flag readFromRomAt(Team team) throws IOException {
        return new Flag(designHandler.readFromRomAt(team), colorHandler.readFromRomAt(team), designHandler.teamsSharing(team));
    }

    @Override
    public void writeToRomAt(Team team, Flag input) throws IOException {
        designHandler.writeToRomAt(team, input.getTeamsUsing(), input.getDesign());
        colorHandler.writeToRomAt(team, input.getColors());
    }
}

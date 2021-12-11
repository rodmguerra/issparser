package io.github.rodmguerra.issparser.handlers;


import com.google.common.collect.Iterables;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.tiles.FlagDesignRomHandler;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.google.common.collect.Iterables.toArray;

public class FlagRomHandler implements RomHandler<Flag> {

    private final File rom;
    private final FlagDesignRomHandler designHandler;
    private final RomHandler<ColoredPart> colorHandler;

    public FlagRomHandler(File rom, FlagDesignRomHandler designHandler, RomHandler<ColoredPart> colorHandler) {
        this.rom = rom;
        this.designHandler = designHandler;
        this.colorHandler = colorHandler;
    }

    @Override
    public Map<Team, Flag> readFromRom() throws IOException {
        return null;
    }

    @Override
    public void writeToRom(Map<Team, ? extends Flag> input) throws IOException {
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

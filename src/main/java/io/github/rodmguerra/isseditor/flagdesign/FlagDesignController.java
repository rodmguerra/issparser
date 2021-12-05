package io.github.rodmguerra.isseditor.flagdesign;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.isseditor.team.TeamView;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.FlagRomHandler;
import io.github.rodmguerra.issparser.handlers.colors.FlagColorRomHandler;
import io.github.rodmguerra.issparser.handlers.tiles.FlagDesignRomHandler;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.io.File;

public class FlagDesignController extends AbstractTeamController<Flag> {
    public FlagDesignController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<Flag> romHandlerFor(File rom) {
        return new FlagRomHandler(rom, new FlagDesignRomHandler(rom), new FlagColorRomHandler(rom));
    }
}

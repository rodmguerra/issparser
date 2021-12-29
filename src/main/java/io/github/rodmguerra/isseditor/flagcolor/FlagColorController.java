package io.github.rodmguerra.isseditor.flagcolor;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.isseditor.team.TeamView;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.colors.FlagColorRomHandler;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import java.io.File;

public class FlagColorController extends AbstractTeamController<ColoredPart> {
    public FlagColorController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<ColoredPart> romHandlerFor(File rom) {
        return new FlagColorRomHandler(rom);
    }
}

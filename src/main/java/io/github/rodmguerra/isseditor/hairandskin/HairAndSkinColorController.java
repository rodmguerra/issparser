package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.isseditor.team.TeamView;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.colors.HairAndSkinRomHandler;
import io.github.rodmguerra.issparser.handlers.texts.PlayerNameRomHandler;
import io.github.rodmguerra.issparser.model.colors.hairandskin.HairAndSkin;
import io.github.rodmguerra.issparser.model.colors.hairandskin.TeamHairAndSkin;

import java.io.File;

public class HairAndSkinColorController extends AbstractTeamController<TeamHairAndSkin> {
    public HairAndSkinColorController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<TeamHairAndSkin> romHandlerFor(File rom) {
        return new HairAndSkinRomHandler(rom);
    }
}

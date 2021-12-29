package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.isseditor.team.TeamView;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.HairAndSkinComboRomHandler;
import io.github.rodmguerra.issparser.handlers.colors.HairAndSkinRomHandler;
import io.github.rodmguerra.issparser.handlers.texts.SpecialHairRomHandler;
import io.github.rodmguerra.issparser.handlers.texts.SpecialSkinRomHandler;
import io.github.rodmguerra.issparser.model.HairAndSkinCombo;

import java.io.File;

public class HairAndSkinColorController extends AbstractTeamController<HairAndSkinCombo> {
    public HairAndSkinColorController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<HairAndSkinCombo> romHandlerFor(File rom) {
        return new HairAndSkinComboRomHandler(
                new HairAndSkinRomHandler(rom),
                new SpecialHairRomHandler(rom),
                new SpecialSkinRomHandler(rom));
    }
}

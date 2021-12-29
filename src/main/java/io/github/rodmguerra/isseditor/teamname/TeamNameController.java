package io.github.rodmguerra.isseditor.teamname;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.TeamNameRomHandler;
import io.github.rodmguerra.issparser.model.TeamName;
import io.github.rodmguerra.issparser.handlers.texts.TeamNameTextRomHandler;
import io.github.rodmguerra.issparser.handlers.tiles.TeamNameTilesRomHandler;

import java.io.File;

public class TeamNameController extends AbstractTeamController<TeamName> {

    public TeamNameController(Router router, TeamNamePage view) {
        super(router, view);
    }

    @Override
    protected RomHandler<TeamName> romHandlerFor(File rom) {
        return new TeamNameRomHandler(new TeamNameTextRomHandler(rom), new TeamNameTilesRomHandler(rom));
    }




}

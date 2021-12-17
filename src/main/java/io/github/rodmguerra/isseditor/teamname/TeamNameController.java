package io.github.rodmguerra.isseditor.teamname;

import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.TeamNameRomHandler;
import io.github.rodmguerra.issparser.handlers.texts.TeamName;
import io.github.rodmguerra.issparser.handlers.texts.TeamNameInMenuRomHandler;
import io.github.rodmguerra.issparser.handlers.tiles.TeamNameInGameRomHandler;

import java.io.File;

public class TeamNameController extends AbstractTeamController<TeamName> {

    public TeamNameController(Router router, TeamNamePage view) {
        super(router, view);
    }

    @Override
    protected RomHandler<TeamName> romHandlerFor(File rom) {
        return new TeamNameRomHandler(new TeamNameInMenuRomHandler(rom), new TeamNameInGameRomHandler(rom));
    }




}

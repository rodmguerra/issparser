package issparser;

import issparser.commons.RomHandler;
import issparser.playernames.PlayerNameRomHandler;

import java.io.File;

public class PlayerNameController extends AbstractTeamController<Iterable<String>> {
    public PlayerNameController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<Iterable<String>> romHandlerFor(File rom) {
        return new PlayerNameRomHandler(rom);
    }
}

package issparser;

import issparser.commons.RomHandler;
import issparser.kits.KitRomHandler;
import issparser.kits.model.TeamKits;

import java.io.File;

public class UniformController extends AbstractTeamController<TeamKits> {
    public UniformController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<TeamKits> romHandlerFor(File rom) {
        return new KitRomHandler(rom);
    }
}

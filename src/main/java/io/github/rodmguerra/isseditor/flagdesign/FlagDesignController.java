package io.github.rodmguerra.isseditor.flagdesign;

import com.google.common.collect.Iterables;
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
import java.io.IOException;

import static com.google.common.collect.Iterables.toArray;

public class FlagDesignController extends AbstractTeamController<Flag> {
    private FlagDesignRomHandler flagDesignRomHandler;

    public FlagDesignController(Router router, FlagDesignPage view) {
        super(router, view);
        /*
        view.onMoveOut(() -> {
            try {
                RomHandler.Team team = router.getState().getTeam();
                flagDesignRomHandler.unlinkFlag(team);
                view.setData(romHandler.readFromRomAt(team));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        view.onMoveIn(teams -> {
            try {
                RomHandler.Team team = router.getState().getTeam();
                flagDesignRomHandler.linkTeams(toArray(teams, RomHandler.Team.class), team);
                view.setData(romHandler.readFromRomAt(team));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        */
    }


    @Override
    protected RomHandler<Flag> romHandlerFor(File rom) {
        flagDesignRomHandler = new FlagDesignRomHandler(rom);
        return new FlagRomHandler(rom, flagDesignRomHandler, new FlagColorRomHandler(rom));
    }




}

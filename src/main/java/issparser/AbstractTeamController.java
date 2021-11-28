package issparser;

import issparser.commons.RomHandler;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public abstract class AbstractTeamController<T> extends AbstractController<T> {

    protected RomHandler<T> romHandler;

    public void setState(State state) {
        this.romHandler = romHandlerFor(state.getRom());
        reloadView();
    }

    protected abstract RomHandler<T> romHandlerFor(File rom);

    //private State state;
    private TeamView<T> view;

    public AbstractTeamController(Router router, TeamView<T> view) {
        super(router);
        State state = router.getState();
        if(state != null) {
            File rom = state.getRom();
            if(rom != null) {
                romHandler = romHandlerFor(rom);
            }
        }
        this.view = view;
        view.setSaveListener(data -> {
            try {
                RomHandler.Team team = router.getState().getTeam();
                romHandler.writeToRomAt(team, (T) data);
                view.setData(romHandler.readFromRomAt(team));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });
        view.setReadListener(() -> {
            try {
                view.setData(romHandler.readFromRomAt(router.getState().getTeam()));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });

        view.setNextTeamListener(() -> router.navigate(router.getState().nextTeam()));
        view.setPreviousTeamListener(() -> router.navigate(router.getState().previousTeam()));
        view.setTeamListener(team -> router.navigate(router.getState().withTeam(team)));

        view.setNextResourceListener(router::nextResource);
        view.setPreviousResourceListener(router::previousResource);
        view.setResourceListener(resourceIndex ->
                router.navigate(Router.Route.forResource(resourceIndex))
        );
    }

    public void reloadView() {
        //if(router.getState().getRom()!= null && router.getState().getTeam() != -1)
        try {
            view.setTeam(router.getState().getTeam());
            view.setData(romHandler.readFromRomAt(router.getState().getTeam()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JPanel getPanel() {
        return view.getPanel();
    }
}

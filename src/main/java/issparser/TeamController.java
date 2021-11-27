package issparser;

import issparser.playernames.PlayerNameRomHandler;

import javax.swing.*;
import java.io.IOException;

public class TeamController extends AbstractController {

    private PlayerNameRomHandler romHandler;

    public void setState(State state) {
        if(!state.equals(this.state)) {
            this.state = state;
            this.romHandler = new PlayerNameRomHandler(state.getRom());
            reloadView();
        }
    }

    private State state;
    private PlayerNameView view;

    public TeamController(Router router, PlayerNameView view) {
        super(router);
        this.view = view;
        view.setSaveListener(playerNames -> {
            try {
                int teamIndex = state.getTeamIndex();
                romHandler.writeToRomAt(teamIndex, playerNames);
                view.setData(romHandler.readFromRomAt(teamIndex));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });
        view.setReadListener(() -> {
            try {
                view.setData(romHandler.readFromRomAt(state.getTeamIndex()));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });

        view.setNextTeamListener( () -> router.navigate(state.nextTeam()));
        view.setPreviousTeamListener( () -> router.navigate(state.previousTeam()));
        view.setTeamListener(teamIndex -> router.navigate(state.withTeam(teamIndex)));

    }

    public void reloadView() {
        if(state.getRom()!= null && state.getTeamIndex() != -1) try {
            view.setTeamIndex(state.getTeamIndex());
            view.setData(romHandler.readFromRomAt(state.getTeamIndex()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JPanel getPanel() {
        return view.getPanel();
    }
}

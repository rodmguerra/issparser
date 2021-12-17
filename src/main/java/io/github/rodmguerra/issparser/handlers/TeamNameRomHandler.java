package io.github.rodmguerra.issparser.handlers;


import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.texts.TeamName;
import io.github.rodmguerra.issparser.model.TeamNameText;
import io.github.rodmguerra.issparser.model.tiles.TeamNameTiles;

import java.io.IOException;
import java.util.Map;

public class TeamNameRomHandler implements RomHandler<TeamName> {

    private final RomHandler<TeamNameText> menuHandler;
    private final RomHandler<TeamNameTiles> gameHandler;

    public TeamNameRomHandler(RomHandler<TeamNameText> menuHandler, RomHandler<TeamNameTiles> gameHandler) {
        this.menuHandler = menuHandler;
        this.gameHandler = gameHandler;
    }
    @Override
    public Map<Team, TeamName> readFromRom() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToRom(Map<Team, ? extends TeamName> input) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TeamName readFromRomAt(Team team) throws IOException {
        return new TeamName(menuHandler.readFromRomAt(team), gameHandler.readFromRomAt(team));
    }

    @Override
    public void writeToRomAt(Team team, TeamName input) throws IOException {
        gameHandler.writeToRomAt(team,input.getInGame());
        menuHandler.writeToRomAt(team,input.getInMenu());
    }
}

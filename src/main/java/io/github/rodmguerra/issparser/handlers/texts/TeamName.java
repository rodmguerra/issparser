package io.github.rodmguerra.issparser.handlers.texts;


import io.github.rodmguerra.issparser.model.TeamNameText;
import io.github.rodmguerra.issparser.model.tiles.TeamNameTiles;

public class TeamName {
    private final TeamNameText inMenu;
    private final TeamNameTiles inGame;

    public TeamName(TeamNameText inMenu, TeamNameTiles inGame) {
        this.inMenu = inMenu;
        this.inGame = inGame;
    }

    public TeamNameText getInMenu() {
        return inMenu;
    }

    public TeamNameTiles getInGame() {
        return inGame;
    }
}

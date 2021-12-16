package io.github.rodmguerra.issparser.handlers.texts;


import io.github.rodmguerra.issparser.model.tiles.TeamNameInGame;
import io.github.rodmguerra.issparser.model.TeamNameInMenu;

public class TeamName {
    private final TeamNameInMenu inMenu;
    private final TeamNameInGame inGame;

    public TeamName(TeamNameInMenu inMenu, TeamNameInGame inGame) {
        this.inMenu = inMenu;
        this.inGame = inGame;
    }

    public TeamNameInMenu getInMenu() {
        return inMenu;
    }

    public TeamNameInGame getInGame() {
        return inGame;
    }
}

package io.github.rodmguerra.isseditor.team;

import io.github.rodmguerra.issparser.commons.RomHandler;

import javax.swing.*;
import java.util.function.Consumer;

public interface TeamView<T> {
    T getData();
    void setData(T data);
    void setTeam(RomHandler.Team team);
    JPanel getPanel();

    void setSaveListener(Consumer<T> listener);
    void setReadListener(Runnable listener);
    void setNextTeamListener(Runnable listener);
    void setPreviousTeamListener(Runnable listener);
    void setTeamListener(Consumer<RomHandler.Team> listener);
    void setNextResourceListener(Runnable listener);
    void setPreviousResourceListener(Runnable listener);
    void setResourceListener(Consumer<Integer> listener);
}

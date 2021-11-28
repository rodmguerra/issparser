package issparser;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public interface TeamView<T> {
    T getData();
    void setData(T data);
    void setTeamIndex(int teamIndex);
    JPanel getPanel();

    final String[] TEAMS = {
            "Germany", "Italy", "Holland", "Spain", "England",
            "Wales", "France", "Denmark", "Sweden",
            "Norway", "Ireland", "Belgium", "Austria", "Switz",
            "Romania", "Bulgaria", "Russia", "Argentina", "Brazil",
            "Colombia", "Mexico", "U.S.A.", "Nigeria", "Cameroon", "Scotland",
            "S.Korea", "Superstar"};

    void setSaveListener(Consumer<T> listener);
    void setReadListener(Runnable listener);
    void setNextTeamListener(Runnable listener);
    void setPreviousTeamListener(Runnable listener);
    void setTeamListener(Consumer<Integer> listener);
    void setNextResourceListener(Runnable listener);
    void setPreviousResourceListener(Runnable listener);
    void setResourceListener(Consumer<Integer> listener);
}

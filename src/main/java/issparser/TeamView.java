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

    void setSaveListener(Consumer<List<String>> saveListener);
    void setReadListener(Runnable readListener);
    void setNextTeamListener(Runnable nextTeamListener);
    void setPreviousTeamListener(Runnable previousTeamListener);
    void setTeamListener(Consumer<Integer> teamListener);
}

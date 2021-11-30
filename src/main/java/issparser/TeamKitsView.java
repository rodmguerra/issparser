package issparser;

import issparser.kits.model.ColorType;
import issparser.kits.model.TeamKits;

import javax.swing.*;
import java.util.stream.Stream;

public class TeamKitsView {
    private final KitView first;
    private final KitView second;
    private final KeeperKitView goalkeeper;
    private final JComboBox<String> predominantColor;

    public TeamKitsView(KitView first, KitView second, KeeperKitView goalkeeper, JComboBox<String> predominantColor) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
        this.predominantColor = predominantColor;
    }

    public KitView getFirst() {
        return first;
    }

    public KitView getSecond() {
        return second;
    }

    public static TeamKitsView zero() {
        JComboBox<String> predominantColor = new JComboBox<>(Stream.of(ColorType.values()).map(ColorType::toString).toArray(String[]::new));
        return new TeamKitsView(KitView.zero(), KitView.zero(), KeeperKitView.zero(), predominantColor);
    }

    public JComboBox<String> getPredominantColor() {
        return predominantColor;
    }

    @Override
    public String toString() {
        return "TeamKitsView{" +
                "first=" + first +
                ", second=" + second +
                ", goalkeeper=" + goalkeeper +
                ", predominantColor=" + predominantColor +
                '}';
    }

    public void setFromModel(TeamKits teamKits) {
        this.first.setFromModel(teamKits.getFirst());
        this.second.setFromModel(teamKits.getSecond());
        this.goalkeeper.setFromModel(teamKits.getGoalkeeper());
        this.predominantColor.setSelectedIndex(teamKits.getPredominantColor().ordinal());
    }

    public TeamKits toModel() {
        return new TeamKits(first.toModel(), second.toModel(), goalkeeper.toModel(), ColorType.at(predominantColor.getSelectedIndex()));
    }

    public KeeperKitView getGoalkeeper() {
        return goalkeeper;
    }
}

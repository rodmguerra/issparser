package io.github.rodmguerra.isseditor.uniformcolors;

import io.github.rodmguerra.issparser.model.colors.uniforms.PredominantColor;
import io.github.rodmguerra.issparser.model.colors.uniforms.TeamUniforms;

import javax.swing.*;
import java.util.stream.Stream;

public class TeamUniformsView {
    private final UniformView first;
    private final UniformView second;
    private final KeeperUniformView goalkeeper;
    private final JComboBox<String> predominantColor;

    public TeamUniformsView(UniformView first, UniformView second, KeeperUniformView goalkeeper, JComboBox<String> predominantColor) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
        this.predominantColor = predominantColor;
    }

    public UniformView getFirst() {
        return first;
    }

    public UniformView getSecond() {
        return second;
    }

    public static TeamUniformsView zero() {
        JComboBox<String> predominantColor = new JComboBox<>(Stream.of(PredominantColor.values()).map(PredominantColor::toString).toArray(String[]::new));
        return new TeamUniformsView(UniformView.zero(), UniformView.zero(), KeeperUniformView.zero(), predominantColor);
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

    public void setFromModel(TeamUniforms teamKits) {
        this.first.setFromModel(teamKits.getFirst());
        this.second.setFromModel(teamKits.getSecond());
        this.goalkeeper.setFromModel(teamKits.getGoalkeeper());
        this.predominantColor.setSelectedIndex(teamKits.getPredominantColor().ordinal());
    }

    public TeamUniforms toModel() {
        return new TeamUniforms(first.toModel(), second.toModel(), goalkeeper.toModel(), PredominantColor.at(predominantColor.getSelectedIndex()));
    }

    public KeeperUniformView getGoalkeeper() {
        return goalkeeper;
    }
}

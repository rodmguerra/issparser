package io.github.rodmguerra.issparser.model.colors.uniforms;

public class TeamUniforms {
    private final MainTeamUniform first;
    private final MainTeamUniform second;
    private final KeeperUniform goalkeeper;
    private final PredominantColor predominantColor;


    public TeamUniforms(MainTeamUniform first, MainTeamUniform second, KeeperUniform goalkeeper, PredominantColor predominantColor) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
        this.predominantColor = predominantColor;
    }

    public MainTeamUniform getFirst() {
        return first;
    }

    public MainTeamUniform getSecond() {
        return second;
    }

    public KeeperUniform getGoalkeeper() {
        return goalkeeper;
    }

    public PredominantColor getPredominantColor() {
        return predominantColor;
    }

    @Override
    public String toString() {
        return "TeamKits{" +
                "first=" + first +
                ", second=" + second +
                ", goalkeeper=" + goalkeeper +
                '}';
    }
}

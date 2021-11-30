package issparser.kits.model;

public class TeamKits {
    private final Kit first;
    private final Kit second;
    private final KeeperKit goalkeeper;
    private final ColorType predominantColor;


    public TeamKits(Kit first, Kit second, KeeperKit goalkeeper, ColorType predominantColor) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
        this.predominantColor = predominantColor;
    }

    public Kit getFirst() {
        return first;
    }

    public Kit getSecond() {
        return second;
    }

    public KeeperKit getGoalkeeper() {
        return goalkeeper;
    }

    public ColorType getPredominantColor() {
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

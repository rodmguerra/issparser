package issparser.kits.model;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 20/11/21
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class TeamKits {
    private final Kit first;
    private final Kit second;

    public TeamKits(Kit first, Kit second) {
        this.first = first;
        this.second = second;
    }

    public Kit getFirst() {
        return first;
    }

    public Kit getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "issparser.kits.model.TeamKits{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}

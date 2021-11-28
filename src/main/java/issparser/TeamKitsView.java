package issparser;

import issparser.kits.model.Kit;
import issparser.kits.model.TeamKits;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 20/11/21
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class TeamKitsView {
    private final KitView first;
    private final KitView second;

    /*
    public static TeamKitsView fromModel(TeamKits kits) {
        return new TeamKitsView(KitView.fromModel(kits.getFirst()), KitView.fromModel(kits.getSecond()));
    }
    */

    public TeamKitsView(KitView first, KitView second) {
        this.first = first;
        this.second = second;
    }

    public KitView getFirst() {
        return first;
    }

    public KitView getSecond() {
        return second;
    }

    public static TeamKitsView zero() {
        return new TeamKitsView(KitView.zero(), KitView.zero());
    }

    @Override
    public String toString() {
        return "issparser.kits.model.TeamKits{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public void setFromModel(TeamKits teamKits) {
        System.out.println(teamKits);
        this.first.setFromModel(teamKits.getFirst());
        this.second.setFromModel(teamKits.getSecond());

    }
}

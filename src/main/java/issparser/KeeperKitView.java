package issparser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import issparser.kits.model.KeeperKit;
import issparser.kits.model.Kit;


public class KeeperKitView {
    private final KitPartView shirtAndSocks;
    private final KitPartView shorts;

    public KeeperKitView(KitPartView shirtAndSocks, KitPartView shorts) {
        this.shirtAndSocks = shirtAndSocks;
        this.shorts = shorts;
    }

    @JsonIgnore
    public KitPartView getShirtAndSocks() {
        return shirtAndSocks;
    }

    @JsonIgnore
    public KitPartView getShorts() {
        return shorts;
    }

    @Override
    public String toString() {
        return "KitView{" +
                "shirtAndSocks=" + shirtAndSocks +
                ", shorts=" + shorts +
                '}';
    }

    public static KeeperKitView zero() {
        return new KeeperKitView(KitPartView.zero(5), KitPartView.zero(1));
    }

    public void setFromModel(KeeperKit kit) {
        shirtAndSocks.setFromModel(kit.getShirtAndSocks());
        shorts.setFromModel(kit.getShorts());
    }

    public KeeperKit toModel() {
        return new KeeperKit(shirtAndSocks.toModel(), shorts.toModel());
    }
}

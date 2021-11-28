package issparser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import issparser.kits.model.Kit;
import issparser.kits.model.KitPart;

import java.util.Arrays;


public class KitView {
    private final KitPartView shirt;
    private final KitPartView shorts;
    private final KitPartView socks;

    /*
    public static KitView fromModel(Kit kit) {
        KitPartView shirt = KitPartView.fromModel(kit.getShirt());
        KitPartView shorts = KitPartView.fromModel(kit.getShorts());
        KitPartView socks = KitPartView.fromModel(kit.getSocks());
        return new KitView(shirt, shorts, socks);
    }
    */

    public KitView(KitPartView shirt, KitPartView shorts, KitPartView socks) {
        this.shirt = shirt;
        this.shorts = shorts;
        this.socks = socks;
    }

    @JsonIgnore
    public KitPartView getShirt() {
        return shirt;
    }

    @JsonIgnore
    public KitPartView getShorts() {
        return shorts;
    }

    @JsonIgnore
    public KitPartView getSocks() {
        return socks;
    }

    @Override
    public String toString() {
        return "KitView{" +
                "shirt=" + shirt +
                ", shorts=" + shorts +
                ", socks=" + socks +
                '}';
    }

    public static KitView zero() {
        return new KitView(KitPartView.zero(3), KitPartView.zero(3), KitPartView.zero(2));
    }

    public void setFromModel(Kit kit) {
        shirt.setFromModel(kit.getShirt());
        shorts.setFromModel(kit.getShorts());
        socks.setFromModel(kit.getSocks());
    }
}

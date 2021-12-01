package io.github.rodmguerra.isseditor.uniformcolors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.issparser.model.colors.uniforms.MainTeamUniform;


public class UniformView {
    private final ColoredPartView shirt;
    private final ColoredPartView shorts;
    private final ColoredPartView socks;

    /*
    public static KitView fromModel(Kit kit) {
        KitPartView shirt = KitPartView.fromModel(kit.getShirtAndSocks());
        KitPartView shorts = KitPartView.fromModel(kit.getShorts());
        KitPartView socks = KitPartView.fromModel(kit.getSocks());
        return new KitView(shirt, shorts, socks);
    }
    */

    public UniformView(ColoredPartView shirt, ColoredPartView shorts, ColoredPartView socks) {
        this.shirt = shirt;
        this.shorts = shorts;
        this.socks = socks;
    }

    @JsonIgnore
    public ColoredPartView getShirt() {
        return shirt;
    }

    @JsonIgnore
    public ColoredPartView getShorts() {
        return shorts;
    }

    @JsonIgnore
    public ColoredPartView getSocks() {
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

    public static UniformView zero() {
        return new UniformView(ColoredPartView.zero(3), ColoredPartView.zero(3), ColoredPartView.zero(2));
    }

    public void setFromModel(MainTeamUniform kit) {
        shirt.setFromModel(kit.getShirt());
        shorts.setFromModel(kit.getShorts());
        socks.setFromModel(kit.getSocks());
    }

    public MainTeamUniform toModel() {
        return new MainTeamUniform(shirt.toModel(), shorts.toModel(), socks.toModel());
    }
}

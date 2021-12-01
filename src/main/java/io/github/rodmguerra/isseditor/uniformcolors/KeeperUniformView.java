package io.github.rodmguerra.isseditor.uniformcolors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.issparser.model.colors.uniforms.KeeperUniform;


public class KeeperUniformView {
    private final ColoredPartView shirtAndSocks;
    private final ColoredPartView shorts;

    public KeeperUniformView(ColoredPartView shirtAndSocks, ColoredPartView shorts) {
        this.shirtAndSocks = shirtAndSocks;
        this.shorts = shorts;
    }

    @JsonIgnore
    public ColoredPartView getShirtAndSocks() {
        return shirtAndSocks;
    }

    @JsonIgnore
    public ColoredPartView getShorts() {
        return shorts;
    }

    @Override
    public String toString() {
        return "KitView{" +
                "shirtAndSocks=" + shirtAndSocks +
                ", shorts=" + shorts +
                '}';
    }

    public static KeeperUniformView zero() {
        return new KeeperUniformView(ColoredPartView.zero(5), ColoredPartView.zero(1));
    }

    public void setFromModel(KeeperUniform kit) {
        shirtAndSocks.setFromModel(kit.getShirtAndSocks());
        shorts.setFromModel(kit.getShorts());
    }

    public KeeperUniform toModel() {
        return new KeeperUniform(shirtAndSocks.toModel(), shorts.toModel());
    }
}

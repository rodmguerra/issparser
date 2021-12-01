package io.github.rodmguerra.isseditor.hairandskin;


import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.issparser.model.colors.hairandskin.HairAndSkin;

public class HairAndSkinView {
    private final ColoredPartView hair;
    private final ColoredPartView skin;

    public HairAndSkinView(ColoredPartView hair, ColoredPartView skin) {
        this.hair = hair;
        this.skin = skin;
    }

    public ColoredPartView getHair() {
        return hair;
    }

    public ColoredPartView getSkin() {
        return skin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HairAndSkinView that = (HairAndSkinView) o;

        if (hair != null ? !hair.equals(that.hair) : that.hair != null) return false;
        if (skin != null ? !skin.equals(that.skin) : that.skin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hair != null ? hair.hashCode() : 0;
        result = 31 * result + (skin != null ? skin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HairAndSkinView{" +
                "hair=" + hair +
                ", skin=" + skin +
                '}';
    }

    public static HairAndSkinView zero(int hairColorCount, int skinColorCount) {
        return new HairAndSkinView(ColoredPartView.zero(hairColorCount), ColoredPartView.zero(skinColorCount));
    }

    public void setFromModel(HairAndSkin hairAndSkin) {
        hair.setFromModel(hairAndSkin.getHair());
        skin.setFromModel(hairAndSkin.getSkin());

    }

    public HairAndSkin toModel() {
        return new HairAndSkin(hair.toModel(), skin.toModel());
    }
}

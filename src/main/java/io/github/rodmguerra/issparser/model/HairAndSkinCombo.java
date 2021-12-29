package io.github.rodmguerra.issparser.model;

import io.github.rodmguerra.issparser.model.colors.hairandskin.NormalHairAndSkin;

public class HairAndSkinCombo {
    private final NormalHairAndSkin normal;
    private final SpecialHair specialHair;
    private final SpecialSkin specialSkin;

    public HairAndSkinCombo(NormalHairAndSkin normal, SpecialHair specialHair, SpecialSkin specialSkin) {
        this.normal = normal;
        this.specialHair = specialHair;
        this.specialSkin = specialSkin;
    }

    public NormalHairAndSkin getNormal() {
        return normal;
    }

    public SpecialHair getSpecialHair() {
        return specialHair;
    }

    public SpecialSkin getSpecialSkin() {
        return specialSkin;
    }
}

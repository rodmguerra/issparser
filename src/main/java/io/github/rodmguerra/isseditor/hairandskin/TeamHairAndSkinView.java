package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.issparser.model.HairAndSkinCombo;
import io.github.rodmguerra.issparser.model.HairStyle;
import io.github.rodmguerra.issparser.model.SpecialHair;
import io.github.rodmguerra.issparser.model.SpecialSkin;
import io.github.rodmguerra.issparser.model.colors.hairandskin.NormalHairAndSkin;

import javax.swing.*;
import java.util.stream.Stream;

import static io.github.rodmguerra.isseditor.Texts.string;
import static io.github.rodmguerra.isseditor.Texts.strings;

public class TeamHairAndSkinView {

    public static final String PAGE = "rom.feature.player_colors";
    private final HairAndSkinView first;
    //private final HairAndSkinView second;
    private final HairAndSkinView goalkeeper;
    private final JComboBox<String> specialHairField;
    private final JComboBox<String> specialSkinField;

    public TeamHairAndSkinView(HairAndSkinView first, HairAndSkinView second, HairAndSkinView goalkeeper) {
        this.first = first;
        //this.second = second;
        this.goalkeeper = goalkeeper;
        this.specialHairField = new JComboBox<>(strings(PAGE, "special", "hair", "values"));
        SpecialSkin[] skins = SpecialSkin.values();
        this.specialSkinField = new JComboBox<>(strings(PAGE, "special", "skin", "values"));
    }

    public HairAndSkinView getFirst() {
        return first;
    }
                    /*
    public HairAndSkinView getSecond() {
        return second;
    }                 */

    public HairAndSkinView getGoalkeeper() {
        return goalkeeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamHairAndSkinView that = (TeamHairAndSkinView) o;

        if (first != null ? !first.equals(that.first) : that.first != null) return false;
        if (goalkeeper != null ? !goalkeeper.equals(that.goalkeeper) : that.goalkeeper != null) return false;
        //if (second != null ? !second.equals(that.second) : that.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        //result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (goalkeeper != null ? goalkeeper.hashCode() : 0);
        return result;
    }

    public static TeamHairAndSkinView zero() {
        return new TeamHairAndSkinView(HairAndSkinView.zero(1, 5), HairAndSkinView.zero(1, 5), HairAndSkinView.zero(1, 3));
    }

    public void setFromModel(HairAndSkinCombo combo) {
        first.setFromModel(combo.getNormal().getFirst());
        //second.setFromModel(combo.getNormal().getSecond());
        goalkeeper.setFromModel(combo.getNormal().getGoalkeeper());
        specialHairField.setSelectedIndex(combo.getSpecialHair().ordinal());
        specialSkinField.setSelectedIndex(combo.getSpecialSkin().ordinal());
    }

    public JComboBox<String> getSpecialHairField() {
        return specialHairField;
    }

    public JComboBox<String> getSpecialSkinField() {
        return specialSkinField;
    }

    public HairAndSkinCombo toModel() {
        NormalHairAndSkin normal = new NormalHairAndSkin(first.toModel(), first.toModel(), goalkeeper.toModel());
        SpecialHair specialHair = SpecialHair.at(specialHairField.getSelectedIndex());
        SpecialSkin specialSkin = SpecialSkin.at(specialSkinField.getSelectedIndex());
        return new HairAndSkinCombo(normal, specialHair, specialSkin);
    }
}

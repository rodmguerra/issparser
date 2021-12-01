package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.issparser.model.colors.hairandskin.TeamHairAndSkin;

public class TeamHairAndSkinView {

    private final HairAndSkinView first;
    private final HairAndSkinView second;
    private final HairAndSkinView goalkeeper;

    public TeamHairAndSkinView(HairAndSkinView first, HairAndSkinView second, HairAndSkinView goalkeeper) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
    }

    public HairAndSkinView getFirst() {
        return first;
    }

    public HairAndSkinView getSecond() {
        return second;
    }

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
        if (second != null ? !second.equals(that.second) : that.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (goalkeeper != null ? goalkeeper.hashCode() : 0);
        return result;
    }

    public static TeamHairAndSkinView zero() {
        return new TeamHairAndSkinView(HairAndSkinView.zero(1, 5), HairAndSkinView.zero(1, 5), HairAndSkinView.zero(1, 3));
    }

    public void setFromModel(TeamHairAndSkin teamHairAndSkin) {
        first.setFromModel(teamHairAndSkin.getFirst());
        second.setFromModel(teamHairAndSkin.getSecond());
        goalkeeper.setFromModel(teamHairAndSkin.getGoalkeeper());
    }

    public TeamHairAndSkin toModel() {
        return new TeamHairAndSkin(first.toModel(), second.toModel(), goalkeeper.toModel());
    }
}

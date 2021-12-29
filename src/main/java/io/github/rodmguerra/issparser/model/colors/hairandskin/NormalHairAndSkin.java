package io.github.rodmguerra.issparser.model.colors.hairandskin;

public class NormalHairAndSkin {
    private final HairAndSkin first;
    private final HairAndSkin second;
    private final HairAndSkin goalkeeper;

    public NormalHairAndSkin(HairAndSkin first, HairAndSkin second, HairAndSkin goalkeeper) {
        this.first = first;
        this.second = second;
        this.goalkeeper = goalkeeper;
    }

    public HairAndSkin getFirst() {
        return first;
    }

    public HairAndSkin getSecond() {
        return second;
    }

    public HairAndSkin getGoalkeeper() {
        return goalkeeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NormalHairAndSkin that = (NormalHairAndSkin) o;

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

    @Override
    public String toString() {
        return "TeamHairAndSkin{" +
                "first=" + first +
                ", second=" + second +
                ", goalkeeper=" + goalkeeper +
                '}';
    }
}

package issparser.kits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({ "shirt", "shorts", "socks" })
public class Kit {
    private final KitPart shirt;
    private final KitPart shorts;
    private final KitPart socks;

    public Kit(KitPart shirt, KitPart shorts, KitPart socks) {
        this.shirt = shirt;
        this.shorts = shorts;
        this.socks = socks;
    }

    @JsonIgnore
    public KitPart getShirt() {
        return shirt;
    }

    @JsonIgnore
    public KitPart getShorts() {
        return shorts;
    }

    @JsonIgnore
    public KitPart getSocks() {
        return socks;
    }

    @JsonProperty("shirt")
    private int[][] getShirtRgbs() {
        return rgbsToIntMatrix(shirt.getRgbs());
    }

    private int[][] rgbsToIntMatrix(RGB[] rgbs) {
        int[][] out = new int[rgbs.length][3];
        for(int i=0; i<rgbs.length; i++){
            out[i][0] = rgbs[i].getRed();
            out[i][1] = rgbs[i].getGreen();
            out[i][2] = rgbs[i].getBlue();
        }
        return out;
    }

    @JsonProperty("shorts")
    private int[][] getShortsRgbs() {
        return rgbsToIntMatrix(shorts.getRgbs());
    }

    @JsonProperty("socks")
    private int[][] getSocksRgbs() {
        return rgbsToIntMatrix(socks.getRgbs());
    }

    @Override
    public String toString() {
        return "issparser.kits.model.Kit{" +
                "shirt=" + shirt +
                ", shorts=" + shorts +
                ", socks=" + socks +
                '}';
    }

}

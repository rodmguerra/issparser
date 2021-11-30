package issparser.kits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({ "shirtAndSocks", "shorts"})
public class KeeperKit {
    private final KitPart shirtAndSocks;
    private final KitPart shorts;

    public KeeperKit(KitPart shirtAndSocks, KitPart shorts) {
        this.shirtAndSocks = shirtAndSocks;
        this.shorts = shorts;
    }

    @JsonIgnore
    public KitPart getShirtAndSocks() {
        return shirtAndSocks;
    }

    @JsonIgnore
    public KitPart getShorts() {
        return shorts;
    }

    @JsonProperty("shirtAndSocks")
    private int[][] getShirtRgbs() {
        return rgbsToIntMatrix(shirtAndSocks.getRgbs());
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


    @Override
    public String toString() {
        return "KeeperKit{" +
                "shirtAndSocks=" + shirtAndSocks +
                ", shorts=" + shorts +
                '}';
    }

}

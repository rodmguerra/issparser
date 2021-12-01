package io.github.rodmguerra.issparser.model.colors.uniforms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;


@JsonPropertyOrder({ "shirtAndSocks", "shorts"})
public class KeeperUniform {
    private final ColoredPart shirtAndSocks;
    private final ColoredPart shorts;

    public KeeperUniform(ColoredPart shirtAndSocks, ColoredPart shorts) {
        this.shirtAndSocks = shirtAndSocks;
        this.shorts = shorts;
    }

    @JsonIgnore
    public ColoredPart getShirtAndSocks() {
        return shirtAndSocks;
    }

    @JsonIgnore
    public ColoredPart getShorts() {
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

package io.github.rodmguerra.issparser.model.colors.uniforms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;


@JsonPropertyOrder({ "shirt", "shorts", "socks" })
public class MainTeamUniform {
    private final ColoredPart shirt;
    private final ColoredPart shorts;
    private final ColoredPart socks;

    public MainTeamUniform(ColoredPart shirt, ColoredPart shorts, ColoredPart socks) {
        this.shirt = shirt;
        this.shorts = shorts;
        this.socks = socks;
    }

    @JsonIgnore
    public ColoredPart getShirt() {
        return shirt;
    }

    @JsonIgnore
    public ColoredPart getShorts() {
        return shorts;
    }

    @JsonIgnore
    public ColoredPart getSocks() {
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
        return "io.github.rodmguerra.issparser.kits.model.Kit{" +
                "shirt=" + shirt +
                ", shorts=" + shorts +
                ", socks=" + socks +
                '}';
    }

}

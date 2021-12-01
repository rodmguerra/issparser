package io.github.rodmguerra.issparser.model.colors;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public class ColoredPart {
    private final RGB[] rgbs;

    @JsonCreator
    public ColoredPart(RGB... rgbs) {
        this.rgbs = rgbs;
    }

    public RGB[] getRgbs() {
        return rgbs;
    }

    @Override
    public String toString() {
        return "ColoredPart{" +
                "rgbs=" + Arrays.toString(rgbs) +
                '}';
    }

}

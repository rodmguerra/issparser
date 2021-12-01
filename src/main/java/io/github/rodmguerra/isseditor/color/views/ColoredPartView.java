package io.github.rodmguerra.isseditor.color.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;

import java.util.Arrays;
import java.util.stream.Stream;

public class ColoredPartView {
    private final RGBView[] rgbs;

    /*
    public static KitPartView fromModel(KitPart kitPart) {
        RGBView[] rgbViews = Arrays.stream(kitPart.getRgbs()).map(RGBView::fromModel).toArray(RGBView[]::new);
        return new KitPartView(rgbViews);
    }
    */

    @JsonCreator
    public ColoredPartView(RGBView... rgbs) {
        this.rgbs = rgbs;
    }

    public RGBView[] getRgbs() {
        return rgbs;
    }

    @Override
    public String toString() {
        return "ColoredPart{" +
                "rgbs=" + Arrays.toString(rgbs) +
                '}';
    }


    public static ColoredPartView zero(int size) {
        RGBView[] rgbs = new RGBView[size];

        for(int i=0; i<size; i++) {
            rgbs[i] = RGBView.zero();
        }


        return new ColoredPartView(rgbs);
    }


    public void setFromModel(ColoredPart part) {
        RGB[] modelRgbs = part.getRgbs();
        for (int i = 0; i < rgbs.length; i++) {
            rgbs[i].setFromModel(modelRgbs[i]);
        }
    }

    public ColoredPart toModel() {
        return new ColoredPart(Stream.of(rgbs).map(RGBView::toModel).toArray(RGB[]::new));
    }
}

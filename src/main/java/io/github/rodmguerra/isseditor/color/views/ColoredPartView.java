package io.github.rodmguerra.isseditor.color.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ColoredPartView {
    private final RGBView[] rgbs;
    private Consumer<ColorChangeEvent> colorChangeConsumer = new Consumer<ColorChangeEvent>() {

        @Override
        public void accept(ColorChangeEvent colorChangeEvent) {
        }

    };
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
        ColoredPartView view = new ColoredPartView(rgbs);

        for(int i=0; i<size; i++) {
            final int pos = i;
            rgbs[pos] = RGBView.zero();
            rgbs[pos].onColorChange(rgb -> view.colorChangeConsumer.accept(new ColorChangeEvent(pos, rgb)));
        }

        return view;
    }


    public void setFromModel(ColoredPart part) {
        RGB[] modelRgbs = part.getRgbs();
        for (int i = 0; i < rgbs.length; i++) {
            rgbs[i].setFromModel(modelRgbs[i]);
            final int pos = i;
            rgbs[i].onColorChange(rgb -> colorChangeConsumer.accept(new ColorChangeEvent(pos, rgb)));
        }
    }

    public void onColorChange(Consumer<ColorChangeEvent> consumer) {
        this.colorChangeConsumer = consumer;
    }

    public ColoredPart toModel() {
        return new ColoredPart(Stream.of(rgbs).map(RGBView::toModel).toArray(RGB[]::new));
    }


    public static class ColorChangeEvent {
        private final int colorIndex;
        private final RGB rgb;

        private ColorChangeEvent(int colorIndex, RGB rgb) {
            this.colorIndex = colorIndex;
            this.rgb = rgb;
        }

        public int getColorIndex() {
            return colorIndex;
        }

        public RGB getRgb() {
            return rgb;
        }
    }
}

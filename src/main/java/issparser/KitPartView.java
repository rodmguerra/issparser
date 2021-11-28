package issparser;

import com.fasterxml.jackson.annotation.JsonCreator;
import issparser.kits.model.KitPart;
import issparser.kits.model.RGB;

import java.util.Arrays;
import java.util.stream.Stream;

public class KitPartView {
    private final RGBView[] rgbs;

    /*
    public static KitPartView fromModel(KitPart kitPart) {
        RGBView[] rgbViews = Arrays.stream(kitPart.getRgbs()).map(RGBView::fromModel).toArray(RGBView[]::new);
        return new KitPartView(rgbViews);
    }
    */

    @JsonCreator
    public KitPartView(RGBView... rgbs) {
        this.rgbs = rgbs;
    }

    public RGBView[] getRgbs() {
        return rgbs;
    }

    @Override
    public String toString() {
        return "issparser.kits.model.KitPart{" +
                "rgbs=" + Arrays.toString(rgbs) +
                '}';
    }


    public static KitPartView zero(int size) {
        RGBView[] rgbs = new RGBView[size];

        for(int i=0; i<size; i++) {
            rgbs[i] = RGBView.zero();
        }


        return new KitPartView(rgbs);
    }


    public void setFromModel(KitPart part) {
        RGB[] modelRgbs = part.getRgbs();
        for (int i = 0; i < rgbs.length; i++) {
            rgbs[i].setFromModel(modelRgbs[i]);
        }
    }
}

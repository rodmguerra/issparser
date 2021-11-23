package issparser.kits.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import issparser.kits.model.RGB;

import java.util.Arrays;

public class KitPart {
    private final RGB[] rgbs;

    @JsonCreator
    public KitPart(RGB... rgbs) {
        this.rgbs = rgbs;
    }

    public RGB[] getRgbs() {
        return rgbs;
    }

    @Override
    public String toString() {
        return "issparser.kits.model.KitPart{" +
                "rgbs=" + Arrays.toString(rgbs) +
                '}';
    }


}

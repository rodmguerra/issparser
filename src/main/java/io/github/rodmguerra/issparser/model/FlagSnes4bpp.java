package io.github.rodmguerra.issparser.model;

public class FlagSnes4bpp {
    private final byte[] top;
    private final byte[] bottom;

    public FlagSnes4bpp(byte[] top, byte[] bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public byte[] getTop() {
        return top;
    }

    public byte[] getBottom() {
        return bottom;
    }
}

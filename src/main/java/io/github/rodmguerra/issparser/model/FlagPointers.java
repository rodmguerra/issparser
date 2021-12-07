package io.github.rodmguerra.issparser.model;


public class FlagPointers {
    private final int top;
    private final int bottom;

    public FlagPointers(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }
}

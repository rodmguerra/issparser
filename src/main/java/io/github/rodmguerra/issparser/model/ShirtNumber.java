package io.github.rodmguerra.issparser.model;


public class ShirtNumber {
    private final byte innerData;

    public ShirtNumber(byte innerData) {
        this.innerData = innerData;
    }

    public int getNumber() {
        return (innerData % 0x10) + 1;
    }

}

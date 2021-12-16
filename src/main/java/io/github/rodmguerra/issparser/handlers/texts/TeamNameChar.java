package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.rodmguerra.issparser.commons.ParsingUtils;

public class TeamNameChar {
    private final byte top;
    private final byte bottom;

    public TeamNameChar(byte top, byte bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    private TeamNameChar(int top, int bottom) {
        this.top = (byte) top;
        this.bottom = (byte) bottom;
    }

    public static TeamNameChar from(String aChar) {
        return map.get(aChar);
    }

    @Override
    public String toString() {
        //return ParsingUtils.bytesString(new byte[] { top, bottom });
        return map.inverse().get(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamNameChar that = (TeamNameChar) o;

        if (bottom != that.bottom) return false;
        if (top != that.top) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) top;
        result = 31 * result + (int) bottom;
        return result;
    }

    private static final BiMap<String, TeamNameChar> map = ImmutableBiMap.<String,TeamNameChar>builder()
            .put("A", new TeamNameChar(0xD0, 0xC0))
            .put("B", new TeamNameChar(0xD1, 0xC1))
            .put("C", new TeamNameChar(0xD2, 0xC2))
            .put("D", new TeamNameChar(0xD3, 0xC3))
            .put("E", new TeamNameChar(0xD4, 0xC4))
            .put("F", new TeamNameChar(0xD5, 0xC5))
            .put("G", new TeamNameChar(0xD6, 0xC6))
            .put("H", new TeamNameChar(0xD7, 0xC7))
            .put("I", new TeamNameChar(0xD8, 0xC8))
            .put("J", new TeamNameChar(0xD9, 0xC9))
            .put("K", new TeamNameChar(0xDA, 0xCA))
            .put("L", new TeamNameChar(0xDB, 0xCB))
            .put("M", new TeamNameChar(0xDC, 0xCC))
            .put("N", new TeamNameChar(0xDD, 0xCD))
            .put("O", new TeamNameChar(0xDE, 0xCE))
            .put("P", new TeamNameChar(0xDF, 0xCF))
            .put("Q", new TeamNameChar(0xF0, 0xE0))
            .put("R", new TeamNameChar(0xF1, 0xE1))
            .put("S", new TeamNameChar(0xF2, 0xE2))
            .put("T", new TeamNameChar(0xF3, 0xE3))
            .put("U", new TeamNameChar(0xF4, 0xE4))
            .put("V", new TeamNameChar(0xF5, 0xE5))
            .put("W", new TeamNameChar(0xF6, 0xE6))
            .put("X", new TeamNameChar(0xF7, 0xE7))
            .put("Y", new TeamNameChar(0xF8, 0xE8))
            .put("Z", new TeamNameChar(0xF9, 0xE9))
            .put(" ", new TeamNameChar(0xFA, 0xEA))
            .build();
}

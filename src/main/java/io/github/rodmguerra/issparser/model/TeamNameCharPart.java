package io.github.rodmguerra.issparser.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamNameCharPart {

    private final boolean bottom;
    private final byte b1;
    private final byte b2;
    private final byte preferredSize;

    private TeamNameCharPart(boolean bottom, byte b1, byte b2) {
        this(bottom, b1, b2, (byte) 9);
    }

    private TeamNameCharPart(boolean bottom, byte b1, byte b2, byte preferredSize) {
        this.bottom = bottom;
        this.b1 = b1;
        this.b2 = b2;
        this.preferredSize = preferredSize;

    }

    public int cutLeft() {
        if(this.getText().startsWith("I")) return 1;
        if(this.getText().startsWith(".")) return 2;
        return 0;
    }


    public static List<TeamNameCharPart> forLetter(String letter) {
        List<TeamNameCharPart> parts = new ArrayList<>();
        TeamNameCharPart topAndBottom = topAndBottoms().get(letter);
        if (topAndBottom != null) {
            parts.add(topAndBottom);
            return parts;
        }

        TeamNameCharPart bottom = bottoms.get(letter);
        if (bottom != null) parts.add(bottom);
        TeamNameCharPart top = tops.get(letter);
        if (top != null) parts.add(top);
        return parts;
    }

    public static List<TeamNameCharPart> forLetter(char letter) {
        return forLetter("" + letter);
    }

    @Override
    public String toString() {
        //return ParsingUtils.bytesString(new byte[] { top, bottom });
        String topAndBottomString = topAndBottoms().inverse().get(this);
        if (topAndBottomString != null) {
            return topAndBottomString + " (top and bottom)";
        }

        String topString = tops.inverse().get(this);
        if (topString != null) {
            return topString + " (top)";
        }

        String bottomString = bottoms.inverse().get(this);
        if (bottomString != null) {
            return bottomString + " (bottom)";
        }

        return "";
    }

    public String getText() {
        //return ParsingUtils.bytesString(new byte[] { top, bottom });
        String topAndBottomString = topAndBottoms().inverse().get(this);
        if (topAndBottomString != null) return topAndBottomString;
        String topString = tops.inverse().get(this);
        if (topString != null) return topString;
        String bottomString = bottoms.inverse().get(this);
        if (bottomString != null) return bottomString;
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamNameCharPart that = (TeamNameCharPart) o;

        if (b1 != that.b1) return false;
        if (b2 != that.b2) return false;
        if (bottom != that.bottom) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (bottom ? 1 : 0);
        result = 31 * result + (int) b1;
        result = 31 * result + (int) b2;
        return result;
    }

    public byte getPreferredSize() {
        return preferredSize;
    }


    private static final BiMap<String, TeamNameCharPart> bottoms = ImmutableBiMap.<String, TeamNameCharPart>builder()
            .put("A", new TeamNameCharPart(true, (byte) 0xD0, (byte) 0x06))
            .put("B", new TeamNameCharPart(true, (byte) 0xD1, (byte) 0x06))
            .put("C", new TeamNameCharPart(true, (byte) 0xD2, (byte) 0x06))
            .put("D", new TeamNameCharPart(true, (byte) 0xD3, (byte) 0x06))
            .put("E", new TeamNameCharPart(true, (byte) 0xD4, (byte) 0x06))
            .put("F", new TeamNameCharPart(true, (byte) 0xD5, (byte) 0x06))
            .put("G", new TeamNameCharPart(true, (byte) 0xD6, (byte) 0x06))
            .put("H", new TeamNameCharPart(true, (byte) 0xD7, (byte) 0x06))
            .put("I", new TeamNameCharPart(true, (byte) 0xD8, (byte) 0x06, (byte) 7))
            .put("J", new TeamNameCharPart(true, (byte) 0xD9, (byte) 0x06))
            .put("K", new TeamNameCharPart(true, (byte) 0xDA, (byte) 0x06))
            .put("L", new TeamNameCharPart(true, (byte) 0xDB, (byte) 0x06))
            .put("M", new TeamNameCharPart(true, (byte) 0xDC, (byte) 0x06, (byte) 8))
            .put("N", new TeamNameCharPart(true, (byte) 0xDD, (byte) 0x06, (byte) 8))
            .put("O", new TeamNameCharPart(true, (byte) 0xDE, (byte) 0x06))
            .put("P", new TeamNameCharPart(true, (byte) 0xDF, (byte) 0x06))
            .put("Q", new TeamNameCharPart(true, (byte) 0xF0, (byte) 0x06))
            .put("R", new TeamNameCharPart(true, (byte) 0xF1, (byte) 0x06))
            .put("S", new TeamNameCharPart(true, (byte) 0xF2, (byte) 0x06))
            .put("T", new TeamNameCharPart(true, (byte) 0xF3, (byte) 0x06, (byte) 8))
            .put("U", new TeamNameCharPart(true, (byte) 0xF4, (byte) 0x06))
            .put("V", new TeamNameCharPart(true, (byte) 0xF5, (byte) 0x06))
            .put("W", new TeamNameCharPart(true, (byte) 0xF6, (byte) 0x06, (byte) 8))
            .put("X", new TeamNameCharPart(true, (byte) 0xF7, (byte) 0x06))
            .put("Y", new TeamNameCharPart(true, (byte) 0xF8, (byte) 0x06))
            .put("Z", new TeamNameCharPart(true, (byte) 0xF9, (byte) 0x06))
            .put(".", new TeamNameCharPart(true, (byte) 0xFA, (byte) 0x06, (byte) 7))
            .put("0", new TeamNameCharPart(true, (byte) 0xB0, (byte) 0x06))
            .put("1", new TeamNameCharPart(true, (byte) 0xB1, (byte) 0x06))
            .put("2", new TeamNameCharPart(true, (byte) 0xB2, (byte) 0x06))
            .put("3", new TeamNameCharPart(true, (byte) 0xB3, (byte) 0x06))
            .put("4", new TeamNameCharPart(true, (byte) 0xB4, (byte) 0x06))
            .put("5", new TeamNameCharPart(true, (byte) 0xB5, (byte) 0x06))
            .put("6", new TeamNameCharPart(true, (byte) 0xB6, (byte) 0x06))
            .put("7", new TeamNameCharPart(true, (byte) 0xB7, (byte) 0x06))
            .put("8", new TeamNameCharPart(true, (byte) 0xB8, (byte) 0x06))
            .put("9", new TeamNameCharPart(true, (byte) 0xB9, (byte) 0x06))
            .build();

    private static final BiMap<String, TeamNameCharPart> tops = ImmutableBiMap.<String, TeamNameCharPart>builder()
            .put("A", new TeamNameCharPart(false, (byte) 0xC0, (byte) 0x06))
            .put("B", new TeamNameCharPart(false, (byte) 0xC1, (byte) 0x06))
            .put("C", new TeamNameCharPart(false, (byte) 0xC2, (byte) 0x06))
            .put("D", new TeamNameCharPart(false, (byte) 0xC3, (byte) 0x06))
            .put("E", new TeamNameCharPart(false, (byte) 0xC4, (byte) 0x06))
            .put("F", new TeamNameCharPart(false, (byte) 0xC5, (byte) 0x06))
            .put("G", new TeamNameCharPart(false, (byte) 0xC6, (byte) 0x06))
            .put("H", new TeamNameCharPart(false, (byte) 0xC7, (byte) 0x06))
            .put("I", new TeamNameCharPart(false, (byte) 0xC8, (byte) 0x06, (byte) 7))
            .put("J", new TeamNameCharPart(false, (byte) 0xC9, (byte) 0x06))
            .put("K", new TeamNameCharPart(false, (byte) 0xCA, (byte) 0x06))
            .put("L", new TeamNameCharPart(false, (byte) 0xCB, (byte) 0x06))
            .put("M", new TeamNameCharPart(false, (byte) 0xCC, (byte) 0x06, (byte) 8))
            .put("N", new TeamNameCharPart(false, (byte) 0xCD, (byte) 0x06, (byte) 8))
            .put("O", new TeamNameCharPart(false, (byte) 0xCE, (byte) 0x06))
            .put("P", new TeamNameCharPart(false, (byte) 0xCF, (byte) 0x06))
            .put("Q", new TeamNameCharPart(false, (byte) 0xE0, (byte) 0x06))
            .put("R", new TeamNameCharPart(false, (byte) 0xE1, (byte) 0x06))
            .put("S", new TeamNameCharPart(false, (byte) 0xE2, (byte) 0x06))
            .put("T", new TeamNameCharPart(false, (byte) 0xE3, (byte) 0x06, (byte) 8))
            .put("U", new TeamNameCharPart(false, (byte) 0xE4, (byte) 0x06))
            .put("V", new TeamNameCharPart(false, (byte) 0xE5, (byte) 0x06))
            .put("W", new TeamNameCharPart(false, (byte) 0xE6, (byte) 0x06, (byte) 8))
            .put("X", new TeamNameCharPart(false, (byte) 0xE7, (byte) 0x06))
            .put("Y", new TeamNameCharPart(false, (byte) 0xE8, (byte) 0x06))
            .put("Z", new TeamNameCharPart(false, (byte) 0xE9, (byte) 0x06))
            .put("0", new TeamNameCharPart(false, (byte) 0xA0, (byte) 0x06))
            .put("1", new TeamNameCharPart(false, (byte) 0xA1, (byte) 0x06))
            .put("2", new TeamNameCharPart(false, (byte) 0xA2, (byte) 0x06))
            .put("3", new TeamNameCharPart(false, (byte) 0xA3, (byte) 0x06))
            .put("4", new TeamNameCharPart(false, (byte) 0xA4, (byte) 0x06))
            .put("5", new TeamNameCharPart(false, (byte) 0xA5, (byte) 0x06))
            .put("6", new TeamNameCharPart(false, (byte) 0xA6, (byte) 0x06))
            .put("7", new TeamNameCharPart(false, (byte) 0xA7, (byte) 0x06))
            .put("8", new TeamNameCharPart(false, (byte) 0xA8, (byte) 0x06))
            .put("9", new TeamNameCharPart(false, (byte) 0xA9, (byte) 0x06))
            .build();


    private static BiMap<String, TeamNameCharPart> topAndBottoms;

    private static BiMap<String, TeamNameCharPart> topAndBottoms() {
        if (topAndBottoms == null) {
            Map<String, TeamNameCharPart> map = new HashMap<>();
            for (String letter : tops.keySet()) {
                if(letter.equals("Z") || letter.equals("9")) continue;
                TeamNameCharPart part = tops.get(letter);
                String nextChar = String.valueOf((char) (letter.charAt(0) + 1));
                map.put(letter + nextChar, new TeamNameCharPart(
                        part.isBottom(),
                        part.getB1(),
                        (byte) 0x16,
                        (byte) (part.getPreferredSize() + tops.get(nextChar).getPreferredSize() - 2)
                ));

               // System.out.println("Size: " + map.get(letter + nextChar));
                //System.out.println(map.get(letter + nextChar).getPreferredSize());


            }

            TeamNameCharPart part = map.remove("PQ");
            map.put("PA", part);

            topAndBottoms = ImmutableBiMap.copyOf(map);
        }
        return topAndBottoms;
    }


            /*= ImmutableBiMap.<String, TeamNameCharPart>builder()
            .put("DE", new TeamNameCharPart(false, (byte) 0xC3, (byte) 0x16, (byte) 16))
            .put("NO", new TeamNameCharPart(false, (byte) 0xCD, (byte) 0x16, (byte) 16))
            .build();  */


    public boolean isBottom() {
        return bottom;
    }

    public byte getB1() {
        return b1;
    }

    public byte getB2() {
        return b2;
    }

    public static TeamNameCharPart deserialize(byte[] partData) {
        if (partData[0] == (byte) 0xF9) {
            for (String partLetter : bottoms.keySet()) {
                TeamNameCharPart bottom = bottoms.get(partLetter);
                if (bottom.getB1() == partData[2] && bottom.getB2() == partData[3]) {
                    return bottom;
                }
            }
        } else {
            for (String partLetter : topAndBottoms().keySet()) {
                TeamNameCharPart topAndBottom = topAndBottoms().get(partLetter);
                if (topAndBottom.getB1() == partData[2] && topAndBottom.getB2() == partData[3]) {
                    return topAndBottom;
                }
            }

            for (String partLetter : tops.keySet()) {
                TeamNameCharPart top = tops.get(partLetter);
                if (top.getB1() == partData[2] && top.getB2() == partData[3]) {
                    return top;
                }
            }
        }
        return null;
        //throw new IllegalArgumentException(ParsingUtils.bytesString(partData));
    }
}

package io.github.rodmguerra.issparser.model;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.handlers.texts.TeamNameCharPart;

import java.nio.ByteBuffer;
import java.util.*;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public class TeamNameInMenu {
    private final Multimap<Byte, TeamNameCharPart> map = ArrayListMultimap.create();

    public static TeamNameInMenu forText(String text) {
        text = ParsingUtils.stripAccents(text).toUpperCase().replaceAll("[^A-Z\\. ]", " ");
        System.out.println(text);
        Multimap<Integer, TeamNameCharPart> zeroMap = ArrayListMultimap.create();
        int currentPosition = 0;

        for (int i = 0; i < text.length(); i++) {
            //whitespace
            if (Character.isWhitespace(text.charAt(i))) {
                currentPosition += 3;
            } else {
                //two letters
                if (i + 1 < text.length()) {
                    List<TeamNameCharPart> parts = TeamNameCharPart.forLetter("" + text.charAt(i) + text.charAt(i + 1));
                    if (parts.size() > 0) {
                        TeamNameCharPart basePart = parts.get(0);
                        if(i>0) currentPosition -= basePart.cutLeft();
                        zeroMap.putAll(currentPosition, parts);
                        currentPosition += basePart.getPreferredSize();
                        i++;
                        continue;
                    }
                }
                //one letter
                List<TeamNameCharPart> parts = TeamNameCharPart.forLetter(text.charAt(i));
                TeamNameCharPart basePart = parts.get(0);
                if(i>0) currentPosition -= basePart.cutLeft();
                zeroMap.putAll(currentPosition, parts);
                currentPosition += basePart.getPreferredSize();
            }
        }
        int currentSize = currentPosition - 1;
        int maximumSize = 70;
        if(currentSize > maximumSize) {
            int deltaSize = currentSize - maximumSize;
            int numberOflettersToMove = zeroMap.keySet().size() - 1;
            int compressionRate = deltaSize / numberOflettersToMove;
            int remainder = deltaSize % numberOflettersToMove;
            //System.out.println("compressionRate " + compressionRate);
            zeroMap = compress(zeroMap, compressionRate, remainder);
            currentSize = maximumSize;
        }

        int positionDelta = currentSize / 2;


        TeamNameInMenu teamName = new TeamNameInMenu();
        for (int zeroPosition : zeroMap.keySet()) {
            int newPosition = zeroPosition - positionDelta;
            teamName.map.putAll((byte) newPosition, zeroMap.get(zeroPosition));
        }


        return teamName;
    }

    private static Multimap<Integer, TeamNameCharPart> compress(Multimap<Integer, TeamNameCharPart> zeroMap, int compression, int remainder) {
        Multimap<Integer, TeamNameCharPart> map = ArrayListMultimap.create();
        List<Integer> sorted =  zeroMap.keySet().stream().sorted(naturalOrder()).collect(toList());
        int i=0;
        int displacement = 0;
        for (int zeroPosition : sorted) {
            if(i>0) {
                displacement+= compression;
                if(remainder > 0) {
                    displacement++;
                    remainder--;
                }
            }
            int newPosition = zeroPosition - displacement;
            map.putAll(newPosition, zeroMap.get(zeroPosition));
            i++;
        }
        return map;
    }

    public String getText() {
        String text = "";
        List<Byte> positions = map.keySet().stream().sorted(naturalOrder()).collect(toList());
        byte lastPosition = Byte.MAX_VALUE;
        int lastLetterSize = Integer.MAX_VALUE;
        for (byte position : positions) {
            String currentLetter = "";
            for (TeamNameCharPart part : map.get(position)) {
                currentLetter = part.getText();
                if (!currentLetter.isEmpty()) {
                    //espaçamento maior que 10 considera um espaço
                    if (position >= lastPosition + 10 * lastLetterSize) {
                        text += " ";
                    }
                    text += currentLetter;
                    lastPosition = position;
                    lastLetterSize = currentLetter.length();
                    break;
                }
            }
        }
        return text;
    }

    public byte getByteSize() {
        return (byte) map.size();
    }

    public byte[] serialize() {
        List<Byte> bytes = new ArrayList<>();
        bytes.add(getByteSize());
        List<Byte> positions = map.keySet().stream().sorted(naturalOrder()).collect(toList());
        for (Byte position : positions) {
            for (TeamNameCharPart part : map.get(position)) {
                bytes.add(part.isBottom() ? (byte) 0xF9 : (byte) 0xF1);
                bytes.add(position);
                bytes.add(part.getB1());
                bytes.add(part.getB2());
            }
        }
        return Bytes.toArray(bytes);
    }

    public static TeamNameInMenu deserialize(byte[] data) {
        TeamNameInMenu teamName = new TeamNameInMenu();
        int charPartLength = data.length / 4;
        List<TeamNameCharPart> chars = new ArrayList<>(charPartLength);
        ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(data, 1, data.length));
        for (int i = 0; i < charPartLength; i++) {
            byte[] partData = new byte[4];
            buffer.get(partData, 0, 4);
            TeamNameCharPart part = TeamNameCharPart.deserialize(partData);
            if(part != null) teamName.map.put(partData[1], part);
        }
        return teamName;
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getNumberOfPositions() {
        return map.keySet().size();
    }

    public int getLetterSize(int position) {
        List<Byte> positions = map.keySet().stream().sorted(naturalOrder()).collect(toList());
        if(position >= positions.size() - 1) return 0;
        return positions.get(position+1) - positions.get(position);
    }

    public Collection<TeamNameCharPart> getLetterAt(int position) {
        List<Byte> positions = map.keySet().stream().sorted(naturalOrder()).collect(toList());
        return map.get(positions.get(position));
    }




}

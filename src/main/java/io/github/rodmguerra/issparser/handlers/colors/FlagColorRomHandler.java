package io.github.rodmguerra.issparser.handlers.colors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FlagColorRomHandler implements RomHandler<ColoredPart> {
    private final File rom;


    private static final int COLOR_BYTE_COUNT = 2;
    private static final int COLOR_COUNT = 4;

    private final ImmutableList<Team> range1;
    private final ImmutableList<Team> range2;
    private static final long RANGE1_OFFSET = 0x2DD91            ;
    private static final long RANGE2_OFFSET = 0x2DE4F;
    private static final int STEP = 10;

    public FlagColorRomHandler(File rom) {
        this.rom = rom;
        range1 = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ENGLAND, Team.ITALY, Team.HOLLAND, Team.FRANCE,
                Team.SPAIN, Team.BELGIUM, Team.IRELAND, Team.COLOMBIA, Team.BRAZIL,
                Team.ARGENTINA, Team.MEXICO, Team.NIGERIA, Team.CAMEROON, Team.USA,
                Team.BULGARIA, Team.ROMANIA, Team.SWEDEN).build();

        range2 = ImmutableList.<Team>builder().add(
                Team.SCOTLAND, Team.SKOREA,  Team.SUPERSTAR, Team.RUSSIA, Team.SWITZ,
                Team.DENMARK, Team.AUSTRIA,Team.WALES, Team.NORWAY).build();
    }

    public Map<Team, ColoredPart> readFromRom() throws IOException {
        ByteSource file = Files.asByteSource(rom);

        ByteSource fRange1 = file.slice(RANGE1_OFFSET, range1.size() * STEP);
        ByteSource fRange2 = file.slice(RANGE2_OFFSET, range2.size() * STEP);
        byte[] bytes = ByteSource.concat(fRange1, fRange2).read();

        Iterable<Team> teamKitPositions = Iterables.concat(range1, range2);
        Map<Team, ColoredPart> flagColors = new HashMap<>();
        int i = 0;
        for (Team team : teamKitPositions) {
            flagColors.put(team, parseColors(bytes, STEP * i));
            i++;
        }
        return flagColors;
    }


    private RGB parseRGB(byte b1, byte b2) {
        byte[] bytes = new byte[]{b2, b1};
        short number = ByteBuffer.wrap(bytes).getShort();
        int red = number % 0x20;
        int green = number % 0x400 / 0x20;
        int blue = number % 0x8000 / 0x400;
        return new RGB(red, green, blue);
    }

    private byte[] serialize(RGB rgb) {
        short number = (short) (rgb.getBlue() * 0x400 + rgb.getGreen() * 0x20 + rgb.getRed());
        byte[] outputBytes = ByteBuffer.allocate(2).putShort(number).array();
        return new byte[]{outputBytes[1], outputBytes[0]};
    }

    private ColoredPart parseColors(byte[] bytes, int offset) {
        RGB[] partColors = new RGB[COLOR_COUNT];
        for (int i = 0; i < COLOR_COUNT; i++) {
            partColors[i] = parseRGB(bytes[offset + i * 2], bytes[offset + i * 2 + 1]);
        }
        return new ColoredPart(partColors);
    }


    @Override
    public ColoredPart readFromRomAt(Team team) throws IOException {
        System.out.println("Reading colors of flag " + team);

        ByteSource source = Files.asByteSource(rom);
        byte[] bytes = source.slice(getOffset(team), STEP).read();

        return parseColors(bytes, 0);
    }

    @Override
    public void writeToRomAt(Team team, ColoredPart colors) throws IOException {
        FileUtils.writeToPosition(rom, serialize(colors), getOffset(team));
    }

    private byte[] serialize(ColoredPart part) {
        return Bytes.concat(Stream.of(part.getRgbs()).map(this::serialize).toArray(byte[][]::new));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new FlagColorRomHandler(new File("../International Superstar Soccer (Europe).sfc")).readFromRomAt(Team.GERMANY));
    }

    private long getOffset(Team team) {
        int position = range1.indexOf(team);
        return (position >= 0) ?
                RANGE1_OFFSET + position * STEP :
                RANGE2_OFFSET + range2.indexOf(team) * STEP;
    }
}

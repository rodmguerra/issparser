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
import io.github.rodmguerra.issparser.model.colors.hairandskin.HairAndSkin;
import io.github.rodmguerra.issparser.model.colors.hairandskin.TeamHairAndSkin;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HairAndSkinRomHandler implements RomHandler<TeamHairAndSkin> {
    private final File rom;


    private static final int COLOR_BYTE_COUNT = 2;
    private static final int HAIR_COLOR_COUNT = 1;
    private static final int OUTFIELD_SKIN_COLOR_COUNT = 5;
    private static final int KEEPER_SKIN_COLOR_COUNT = 3;

    private final ImmutableList<Team> range1;
    private final ImmutableList<Team> keeperRange1;
    private final ImmutableList<Team> range2;
    private static final long FIRST_RANGE1_OFFSET = 0x2EA3B  - 12;
    private static final long SECOND_RANGE1_OFFSET = 0x2ECBB - 12;
    private static final long FIRST_RANGE2_OFFSET = 0x2F0EB - 12;
    private static final long SECOND_RANGE2_OFFSET = 0x2F1EB - 12;
    private static final int OUTFIELD_STEP = 32;
    private static final long KEEPER_RANGE1_OFFSET = 0x2EF37 - 8;
    private static final long KEEPER_RANGE2_OFFSET = 0x2F2E7 - 8;
    private static final int KEEPER_STEP = 24;

    public HairAndSkinRomHandler(File rom) {
        this.rom = rom;
        range1 = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ITALY, Team.HOLLAND, Team.SPAIN, Team.ENGLAND,
                Team.FRANCE, Team.SWEDEN, Team.IRELAND, Team.BELGIUM, Team.ROMANIA,
                Team.BULGARIA, Team.ARGENTINA, Team.BRAZIL, Team.COLOMBIA, Team.MEXICO,
                Team.USA, Team.NIGERIA, Team.CAMEROON, Team.SUPERSTAR).build();

        keeperRange1 = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ITALY, Team.HOLLAND, Team.SPAIN, Team.ENGLAND,
                Team.FRANCE, Team.SWEDEN, Team.IRELAND, Team.BELGIUM, Team.ROMANIA,
                Team.BULGARIA, Team.ARGENTINA, Team.BRAZIL, Team.COLOMBIA, Team.MEXICO,
                Team.USA, Team.NIGERIA, Team.CAMEROON).build();

        range2 = ImmutableList.<Team>builder().add(
                Team.RUSSIA, Team.SCOTLAND, Team.SKOREA, Team.WALES, Team.NORWAY,
                Team.SWITZ, Team.DENMARK, Team.AUSTRIA).build();
    }

    public Map<Team, TeamHairAndSkin> readFromRom() throws IOException {
        ByteSource file = Files.asByteSource(rom);

        ByteSource fRange1 = file.slice(FIRST_RANGE1_OFFSET, range1.size() * OUTFIELD_STEP);
        ByteSource fRange2 = file.slice(FIRST_RANGE2_OFFSET, range2.size() * OUTFIELD_STEP);
        byte[] firstKitBytes = ByteSource.concat(fRange1, fRange2).read();

        ByteSource sRange1 = file.slice(SECOND_RANGE1_OFFSET, range1.size() * OUTFIELD_STEP);
        ByteSource sRange2 = file.slice(SECOND_RANGE2_OFFSET, range2.size() * OUTFIELD_STEP);
        byte[] secondKitBytes = ByteSource.concat(sRange1, sRange2).read();

        //No superstar goalkeeper (shared with belgium)
        ByteSource kRange1 = file.slice(KEEPER_RANGE1_OFFSET, keeperRange1.size() * KEEPER_STEP);
        ByteSource kRange2 = file.slice(KEEPER_RANGE2_OFFSET, range2.size() * KEEPER_STEP);
        byte[] keeperKitBytes = ByteSource.concat(kRange1, kRange2).read();

        Iterable<Team> teamKitPositions = Iterables.concat(range1, range2);
        Map<Team, HairAndSkin> firstKits = new HashMap<>();
        Map<Team, HairAndSkin> secondKits = new HashMap<>();
        int i = 0;
        for (Team team : teamKitPositions) {
            firstKits.put(team, parseHairAndSkin(firstKitBytes, OUTFIELD_STEP * i));
            secondKits.put(team, parseHairAndSkin(secondKitBytes, OUTFIELD_STEP * i));
            i++;
        }

        Iterable<Team> keeperKitPositions = Iterables.concat(this.keeperRange1, this.range2);
        Map<Team, HairAndSkin> keeperKits = new HashMap<>();
        i = 0;
        for (Team team : keeperKitPositions) {
            keeperKits.put(team, parseHairAndSkin(keeperKitBytes, KEEPER_STEP * i++));
        }

        Map<Team, TeamHairAndSkin> kits = new HashMap<>();
        for (Team team : firstKits.keySet()) {
            kits.put(team, new TeamHairAndSkin(firstKits.get(team), secondKits.get(team), keeperKits.get(team)));
        }

        return kits;
    }

    private HairAndSkin parseHairAndSkin(byte[] bytes, int i) {
        int cursor = 0;
        ColoredPart hair = parsePart(bytes, i + cursor, HAIR_COLOR_COUNT);
        cursor += HAIR_COLOR_COUNT * COLOR_BYTE_COUNT;
        ColoredPart skin = parsePart(bytes, i + cursor, OUTFIELD_SKIN_COLOR_COUNT);
        return new HairAndSkin(hair, skin);
    }

    private HairAndSkin parseKeeperHairAndSkin(byte[] bytes, int i) {
        int cursor = 0;
        ColoredPart hair = parsePart(bytes, i + cursor, HAIR_COLOR_COUNT);
        cursor += HAIR_COLOR_COUNT * COLOR_BYTE_COUNT;
        ColoredPart skin = parsePart(bytes, i + cursor, KEEPER_SKIN_COLOR_COUNT);
        return new HairAndSkin(hair, skin);
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

    private ColoredPart parsePart(byte[] bytes, int offset, int partColorCount) {
        RGB[] partColors = new RGB[partColorCount];
        for (int i = 0; i < partColorCount; i++) {
            partColors[i] = parseRGB(bytes[offset + i * 2], bytes[offset + i * 2 + 1]);
        }
        return new ColoredPart(partColors);
    }

    public void writeToRom(Map<Team, ? extends TeamHairAndSkin> kits) throws IOException {  /*
        StringBuilder sb = new StringBuilder();
        for (Iterable<String> teamPlayers : playersByTeam) {
            for (String teamPlayer : teamPlayers) {
                sb.append(io.github.rodmguerra.issparser.commons.ParsingUtils.cutAndCenter(teamPlayer, NAME_LENGTH));
            }
        }
        byte[] bytes = io.github.rodmguerra.issparser.commons.ParsingUtils.issBytes(sb);
        System.out.println(io.github.rodmguerra.issparser.commons.ParsingUtils.bytesString(bytes));
        io.github.rodmguerra.issparser.commons.FileUtils.writeToPosition(rom, bytes, RANGE1_OFFSET);   */

        //writePredominantColor(team, teamKits);
    }

    @Override
    public TeamHairAndSkin readFromRomAt(Team team) throws IOException {
        System.out.println("Reading hairs and skins of team " + team);

        ByteSource source = Files.asByteSource(rom);
        byte[] firstKitBytes = source.slice(getFirstKitOffset(team), OUTFIELD_STEP).read();
        byte[] secondKitBytes = source.slice(getSecondKitOffset(team), OUTFIELD_STEP).read();
        byte[] keeperKitBytes = source.slice(getGoalkeeperKitOffset(team), KEEPER_STEP).read();

        return new TeamHairAndSkin(parseHairAndSkin(firstKitBytes, 0), parseHairAndSkin(secondKitBytes, 0), parseKeeperHairAndSkin(keeperKitBytes, 0));
    }

    @Override
    public void writeToRomAt(Team team, TeamHairAndSkin teamHairAndSkin) throws IOException {
        FileUtils.writeToPosition(rom, serialize(teamHairAndSkin.getFirst()), getFirstKitOffset(team));
        FileUtils.writeToPosition(rom, serialize(teamHairAndSkin.getSecond()), getSecondKitOffset(team));
        FileUtils.writeToPosition(rom, serialize(teamHairAndSkin.getGoalkeeper()), getGoalkeeperKitOffset(team));
    }


    private byte[] serialize(HairAndSkin hairAndSkin) {
        return Bytes.concat(serialize(hairAndSkin.getHair()), serialize(hairAndSkin.getSkin()));
    }

    private byte[] serialize(ColoredPart part) {
        return Bytes.concat(Stream.of(part.getRgbs()).map(this::serialize).toArray(byte[][]::new));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new HairAndSkinRomHandler(new File("isse.sfc")).readFromRomAt(Team.GERMANY).getSecond().getSkin());
    }

    private long getFirstKitOffset(Team team) {
        int position = range1.indexOf(team);
        return (position >= 0) ?
                FIRST_RANGE1_OFFSET + position * OUTFIELD_STEP :
                FIRST_RANGE2_OFFSET + range2.indexOf(team) * OUTFIELD_STEP;
    }

    private long getSecondKitOffset(Team team) {
        int position = range1.indexOf(team);
        return (position >= 0) ?
                SECOND_RANGE1_OFFSET + position * OUTFIELD_STEP :
                SECOND_RANGE2_OFFSET + range2.indexOf(team) * OUTFIELD_STEP;
    }

    private long getGoalkeeperKitOffset(Team team) {
        if(team == Team.SUPERSTAR) return getGoalkeeperKitOffset(Team.BELGIUM);
        int position = range1.indexOf(team);
        if (position >= 0) return KEEPER_RANGE1_OFFSET + position * KEEPER_STEP;
        position = range2.indexOf(team);
        return KEEPER_RANGE2_OFFSET + position * KEEPER_STEP;
    }
}

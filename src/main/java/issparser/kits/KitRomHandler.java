package issparser.kits;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import issparser.commons.FileUtils;
import issparser.commons.RomHandler;
import issparser.kits.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class KitRomHandler implements RomHandler<TeamKits> {
    private final File rom;


    private static final int COLOR_BYTE_COUNT = 2;
    private static final int SHIRT_COLOR_COUNT = 3;
    private static final int SHORTS_COLOR_COUNT = 3;
    private static final int SOCKS_COLOR_COUNT = 2;
    private final ImmutableList<Team> range1;
    private final ImmutableList<Team> keeperRange1;
    private final ImmutableList<Team> range2;
    private static final long FIRST_KIT_RANGE1_OFFSET = 0x2EA3B;
    private static final long SECOND_KIT_RANGE1_OFFSET = 0x2ECBB;
    private static final long FIRST_KIT_RANGE2_OFFSET = 0x2F0EB;
    private static final long SECOND_KIT_RANGE2_OFFSET = 0x2F1EB;
    private static final int TEAM_KIT_STEP = 32;
    private static final long KEEPER_KIT_RANGE1_OFFSET = 0x2EF37;
    private static final long KEEPER_KIT_RANGE2_OFFSET = 0x2F2E7;
    private static final int KEEPER_KIT_STEP = 24;
    private static final long PREDOMINANT_COLOR_OFFSET = 0x8DB2;

    public KitRomHandler(File rom) {
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

    public Map<Team, TeamKits> readFromRom() throws IOException {
        ByteSource file = Files.asByteSource(rom);

        ByteSource fRange1 = file.slice(FIRST_KIT_RANGE1_OFFSET, range1.size() * TEAM_KIT_STEP);
        ByteSource fRange2 = file.slice(FIRST_KIT_RANGE2_OFFSET, range2.size() * TEAM_KIT_STEP);
        byte[] firstKitBytes = ByteSource.concat(fRange1, fRange2).read();

        ByteSource sRange1 = file.slice(SECOND_KIT_RANGE1_OFFSET, range1.size() * TEAM_KIT_STEP);
        ByteSource sRange2 = file.slice(SECOND_KIT_RANGE2_OFFSET, range2.size() * TEAM_KIT_STEP);
        byte[] secondKitBytes = ByteSource.concat(sRange1, sRange2).read();

        //No superstar goalkeeper (shared with belgium)
        ByteSource kRange1 = file.slice(KEEPER_KIT_RANGE1_OFFSET, keeperRange1.size() * KEEPER_KIT_STEP);
        ByteSource kRange2 = file.slice(KEEPER_KIT_RANGE2_OFFSET, range2.size() * KEEPER_KIT_STEP);
        byte[] keeperKitBytes = ByteSource.concat(kRange1, kRange2).read();

        Iterable<Team> teamKitPositions = Iterables.concat(range1, range2);
        Map<Team, Kit> firstKits = new HashMap<>();
        Map<Team, Kit> secondKits = new HashMap<>();
        int i = 0;
        for (Team team : teamKitPositions) {
            firstKits.put(team, parseKit(firstKitBytes, TEAM_KIT_STEP * i));
            secondKits.put(team, parseKit(secondKitBytes, TEAM_KIT_STEP * i));
            i++;
        }

        Iterable<Team> keeperKitPositions = Iterables.concat(this.keeperRange1, this.range2);
        Map<Team, KeeperKit> keeperKits = new HashMap<>();
        i = 0;
        for (Team team : keeperKitPositions) {
            keeperKits.put(team, parseKeeperKit(keeperKitBytes, KEEPER_KIT_STEP * i++));
        }

        Map<Team, TeamKits> kits = new HashMap<>();
        for (Team team : firstKits.keySet()) {
            ColorType predominantColor = readPredominantColor(team, file);
            kits.put(team, new TeamKits(firstKits.get(team), secondKits.get(team), keeperKits.get(team), predominantColor));
        }

        return kits;
    }

    private Kit parseKit(byte[] bytes, int i) {
        int cursor = 0;
        KitPart shirt = parsePart(bytes, i + cursor, SHIRT_COLOR_COUNT);
        cursor += SHIRT_COLOR_COUNT * COLOR_BYTE_COUNT;

        KitPart shorts = parsePart(bytes, i + cursor, SHORTS_COLOR_COUNT);

        cursor += SHORTS_COLOR_COUNT * COLOR_BYTE_COUNT;
        KitPart socks = parsePart(bytes, i + cursor, SOCKS_COLOR_COUNT);

        return new Kit(shirt, shorts, socks);
    }

    private KeeperKit parseKeeperKit(byte[] bytes, int i) {
        int cursor = 0;
        KitPart shirt = parsePart(bytes, i + cursor, 5);
        cursor += 5 * COLOR_BYTE_COUNT;

        KitPart shorts = parsePart(bytes, i + cursor, 1);

        return new KeeperKit(shirt, shorts);
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

    private KitPart parsePart(byte[] bytes, int offset, int partColorCount) {
        RGB[] partColors = new RGB[partColorCount];
        for (int i = 0; i < partColorCount; i++) {
            partColors[i] = parseRGB(bytes[offset + i * 2], bytes[offset + i * 2 + 1]);
        }
        return new KitPart(partColors);
    }

    public byte[] rgbToSNES(RGB rgb) {
        short rec = (short) (rgb.getBlue() * 0x400 + rgb.getGreen() * 0x20 + rgb.getRed());
        byte[] outputBytes = ByteBuffer.allocate(2).putShort(rec).array();
        return new byte[]{outputBytes[1], outputBytes[0]};
    }

    public void writeToRom(Map<Team, ? extends TeamKits> kits) throws IOException {  /*
        StringBuilder sb = new StringBuilder();
        for (Iterable<String> teamPlayers : playersByTeam) {
            for (String teamPlayer : teamPlayers) {
                sb.append(issparser.commons.ParsingUtils.cutAndCenter(teamPlayer, NAME_LENGTH));
            }
        }
        byte[] bytes = issparser.commons.ParsingUtils.issBytes(sb);
        System.out.println(issparser.commons.ParsingUtils.bytesString(bytes));
        issparser.commons.FileUtils.writeToPosition(rom, bytes, RANGE1_OFFSET);   */

        //writePredominantColor(team, teamKits);
    }

    @Override
    public TeamKits readFromRomAt(Team team) throws IOException {
        System.out.println("Reading kits of team " + team);

        ByteSource source = Files.asByteSource(rom);
        byte[] firstKitBytes = source.slice(getFirstKitOffset(team), TEAM_KIT_STEP).read();
        byte[] secondKitBytes = source.slice(getSecondKitOffset(team), TEAM_KIT_STEP).read();
        byte[] keeperKitBytes = source.slice(getGoalkeeperKitOffset(team), KEEPER_KIT_STEP).read();
        ColorType predominantColor = readPredominantColor(team, source);

        return new TeamKits(parseKit(firstKitBytes, 0), parseKit(secondKitBytes, 0), parseKeeperKit(keeperKitBytes, 0), predominantColor);
    }

    private ColorType readPredominantColor(Team team, ByteSource source) throws IOException {
        byte[] predominantColorByte = source.slice(PREDOMINANT_COLOR_OFFSET + team.ordinal(), 1).read();
        return ColorType.at(predominantColorByte[0]);
    }

    @Override
    public void writeToRomAt(Team team, TeamKits teamKits) throws IOException {
        FileUtils.writeToPosition(rom, serialize(teamKits.getFirst()), getFirstKitOffset(team));
        FileUtils.writeToPosition(rom, serialize(teamKits.getSecond()), getSecondKitOffset(team));
        FileUtils.writeToPosition(rom, serialize(teamKits.getGoalkeeper()), getGoalkeeperKitOffset(team));
        writePredominantColor(team, teamKits);
    }

    private void writePredominantColor(Team team, TeamKits teamKits) throws IOException {
        FileUtils.writeToPosition(rom, new byte[]{(byte) teamKits.getPredominantColor().ordinal()}, PREDOMINANT_COLOR_OFFSET + team.ordinal());
    }

    private byte[] serialize(Kit kit) {
        return Bytes.concat(serialize(kit.getShirt()), serialize(kit.getShorts()), serialize(kit.getSocks()));
    }

    private byte[] serialize(KeeperKit kit) {
        return Bytes.concat(serialize(kit.getShirtAndSocks()), serialize(kit.getShorts()));
    }

    private byte[] serialize(KitPart part) {
        return Bytes.concat(Stream.of(part.getRgbs()).map(this::serialize).toArray(byte[][]::new));
    }



    public static void main(String[] args) throws IOException {
        System.out.println(new KitRomHandler(new File("isse.sfc")).readFromRomAt(Team.GERMANY).getSecond().getShirt());
    }

    private long getFirstKitOffset(Team team) {
        int position = range1.indexOf(team);
        return (position >= 0) ?
                FIRST_KIT_RANGE1_OFFSET + position * TEAM_KIT_STEP :
                FIRST_KIT_RANGE2_OFFSET + range2.indexOf(team) * TEAM_KIT_STEP;
    }

    private long getSecondKitOffset(Team team) {
        int position = range1.indexOf(team);
        return (position >= 0) ?
                SECOND_KIT_RANGE1_OFFSET + position * TEAM_KIT_STEP :
                SECOND_KIT_RANGE2_OFFSET + range2.indexOf(team) * TEAM_KIT_STEP;
    }

    private long getGoalkeeperKitOffset(Team team) {
        if(team == Team.SUPERSTAR) return getGoalkeeperKitOffset(Team.BELGIUM);
        int position = range1.indexOf(team);
        if (position >= 0) return KEEPER_KIT_RANGE1_OFFSET + position * KEEPER_KIT_STEP;
        position = range2.indexOf(team);
        return KEEPER_KIT_RANGE2_OFFSET + position * KEEPER_KIT_STEP;
    }


}

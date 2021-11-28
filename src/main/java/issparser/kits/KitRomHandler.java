package issparser.kits;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import issparser.commons.FileUtils;
import issparser.commons.RomHandler;
import issparser.kits.model.Kit;
import issparser.kits.model.KitPart;
import issparser.kits.model.RGB;
import issparser.kits.model.TeamKits;

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
    private static final int TEAM_STEP = 32;

    private static final long FIRST_KIT_RANGE1_OFFSET = 0x2EA3B;
    private final ImmutableList<Team> firstKitRange1;

    private static final long SECOND_KIT_RANGE1_OFFSET = 0x2ECBB;
    private final ImmutableList<Team> secondKitRange1;

    private static final long FIRST_KIT_RANGE2_OFFSET = 0x2F0EB;
    private final ImmutableList<Team> firstKitRange2;

    private static final long SECOND_KIT_RANGE2_OFFSET = 0x2F1EB;
    private final ImmutableList<Team> secondKitRange2;

    public KitRomHandler(File rom) {
        this.rom = rom;
        firstKitRange1 = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ITALY, Team.HOLLAND, Team.SPAIN, Team.ENGLAND,
                Team.FRANCE, Team.SWEDEN, Team.IRELAND, Team.BELGIUM, Team.ROMANIA,
                Team.BULGARIA, Team.ARGENTINA, Team.BRAZIL, Team.COLOMBIA, Team.MEXICO,
                Team.USA, Team.NIGERIA, Team.CAMEROON, Team.SUPERSTAR).build();

        secondKitRange1 = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ITALY, Team.HOLLAND, Team.SPAIN, Team.ENGLAND,
                Team.FRANCE, Team.SWEDEN, Team.IRELAND, Team.BELGIUM, Team.ROMANIA,
                Team.BULGARIA, Team.ARGENTINA, Team.BRAZIL, Team.COLOMBIA, Team.MEXICO,
                Team.USA, Team.NIGERIA, Team.CAMEROON, Team.SUPERSTAR).build();

        firstKitRange2 = ImmutableList.<Team>builder().add(
                Team.RUSSIA, Team.SCOTLAND, Team.SKOREA, Team.WALES, Team.NORWAY,
                Team.SWITZ, Team.DENMARK, Team.AUSTRIA).build();

        secondKitRange2 = ImmutableList.copyOf(firstKitRange2);
    }

    public Map<Team, TeamKits> readFromRom() throws IOException {
        ByteSource file = Files.asByteSource(rom);
        ByteSource range1 = file.slice(FIRST_KIT_RANGE1_OFFSET, firstKitRange1.size() * TEAM_STEP);
        ByteSource range2 = file.slice(FIRST_KIT_RANGE2_OFFSET, firstKitRange2.size() * TEAM_STEP);
        byte[] firstKitBytes = ByteSource.concat(range1, range2).read();

        ByteSource sRange1 = file.slice(SECOND_KIT_RANGE1_OFFSET, secondKitRange1.size() * TEAM_STEP);
        ByteSource sRange2 = file.slice(SECOND_KIT_RANGE2_OFFSET, secondKitRange2.size() * TEAM_STEP);
        byte[] secondKitBytes = ByteSource.concat(sRange1, sRange2).read();

        Iterable<Team> firstKitPositions = Iterables.concat(firstKitRange1, firstKitRange2);
        Map<Team, Kit> firstKits = new HashMap<>();
        int i = 0;
        for (Team team : firstKitPositions) {
            firstKits.put(team, parseKit(firstKitBytes, TEAM_STEP * i++));
        }

        Iterable<Team> secondKitPositions = Iterables.concat(secondKitRange1, secondKitRange2);
        Map<Team, Kit> secondKits = new HashMap<>();
        i = 0;
        for (Team team : secondKitPositions) {
            secondKits.put(team, parseKit(secondKitBytes, TEAM_STEP * i++));
        }

        Map<Team, TeamKits> kits = new HashMap<>();
        for (Team team : firstKits.keySet()) {
            kits.put(team, new TeamKits(firstKits.get(team), secondKits.get(team)));
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
    }

    @Override
    public TeamKits readFromRomAt(Team team) throws IOException {
        System.out.println("Reading kits of team " + team);

        byte[] firstKitBytes = Files.asByteSource(rom).slice(getFirstKitOffset(team), TEAM_STEP).read();
        byte[] secondKitBytes = Files.asByteSource(rom).slice(getSecondKitOffset(team), TEAM_STEP).read();

        return new TeamKits(parseKit(firstKitBytes, 0), parseKit(secondKitBytes, 0));
    }

    @Override
    public void writeToRomAt(Team team, TeamKits teamKits) throws IOException {
        FileUtils.writeToPosition(rom, serialize(teamKits.getFirst()), getFirstKitOffset(team));
        FileUtils.writeToPosition(rom, serialize(teamKits.getSecond()), getSecondKitOffset(team));
    }

    private byte[] serialize(Kit kit) {
        return Bytes.concat(serialize(kit.getShirt()), serialize(kit.getShorts()), serialize(kit.getSocks()));
    }

    private byte[] serialize(KitPart part) {
        return Bytes.concat(Stream.of(part.getRgbs()).map(this::serialize).toArray(byte[][]::new));
    }



    public static void main(String[] args) throws IOException {
        System.out.println(new KitRomHandler(new File("isse.sfc")).readFromRomAt(Team.GERMANY).getSecond().getShirt());
    }

    private long getFirstKitOffset(Team team) {
        int position = firstKitRange1.indexOf(team);
        return (position >= 0) ?
                FIRST_KIT_RANGE1_OFFSET + position * TEAM_STEP :
                FIRST_KIT_RANGE2_OFFSET + firstKitRange2.indexOf(team) * TEAM_STEP;
    }

    private long getSecondKitOffset(Team team) {
        int position = secondKitRange1.indexOf(team);
        return (position >= 0) ?
                SECOND_KIT_RANGE1_OFFSET + position * TEAM_STEP :
                SECOND_KIT_RANGE2_OFFSET + secondKitRange2.indexOf(team) * TEAM_STEP;
    }


}

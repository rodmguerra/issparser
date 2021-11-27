package issparser.kits;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import issparser.commons.RomHandler;
import issparser.kits.model.Kit;
import issparser.kits.model.KitPart;
import issparser.kits.model.RGB;
import issparser.kits.model.TeamKits;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class KitRomHandler implements RomHandler<TeamKits> {
    private final File rom;

    private static final long OFFSET = 0x02EA3B;
    private static final int COLOR_BYTE_COUNT = 2;
    private static final int SHIRT_COLOR_COUNT = 3;
    private static final int SHORTS_COLOR_COUNT = 3;
    private static final int SOCKS_COLOR_COUNT = 2;
    private static final int TEAM_COUNT = 27;
    private static final int KIT_LENGTH = COLOR_BYTE_COUNT * (SHIRT_COLOR_COUNT + SHORTS_COLOR_COUNT + SOCKS_COLOR_COUNT);
    private static final  int LENGTH = TEAM_COUNT * KIT_LENGTH * 2;


    public KitRomHandler(File rom) {
        this.rom = rom;
    }

    public List<TeamKits> readFromRom() throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(OFFSET, LENGTH);
        byte[] bytes = source.read();

        List<TeamKits> teamKits = new ArrayList<>();

        for(int i=0; i<bytes.length; i+=(KIT_LENGTH*2)){
            teamKits.add(new TeamKits(parseKit(bytes, i), parseKit(bytes, i+KIT_LENGTH)));
        }

        return teamKits;
    }

    private Kit parseKit(byte[] bytes, int i) {
        int cursor = 0;
        KitPart shirt = parsePart(bytes, i+cursor, SHIRT_COLOR_COUNT);
        cursor += SHIRT_COLOR_COUNT * COLOR_BYTE_COUNT;

        KitPart shorts = parsePart(bytes, i+cursor, SHORTS_COLOR_COUNT);

        cursor += SHORTS_COLOR_COUNT * COLOR_BYTE_COUNT;
        KitPart socks = parsePart(bytes, i+cursor, SOCKS_COLOR_COUNT);
                 /*
            System.out.println(rgb);
            System.out.println(issparser.commons.ParsingUtils.bytesString(rgbToSNES(rgb))); */
        return new Kit(shirt, shorts, socks);
    }

    private RGB parseRGB(byte b1, byte b2) {
        byte[] bytes = new byte[]{b2, b1};
        short number = ByteBuffer.wrap(bytes).getShort();
        int red = number%0x20;
        int green = number%0x400/0x20;
        int blue = number%0x8000/0x400;
        return new RGB(red, green, blue);
    }

    private KitPart parsePart(byte[] bytes, int offset, int partColorCount) {
        RGB[] partColors = new RGB[partColorCount];
        for(int i=0; i<partColorCount; i++){
            partColors[i] = parseRGB(bytes[offset+i],bytes[i+offset+i+1]);
        }
        return new KitPart(partColors);
    }

    public byte[] rgbToSNES(RGB rgb) {
        short rec = (short) (rgb.getBlue() * 0x400 + rgb.getGreen() * 0x20 + rgb.getRed());
        byte[] outputBytes = ByteBuffer.allocate(2).putShort(rec).array();
        return new byte[] {outputBytes[1], outputBytes[0]};
    }

    public void writeToRom(Iterable<TeamKits> kits) throws IOException {  /*
        StringBuilder sb = new StringBuilder();
        for (Iterable<String> teamPlayers : playersByTeam) {
            for (String teamPlayer : teamPlayers) {
                sb.append(issparser.commons.ParsingUtils.center(teamPlayer, NAME_LENGTH));
            }
        }
        byte[] bytes = issparser.commons.ParsingUtils.issBytes(sb);
        System.out.println(issparser.commons.ParsingUtils.bytesString(bytes));
        issparser.commons.FileUtils.writeToPosition(rom, bytes, OFFSET);   */
    }

    public static void main(String[] args) throws IOException {
        new KitRomHandler(new File("isse.sfc")).readFromRom();
    }



}

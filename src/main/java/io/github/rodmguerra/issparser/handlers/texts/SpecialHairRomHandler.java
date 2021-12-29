package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.SpecialHair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.rodmguerra.issparser.commons.ParsingUtils.arrayOf;
import static io.github.rodmguerra.issparser.commons.ParsingUtils.listOf;

public class SpecialHairRomHandler implements RomHandler<SpecialHair> {
    private final File rom;
    private final long offset;

    public static final long OFFSET_ORIGINAL = 0x8D74;
    private static final int TEAM_LENGTH = 2;
    ImmutableBiMap<SpecialHair, List<Byte>> map = ImmutableBiMap.<SpecialHair, List<Byte>>of(
            SpecialHair.REGULAR, ImmutableList.of((byte) 0x00, (byte) 0x00),
            SpecialHair.BLOND, ImmutableList.of((byte) 0x1D, (byte) 0xEA),
            SpecialHair.LIGHT_BROWN, ImmutableList.of((byte) 0x11, (byte) 0xEA),
            SpecialHair.DARK_BROWN, ImmutableList.of((byte) 0x15, (byte) 0xEA),
            SpecialHair.BLACK, ImmutableList.of((byte) 0x19, (byte) 0xEA)
    );

    public SpecialHairRomHandler(File rom, long offset) {
        this.rom = rom;
        this.offset = offset;
    }

    public SpecialHairRomHandler(File rom) {
        this(rom, OFFSET_ORIGINAL);
    }


    public SpecialHair readFromRomAt(Team team) throws IOException {
        byte[] data = Files.asByteSource(rom).slice(offset(team), TEAM_LENGTH).read();
        return parse(data);
    }

    private SpecialHair parse(byte[] data) {
        SpecialHair specialHair = map.inverse().get(listOf(data));
        if(specialHair == null) {
            System.out.println("Warning: hair not found: " + ParsingUtils.bytesString(data));;
            specialHair = SpecialHair.REGULAR;
        }
        return specialHair;
    }

    @Override
    public void writeToRomAt(Team team, SpecialHair input) throws IOException {
        List<Byte> byteList = map.get(input);
        FileUtils.writeToPosition(rom, arrayOf(byteList), offset(team));
    }


    private long offset(Team team) {
        return offset + team.ordinal() * TEAM_LENGTH;
    }


    public static void main(String[] args) throws IOException {
        SpecialHairRomHandler handler = new SpecialHairRomHandler(new File("..\\iss.sfc"));
        System.out.println(handler.readFromRom().get(Team.BRAZIL));
    }
}

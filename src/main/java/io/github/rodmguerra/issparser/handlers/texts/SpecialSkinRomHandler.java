package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.SpecialHair;
import io.github.rodmguerra.issparser.model.SpecialSkin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.arrayOf;
import static io.github.rodmguerra.issparser.commons.ParsingUtils.listOf;

public class SpecialSkinRomHandler implements RomHandler<SpecialSkin> {
    private final File rom;
    private final long offset;

    public static final long OFFSET_ORIGINAL = 0x8D3C;
    private static final int TEAM_LENGTH = 2;
    private static final ImmutableBiMap<SpecialSkin, List<Byte>> map = ImmutableBiMap.<SpecialSkin, List<Byte>>of(
            SpecialSkin.REGULAR, ImmutableList.of((byte) 0x00, (byte) 0x00),
            SpecialSkin.WHITE, ImmutableList.of((byte) 0xE1, (byte) 0xE9),
            SpecialSkin.BLACK, ImmutableList.of((byte) 0x05, (byte) 0xEA),
            SpecialSkin.BROWN, ImmutableList.of((byte) 0xF9, (byte) 0xE9)
    );

    public SpecialSkinRomHandler(File rom, long offset) {
        this.rom = rom;
        this.offset = offset;
    }

    public SpecialSkinRomHandler(File rom) {
        this(rom, OFFSET_ORIGINAL);
    }


    public SpecialSkin readFromRomAt(Team team) throws IOException {
        byte[] data = Files.asByteSource(rom).slice(offset(team), TEAM_LENGTH).read();
        return parse(data);
    }

    private SpecialSkin parse(byte[] data) {
        SpecialSkin SpecialSkin = map.inverse().get(listOf(data));
        if(SpecialSkin == null) {
            System.out.println("Warning: skin not found " + ParsingUtils.bytesString(data));
            SpecialSkin = SpecialSkin.REGULAR;
        }
        return SpecialSkin;
    }

    @Override
    public void writeToRomAt(Team team, SpecialSkin input) throws IOException {
        FileUtils.writeToPosition(rom, arrayOf(map.get(input)), offset(team));
    }

    private long offset(Team team) {
        return offset + team.ordinal() * TEAM_LENGTH;
    }



    public static void main(String[] args) throws IOException {
        SpecialSkinRomHandler handler = new SpecialSkinRomHandler(new File("..\\iss.sfc"));
        System.out.println(handler.readFromRom().get(Team.BRAZIL));
    }
}

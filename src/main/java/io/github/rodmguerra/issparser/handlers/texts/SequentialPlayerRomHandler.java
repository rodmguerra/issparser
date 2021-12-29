/*package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.PlayerRomHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.unsigned;

public class SequentialPlayerRomHandler<T> implements PlayerRomHandler<T> {
    public static final int NUMBER_OF_PLAYERS = 15;
    private final File rom;
    private final long offset;

    private final int teamLength;
    private final int playerLength;
    private final int parsingLength;
    private final Function<byte[],T> parser;
    private final Function<T, byte[]> serializer;

    public SequentialPlayerRomHandler(File rom, long offset, int playerLength, int parsingLength,
                                      Function<byte[],T> parser, Function<T, byte[]> serializer) {
        this.rom = rom;
        this.offset = offset;
        this.parser = parser;
        this.playerLength = playerLength;
        this.teamLength = playerLength * NUMBER_OF_PLAYERS;
        this.parsingLength = parsingLength;
        this.serializer = serializer;
    }

    public Map<Team, Iterable<T>> readFromRom() throws IOException {
        Map<Team,Iterable<T>> map = new HashMap<>();
        ByteSource source = Files.asByteSource(rom).slice(offset, teamLength * Team.values().length);
        byte[] data = source.read();
        for (int i = 0; i < Team.values().length; i++) {
           // System.out.println(i);
            List<T> players = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_PLAYERS; j++) {
                byte[] output = ParsingUtils.bytes(data, i * teamLength + j * playerLength, parsingLength);
                players.add(parser.apply(output));
            }
            map.put(Team.at(i), players);
        }
        return map;
    }

    public Iterable<T> readFromRomAt(Team team) throws IOException {
        List<T> players = new ArrayList<>();
        ByteSource source = Files.asByteSource(rom).slice(offset(team), teamLength);
        byte[] data = source.read();
        for (int i = 0; i < 15; i++) {
            players.add(parser.apply(ParsingUtils.bytes(data, i * playerLength, parsingLength)));
        }
        return players;
    }

    private long offset(Team team) {
        return offset + team.ordinal() * teamLength;
    }

    public T readFromRomAt(Team team, int playerIndex) throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset(team, playerIndex), parsingLength);
        return parser.apply(source.read());
    }

    @Override
    public void writeToRomAt(Team team, int playerIndex, T value) throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset(team, playerIndex), 1);
        byte current = source.read()[0];
        int base = (unsigned(current) / 0x10) * 0x10;
        byte output = (byte) (base + value - 1);
        FileUtils.writeToPosition(rom, new byte[] {output}, offset(team, playerIndex));
    }

    private long offset(Team team, int playerIndex) {
        return offset(team) + playerIndex * playerLength;
    }


    public static void main(String[] args) throws IOException {
        SequentialPlayerRomHandler handler = new SequentialPlayerRomHandler(new File("..\\iss.sfc"));
        System.out.println(handler.readFromRom().get(Team.BRAZIL));
    }
}
        */
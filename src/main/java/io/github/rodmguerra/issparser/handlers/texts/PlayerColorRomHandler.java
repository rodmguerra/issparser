package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.PlayerRomHandler;
import io.github.rodmguerra.issparser.model.PlayerColor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.unsigned;

public class PlayerColorRomHandler implements PlayerRomHandler<PlayerColor> {

    public static final int NUMBER_OF_PLAYERS = 15;
    private final File rom;
    private final long offset;

    public static final long OFFSET_ORIGINAL = 0x387F1;
    private static final int TEAM_LENGTH = 90;
    private static final int PLAYER_LENGTH = 6;

    public PlayerColorRomHandler(File rom, long offset) {
        this.rom = rom;
        this.offset = offset;
    }

    public PlayerColorRomHandler(File rom) {
        this(rom, OFFSET_ORIGINAL);
    }

    public Map<Team, Iterable<PlayerColor>> readFromRom() throws IOException {
        Map<Team, Iterable<PlayerColor>> map = new HashMap<>();
        ByteSource source = Files.asByteSource(rom).slice(offset, TEAM_LENGTH * Team.values().length);
        byte[] data = source.read();
        for (int i = 0; i < Team.values().length; i++) {
            // System.out.println(i);
            List<PlayerColor> players = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_PLAYERS; j++) {
                byte b = ParsingUtils.bytes(data, i * TEAM_LENGTH + j * PLAYER_LENGTH, 1)[0];
                players.add(parsePlayerColor(b));
            }
            map.put(Team.at(i), players);
        }
        return map;
    }

    public Iterable<PlayerColor> readFromRomAt(Team team) throws IOException {
        List<PlayerColor> players = new ArrayList<>();
        ByteSource source = Files.asByteSource(rom).slice(offset(team), TEAM_LENGTH);
        byte[] data = source.read();
        for (int i = 0; i < 15; i++) {
            players.add(parsePlayerColor(ParsingUtils.bytes(data, i * PLAYER_LENGTH, 1)[0]));
        }
        return players;
    }

    private long offset(Team team) {
        return offset + team.ordinal() * TEAM_LENGTH;
    }

    public PlayerColor readFromRomAt(Team team, int playerIndex) throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset(team, playerIndex), 1);
        return parsePlayerColor(source.read()[0]);
    }

    @Override
    public void writeToRomAt(Team team, int playerIndex, PlayerColor value) throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset(team, playerIndex), 1);
        byte current = source.read()[0];
        int output = (unsigned(current) % 0x40);
        if (value == PlayerColor.SPECIAL) output += 0x40;
        FileUtils.writeToPosition(rom, new byte[]{(byte)output}, offset(team, playerIndex));
    }

    private long offset(Team team, int playerIndex) {
        return offset(team) + playerIndex * PLAYER_LENGTH;
    }

    private static PlayerColor parsePlayerColor(byte b) {
        return b / 0x40 > 0 ? PlayerColor.SPECIAL : PlayerColor.NORMAL;
    }

    public static void main(String[] args) throws IOException {
        PlayerColorRomHandler handler = new PlayerColorRomHandler(new File("..\\iss.sfc"));
        System.out.println(handler.readFromRom().get(Team.BRAZIL));
    }
}

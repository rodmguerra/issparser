package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerNameRomHandler implements RomHandler<Iterable<String>> {
    private final File rom;
    private final long offset;

    public static final long OFFSET_ORIGINAL = 0x3b62c;
    private static final int TEAM_COUNT = 27;
    private static final int PLAYERS_BY_TEAM_COUNT = 15;
    private static final int NAME_LENGTH = 8;
    private static final int TEAM_LENGTH = PLAYERS_BY_TEAM_COUNT * NAME_LENGTH;
    private static final long LENGTH = TEAM_COUNT * TEAM_LENGTH;

    private final ImmutableList<Team> teamPositions;

    public PlayerNameRomHandler(File rom, long offset) {
        this.rom = rom;
        this.offset = offset;
        teamPositions = ImmutableList.<Team>builder().add(
                Team.GERMANY, Team.ITALY, Team.HOLLAND, Team.SPAIN, Team.ENGLAND,
                Team.WALES, Team.FRANCE, Team.DENMARK, Team.SWEDEN, Team.NORWAY,
                Team.IRELAND, Team.BELGIUM, Team.AUSTRIA, Team.SWITZ, Team.ROMANIA,
                Team.BULGARIA, Team.RUSSIA, Team.ARGENTINA, Team.BRAZIL, Team.COLOMBIA,
                Team.MEXICO, Team.USA, Team.NIGERIA, Team.CAMEROON, Team.SCOTLAND,
                Team.SKOREA, Team.SUPERSTAR)
                .build();
    }

    public PlayerNameRomHandler(File rom) {
        this(rom, OFFSET_ORIGINAL);
    }

    public Map<Team, Iterable<String>> readFromRom() throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset, LENGTH);
        byte[] bytes = source.read();

        System.out.println(ParsingUtils.bytesString(bytes));
        String[] playerNames = ParsingUtils.issText(bytes).split("(?<=\\G........)");
        Map<Team, Iterable<String>> teams = new HashMap<>(TEAM_COUNT);
        List<String> teamPlayers = null;
        for (int i = 0; i < playerNames.length; i++) {
            if (i % PLAYERS_BY_TEAM_COUNT == 0) {
                teamPlayers = new ArrayList<>(PLAYERS_BY_TEAM_COUNT);
                teams.put(teamPositions.get(i), teamPlayers);
            }
            teamPlayers.add(playerNames[i].trim());
        }

        return teams;
    }

    public Iterable<String> readFromRomAt(Team team) throws IOException {
        ByteSource source = Files.asByteSource(rom).slice(offset + positionOf(team) * TEAM_LENGTH, TEAM_LENGTH);
        byte[] bytes = source.read();
        System.out.println(ParsingUtils.bytesString(bytes));
        String[] playerNames = ParsingUtils.issText(bytes).split("(?<=\\G........)");
        List<String> playerNameList = new ArrayList<>();
        for (String playerName : playerNames) {
            playerNameList.add(playerName.trim());
        }
        return playerNameList;
    }


    public void writeToRom(Map<Team, ? extends Iterable<String>> teams) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Team team : teamPositions) {
            Iterable<String> playerNames = teams.get(team);
            for (String teamPlayer : playerNames) {
                sb.append(treatName(teamPlayer));
            }
        }
        byte[] bytes = ParsingUtils.issBytes(sb);
        System.out.println(ParsingUtils.bytesString(bytes));
        FileUtils.writeToPosition(rom, bytes, offset);
    }

    private CharSequence treatName(String teamPlayer) {
        return ParsingUtils.cutAndCenter(ParsingUtils.stripAccents(teamPlayer), NAME_LENGTH);
    }

    public void writeToRomAt(Team team, Iterable<String> teamPlayers) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String teamPlayer : teamPlayers) {
            sb.append(treatName(teamPlayer));
        }
        byte[] bytes = ParsingUtils.issBytes(sb);
        System.out.println(ParsingUtils.bytesString(bytes));
        FileUtils.writeToPosition(rom, bytes, offset + positionOf(team) * TEAM_LENGTH);
    }

    public int positionOf(Team team) {
        return teamPositions.indexOf(team);
    }
}

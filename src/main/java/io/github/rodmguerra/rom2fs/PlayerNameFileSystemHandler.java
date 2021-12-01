package io.github.rodmguerra.rom2fs;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerNameFileSystemHandler implements FileSystemHandler<Iterable<String>> {

    private final String folder;
    private final String prefix;
    private final String suffix;
    private static final int PLAYERS_BY_TEAM_COUNT = 15;

    private static final int TEAMS = 27;

    public PlayerNameFileSystemHandler(String folder, String prefix, String suffix) {
        this.folder = folder;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public Map<Integer, Iterable<String>> read() throws IOException {
        File dir = new File(folder);
        Map<Integer, Iterable<String>> playerNames = new TreeMap<>();
        for (String fileName : new TreeSet<>(Arrays.asList(dir.list()))) {
            if(fileName.startsWith(prefix) && fileName.endsWith(suffix)) {
                String indexPart = fileName.substring(prefix.length(),fileName.length()-suffix.length());
                int index = 0;
                try {
                    index = Integer.parseInt(indexPart);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Ignored file: " + fileName);
                    continue;
                }

                if(index < 1 || index > TEAMS) {
                    System.out.println("Warning: Ignored file: " + fileName);
                    continue;
                }

                File file = new File(folder + "/" + fileName);
                playerNames.put(index - 1, Files.readLines(file, Charsets.UTF_8));
            } else {
                System.out.println("Warning: Ignored file: " + fileName);
                continue;
            }
        }
        return playerNames;
    }

    public void write(Iterable<Iterable<String>> playersByTeam) throws IOException {
        int i=0;
        for (Iterable<String> teamPlayers : playersByTeam) {
            File file = new File(folder + "/" + prefix + ++i + suffix);
            Files.createParentDirs(file);
            List<String> trimmedTeamPlayers = new ArrayList<>(PLAYERS_BY_TEAM_COUNT);
            for (String teamPlayer : teamPlayers) {
                trimmedTeamPlayers.add(teamPlayer.trim());
            }
            Files.write(String.join("\n", trimmedTeamPlayers), file, Charsets.UTF_8);
        }
    }

}

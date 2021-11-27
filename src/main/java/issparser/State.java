package issparser;


import java.io.File;

public class State {
    public State() {
        this(null);
    }

    public State(File rom) {
        this(rom, 0);
    }

    public State(File rom, int teamIndex) {
        this.teamIndex = teamIndex;
        this.rom = rom;
    }

    private final int teamIndex;
    private final File rom;

    public State nextTeam() {
        return new State(rom, (teamIndex + 1) % 27 );
    }

    public int getTeamIndex() {
        return teamIndex;
    }

    public File getRom() {
        return rom;
    }

    public State previousTeam() {
        return new State(rom, (teamIndex + 27 - 1) % 27 );

    }

    public State withRom(File rom) {
        return new State(rom, teamIndex);
    }


    public State withTeam(int teamIndex) {
        return new State(rom, teamIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (teamIndex != state.teamIndex) return false;
        if (rom != null ? !rom.equals(state.rom) : state.rom != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teamIndex;
        result = 31 * result + (rom != null ? rom.hashCode() : 0);
        return result;
    }
}


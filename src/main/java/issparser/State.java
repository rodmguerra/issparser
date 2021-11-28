package issparser;


import issparser.commons.RomHandler;

import java.io.File;

public class State {
    public State() {
        this(null);
    }

    public State(File rom) {
        this(rom, RomHandler.Team.GERMANY);
    }

    public State(File rom, RomHandler.Team team) {
        this.team = team;
        this.rom = rom;
    }

    private final RomHandler.Team team;
    private final File rom;

    public State nextTeam() {
        return new State(rom, team.next() );
    }

    public RomHandler.Team getTeam() {
        return team;
    }

    public File getRom() {
        return rom;
    }

    public State previousTeam() {
        return new State(rom, team.previous() );

    }

    public State withRom(File rom) {
        return new State(rom, team);
    }


    public State withTeam(RomHandler.Team team) {
        return new State(rom, team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (team != state.team) return false;
        if (rom != null ? !rom.equals(state.rom) : state.rom != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = team.hashCode();
        result = 31 * result + rom.hashCode();
        return result;
    }
}


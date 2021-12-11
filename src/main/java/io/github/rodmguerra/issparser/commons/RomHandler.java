package io.github.rodmguerra.issparser.commons;

import java.io.IOException;
import java.util.Map;

public interface RomHandler<T> {

    Map<Team, T> readFromRom() throws IOException;

    void writeToRom(Map<Team, ? extends T> input) throws IOException;

    T readFromRomAt(Team team) throws IOException;

    void writeToRomAt(Team team, T input) throws IOException;

    enum Team {
        GERMANY("Germany"), ITALY("Italy"), HOLLAND("Holland"), SPAIN("Spain"), ENGLAND("England"),
        SCOTLAND("Scotland"), WALES("Wales"), FRANCE("France"), DENMARK("Denmark"), SWEDEN("Sweden"),
        NORWAY("Norway"), IRELAND("Ireland"), BELGIUM("Belgium"), AUSTRIA("Austria"), SWITZ("Switz"),
        ROMANIA("Romania"), BULGARIA("Bulgaria"), RUSSIA("Russia"), ARGENTINA("Argentina"), BRAZIL("Brazil"),
        COLOMBIA("Colombia"), MEXICO("Mexico"), USA("U.S.A."), NIGERIA("Nigeria"), CAMEROON("Cameroon"),
        SKOREA("S.Korea"), SUPERSTAR("Super Star");

        private final String name;

        private Team(String name) {
            this.name = name;
        }

        public static Team at(int ordinal) {
            return Team.values()[ordinal];
        }

        public Team next() {
            Team[] teams = Team.values();
            return teams[(ordinal()+1) % teams.length];
        }

        public Team previous() {
            Team[] teams = Team.values();
            return teams[(ordinal() + teams.length-1) % teams.length];
        }

        @Override
        public String toString() {
            return String.format("%02d", ordinal() + 1) + ". " + name;
        }
    }

}

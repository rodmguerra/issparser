package io.github.rodmguerra.issparser.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface PlayerRomHandler<T> extends RomHandler<Iterable<T>> {
    T readFromRomAt(Team team, int playerIndex) throws IOException;

    default Iterable<T> readFromRomAt(Team team) throws IOException {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(readFromRomAt(team, i));
        }
        return list;
    }

    void writeToRomAt(Team team, int playerIndex, T value) throws IOException;

    default void writeToRomAt(Team team, Iterable<T> list) throws IOException {
        int i=0;
        for (T value : list) {
            writeToRomAt(team, i++, value);
        }
    }
}

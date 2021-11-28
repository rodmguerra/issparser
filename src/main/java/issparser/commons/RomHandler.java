package issparser.commons;

import java.io.IOException;

public interface RomHandler<T> {

    Iterable<T> readFromRom() throws IOException;
    void writeToRom(Iterable<T> input) throws IOException;
    T readFromRomAt(int index) throws IOException;
    void writeToRomAt(int index, T input) throws IOException;

}

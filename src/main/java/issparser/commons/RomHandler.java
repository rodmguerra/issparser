package issparser.commons;

import java.io.IOException;

public interface RomHandler<T> {

    Iterable<T> readFromRom() throws IOException;
    void writeToRom(Iterable<T> input) throws IOException;
}

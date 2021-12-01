package io.github.rodmguerra.rom2fs;

import java.io.IOException;
import java.util.Map;

public interface FileSystemHandler<T> {

    Map<Integer, T> read() throws IOException;
    void write(Iterable<T> object) throws IOException;
}

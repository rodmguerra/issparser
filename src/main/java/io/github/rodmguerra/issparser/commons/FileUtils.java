package io.github.rodmguerra.issparser.commons;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.bytesString;

public class FileUtils {

    public static void writeToPosition(String filename, byte[] data, long position)
            throws IOException {
        RandomAccessFile writer = new RandomAccessFile(filename, "rw");
        writer.seek(position);
        writer.write(data);
        writer.close();
    }

    public static void writeToPosition(File file, byte[] data, long position)
            throws IOException {
        System.out.println("Writing to " + Long.toHexString(position) + "= " + bytesString(data));
        RandomAccessFile writer = new RandomAccessFile(file.getAbsolutePath(), "rw");
        writer.seek(position);
        writer.write(data);
        writer.close();
    }
}

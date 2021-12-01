package io.github.rodmguerra.rom2fs;

import io.github.rodmguerra.issparser.commons.RomHandler;

import java.io.IOException;

public class Module {
    private final RomHandler<Object> romHandler;
    private final FileSystemHandler<Object> fsHandler;

    public Module(RomHandler romHandler, FileSystemHandler fsHandler) {
        this.romHandler = romHandler;
        this.fsHandler = fsHandler;
    }


    public void writeOperation() throws IOException {
        /** TODO: fix ***/
        //romHandler.writeToRom(fsHandler.read());
    }

    public void readOperation() throws IOException {
        /** TODO: Fix fs handler **/
        //fsHandler.write(romHandler.readFromRom());
    }
}

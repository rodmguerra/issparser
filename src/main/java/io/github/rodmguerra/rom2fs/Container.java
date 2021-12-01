package io.github.rodmguerra.rom2fs;

import io.github.rodmguerra.issparser.handlers.colors.UniformColorRomHandler;
import io.github.rodmguerra.issparser.handlers.texts.PlayerNameRomHandler;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 21/11/21
 * Time: 00:02
 * To change this template use File | Settings | File Templates.
 */
public class Container {

    private static final String PLAYER_NAMES_DIR = "playerNames";
    private static final String PLAYER_NAMES_FILE_PREFFIX = "team";
    private static final String DEFAULT_EXTENSION = ".txt";
    private final String rom;
    private Module playerNameModule;
    private Module kitsModule;

    public Container(String rom) {
        this.rom = rom;
        PlayerNameRomHandler playerNameRomHandler = new PlayerNameRomHandler(new File(rom), PlayerNameRomHandler.OFFSET_ORIGINAL);
        PlayerNameFileSystemHandler playerNameFileSystemHandler = new PlayerNameFileSystemHandler(PLAYER_NAMES_DIR, PLAYER_NAMES_FILE_PREFFIX, DEFAULT_EXTENSION);
        this.playerNameModule = new Module(playerNameRomHandler, playerNameFileSystemHandler);
        this.kitsModule = new Module(new UniformColorRomHandler(new File(rom)), new UniformColorFileSystemHandler());
    }


    public List<Module> modules() {
        return Arrays.asList(playerNameModule /*, kitsModule*/);
    }
}

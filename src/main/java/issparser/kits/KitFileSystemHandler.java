package issparser.kits;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import issparser.commons.FileSystemHandler;
import issparser.kits.model.TeamKits;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 21/11/21
 * Time: 00:14
 * To change this template use File | Settings | File Templates.
 */
public class KitFileSystemHandler implements FileSystemHandler<TeamKits> {
    @Override
    public Map<Integer, TeamKits> read() throws IOException {
        return new HashMap<Integer, TeamKits>();
    }

    @Override
    public void write(Iterable<TeamKits> teamKits) throws IOException {
        new File("teamKits").mkdir();

        ObjectMapper mapper = new ObjectMapper();
        int i=0;
        for (TeamKits teamKit : teamKits) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("teamKits/team" + (++i) + ".json"), teamKit);
        }
    }
}

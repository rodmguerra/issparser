package issparser.kits;

import com.fasterxml.jackson.databind.ObjectMapper;
import issparser.commons.FileSystemHandler;
import issparser.kits.model.TeamKits;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

package io.github.rodmguerra.rom2fs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rodmguerra.issparser.model.colors.uniforms.TeamUniforms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UniformColorFileSystemHandler implements FileSystemHandler<TeamUniforms> {
    @Override
    public Map<Integer, TeamUniforms> read() throws IOException {
        return new HashMap<Integer, TeamUniforms>();
    }

    @Override
    public void write(Iterable<TeamUniforms> teamKits) throws IOException {
        new File("teamKits").mkdir();

        ObjectMapper mapper = new ObjectMapper();
        int i=0;
        for (TeamUniforms teamKit : teamKits) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("teamKits/team" + (++i) + ".json"), teamKit);
        }
    }
}

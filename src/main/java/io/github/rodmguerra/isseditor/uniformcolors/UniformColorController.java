package io.github.rodmguerra.isseditor.uniformcolors;

import io.github.rodmguerra.isseditor.team.AbstractTeamController;
import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.team.TeamView;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.handlers.colors.UniformColorRomHandler;
import io.github.rodmguerra.issparser.model.colors.uniforms.TeamUniforms;

import java.io.File;

public class UniformColorController extends AbstractTeamController<TeamUniforms> {
    public UniformColorController(Router router, TeamView view) {
        super(router, view);
    }

    @Override
    protected RomHandler<TeamUniforms> romHandlerFor(File rom) {
        return new UniformColorRomHandler(rom);
    }
}

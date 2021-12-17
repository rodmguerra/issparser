package io.github.rodmguerra.isseditor;

import io.github.rodmguerra.isseditor.flagcolor.FlagColorController;
import io.github.rodmguerra.isseditor.flagcolor.FlagColorPage;
import io.github.rodmguerra.isseditor.flagdesign.FlagDesignController;
import io.github.rodmguerra.isseditor.flagdesign.FlagDesignPage;
import io.github.rodmguerra.isseditor.hairandskin.HairAndSkinColorController;
import io.github.rodmguerra.isseditor.hairandskin.HairAndSkinColorPage;
import io.github.rodmguerra.isseditor.home.HomeController;
import io.github.rodmguerra.isseditor.playernames.PlayerNameController;
import io.github.rodmguerra.isseditor.playernames.PlayerNamePage;
import io.github.rodmguerra.isseditor.teamname.TeamNameController;
import io.github.rodmguerra.isseditor.teamname.TeamNamePage;
import io.github.rodmguerra.isseditor.uniformcolors.UniformColorController;
import io.github.rodmguerra.isseditor.uniformcolors.UniformColorPage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Router {

    public enum Route {
        HOME(-1),
        PLAYER_NAMES(0),
        UNIFORM_COLORS(1),
        HAIR_AND_SKIN_COLORS(2),
        FLAG_COLORS(3),
        FLAG_DESIGN(4),
        TEAM_NAME_IN_GAME(5);
        private final int resource;

        private Route(int resource) {
            this.resource = resource;
        }

        public static Route forResource(int id) {
            for (Route route : Route.values()) {
                if(route.resource == id) return route;
            }
            return HOME;
        }
    }
    private static final int RESOURCE_COUNT = Route.values().length - 1;
    private Map<Route, Controller> controllers  = new HashMap<>();

    private Route route;
    private State state;

    public Router(JFrame frame) {
        this.frame = frame;
        controllers.put(Route.HOME, new HomeController(this));
        controllers.put(Route.PLAYER_NAMES, new PlayerNameController(this, new PlayerNamePage()));
        controllers.put(Route.UNIFORM_COLORS, new UniformColorController(this, new UniformColorPage()));
        controllers.put(Route.HAIR_AND_SKIN_COLORS, new HairAndSkinColorController(this, new HairAndSkinColorPage()));
        controllers.put(Route.FLAG_COLORS, new FlagColorController(this, new FlagColorPage()));
        controllers.put(Route.FLAG_DESIGN, new FlagDesignController(this, new FlagDesignPage()));
        controllers.put(Route.TEAM_NAME_IN_GAME, new TeamNameController(this, new TeamNamePage()));

        this.state = new State();
    }

    private JFrame frame;

    public void navigate(Route route, State state) {
        State oldState = this.state;
        Route oldRoute = this.route;
        this.route = route;
        this.state = state;
        frame.getContentPane().removeAll();
        Controller controller = controllers.get(route);
        if(!state.equals(oldState) || !route.equals(oldRoute)) {
            controller.setState(state);
        }
        frame.getContentPane().add(controller.getPanel());
        double width = frame.getPreferredSize().getWidth();
        double height = frame.getPreferredSize().getHeight();
        frame.repaint();

        if(width < 500 && height < 250) {
            frame.setSize(500, 250);
        } else {frame.pack(); }

    }

    public void navigate(State state) {
        navigate(route, state);
    }

    public void navigate(Route route) {
        navigate(route, state);
    }

    public void nextResource() {
        int next = (route.resource + 1) % RESOURCE_COUNT;
        navigate(Route.forResource(next), state);
    }

    public void previousResource() {
        int next = (route.resource + RESOURCE_COUNT - 1) % RESOURCE_COUNT;
        navigate(Route.forResource(next), state);
    }

    public State getState() {
        return state;
    }
}

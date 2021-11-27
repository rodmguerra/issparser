package issparser;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Router {
    public enum Route {
        HOME,
        ROM_HOME,
        PLAYER_NAMES
    }
    private Map<Route, Controller> controllers  = new HashMap<>();

    private Route route;
    private State state;

    public Router(JFrame frame) {
        this.frame = frame;
        controllers.put(Route.HOME, new HomeController(this));
        controllers.put(Route.ROM_HOME, new HomeController(this));
        controllers.put(Route.PLAYER_NAMES, new TeamController(this, new PlayerNameView()));
        this.state = new State();
    }

    private JFrame frame;

    public void navigate(Route route, State state) {
        this.route = route;
        frame.getContentPane().removeAll();
        Controller controller = controllers.get(route);
        controller.setState(state);
        frame.getContentPane().add(controller.getPanel());
        double width = frame.getPreferredSize().getWidth();
        double height = frame.getPreferredSize().getHeight();
        if(width < 500 && height < 250) {
            frame.setSize(500, 250);
        } else frame.pack();
    }

    public void navigate(State state) {
        navigate(route, state);
    }

    public void navigate(Route route) {
        navigate(route, state);
    }

    public void nextRoute(State state) {
        Router.Route[] routes = Router.Route.values();
        int nextOrdinal = (route.ordinal() + 1) % routes.length;
        navigate(routes[nextOrdinal], state);
    }

    public void previousRoute(State state) {
        Router.Route[] routes = Router.Route.values();
        int nextOrdinal = (route.ordinal() + routes.length -1) % routes.length;
        navigate(routes[nextOrdinal], state);
    }

    public State getState() {
        return state;
    }
}

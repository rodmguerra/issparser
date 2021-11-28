package issparser;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Router {

    public enum Route {
        HOME(-1),
        PLAYER_NAMES(0),
        PLAYER_UNIFORMS(1);
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
    private static final int RESOURCE_COUNT = 2;
    private Map<Route, Controller> controllers  = new HashMap<>();

    private Route route;
    private State state;

    public Router(JFrame frame) {
        this.frame = frame;
        controllers.put(Route.HOME, new HomeController(this));
        controllers.put(Route.PLAYER_NAMES, new PlayerNameController(this, new PlayerNameView()));
        controllers.put(Route.PLAYER_UNIFORMS, new UniformController(this, new UniformView()));
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
        if(!state.equals(oldState) || !route.equals(oldRoute)) controller.setState(state);
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

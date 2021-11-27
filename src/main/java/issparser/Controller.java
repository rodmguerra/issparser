package issparser;

import javax.swing.*;

public interface Controller {
    JPanel getPanel();
    State getState();
    void setState(State state);
}

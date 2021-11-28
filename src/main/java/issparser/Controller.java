package issparser;

import javax.swing.*;

public interface Controller<T> {
    JPanel getPanel();
    void setState(State state);
}

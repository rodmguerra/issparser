package io.github.rodmguerra.isseditor;

import javax.swing.*;

public interface Controller<T> {
    JPanel getPanel();
    void setState(State state);
}

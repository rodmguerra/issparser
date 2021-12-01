package io.github.rodmguerra.isseditor;

import javax.swing.*;
import java.util.function.Consumer;

public class AbstractRomView<T>  {
    protected Consumer<T> saveListener;
    protected Runnable nextResourceListener;
    protected Runnable previousResourceListener;
    protected Runnable readListener;
    protected Consumer<Integer> resourceListener;

    public void setSaveListener(Consumer<T> saveListener) {
        this.saveListener = saveListener;
    }

    public void setReadListener(Runnable readListener) {
        this.readListener = readListener;
    }

    public void setNextResourceListener(Runnable nextResourceListener) {
        this.nextResourceListener = nextResourceListener;
    }

    public void setPreviousResourceListener(Runnable previousResourceListener) {
        this.previousResourceListener = previousResourceListener;
    }

    public void setResourceListener(Consumer<Integer> resourceListener) {
        this.resourceListener = resourceListener;
    }

    public AbstractRomView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

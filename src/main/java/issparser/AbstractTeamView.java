package issparser;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractTeamView<T> implements TeamView<T> {

    protected Consumer<List<String>> saveListener;
    protected Runnable nextTeamListener;
    protected Runnable previousTeamListener;
    protected Runnable readListener;
    protected Consumer<Integer> teamListener;

    public void setSaveListener(Consumer<List<String>> saveListener) {
        this.saveListener = saveListener;
    }

    public void setReadListener(Runnable readListener) {
        this.readListener = readListener;
    }

    public void setNextTeamListener(Runnable nextTeamListener) {
        this.nextTeamListener = nextTeamListener;
    }

    public void setPreviousTeamListener(Runnable previousTeamListener) {
        this.previousTeamListener = previousTeamListener;
    }

    public void setTeamListener(Consumer<Integer> teamListener) {
        this.teamListener = teamListener;
    }

    public AbstractTeamView() {
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

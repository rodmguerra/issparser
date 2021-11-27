package issparser;


import javax.swing.*;

public abstract class AbstractController implements Controller {

    protected State state;
    protected JPanel panel;
    protected Router router;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public JPanel getPanel() {
        return panel;
    }

    public AbstractController(Router router) {
        this.router = router;
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

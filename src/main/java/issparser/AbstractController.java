package issparser;


import javax.swing.*;

public abstract class AbstractController<T> implements Controller<T> {

    protected JPanel panel;
    protected Router router;


    public abstract void setState(State state);

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

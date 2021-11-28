package issparser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractTeamView<T> extends AbstractRomView<T> implements TeamView<T> {

    protected Consumer<T> saveListener;
    protected Runnable nextTeamListener;
    protected Runnable previousTeamListener;
    protected Runnable readListener;
    protected Consumer<Integer> teamListener;

    private JPanel panel;
    private JComboBox<String> teamCombo;
    private JComboBox<String> resourceCombo;

    public void setSaveListener(Consumer<T> saveListener) {
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

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        BoxLayout grid = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(grid);


         /* Change action */
        JPanel resourceChangePanel = new JPanel(new GridLayout(1,2));
        loadResourcePanel(resourceChangePanel);
        panel.add(resourceChangePanel);


        /* Change team */
        JPanel teamChangePanel = new JPanel(new GridLayout(1,2));
        JButton previousTeamButton = new JButton("<<");
        previousTeamButton.addActionListener(e -> previousTeamListener.run());
        teamChangePanel.add(previousTeamButton);
        teamCombo = new JComboBox<>(TEAMS);
        teamCombo.addItemListener(e -> {
            teamListener.accept(teamCombo.getSelectedIndex());
        } );
        teamChangePanel.add(teamCombo);
        JButton nextTeamButton = new JButton(">>");
        nextTeamButton.addActionListener(e -> nextTeamListener.run());
        teamChangePanel.add(nextTeamButton);
        panel.add(teamChangePanel);

        /* Reload */
        JButton reloadButton = new JButton("Reload from ROM");
        reloadButton.addActionListener(e -> readListener.run());
        JPanel reloadPanel = new JPanel(new GridLayout(1,1));
        reloadPanel.add(reloadButton);
        panel.add(reloadPanel);

        panel.add(innerPannel());

          /* Save */
        JButton saveButton = new JButton("Save to ROM");
        saveButton.addActionListener(e -> saveListener.accept(getData()));
        JPanel savePanel = new JPanel(new GridLayout(1,1));
        savePanel.add(saveButton);
        panel.add(savePanel);
        this.panel = panel;
    }

    private void loadResourcePanel(JPanel resourceChangePanel) {
        JButton previousActionButton = new JButton("<<");
        previousActionButton.addActionListener(e -> previousResourceListener.run());
        resourceChangePanel.add(previousActionButton);
        resourceCombo = new JComboBox<>(new String[]{"Player Names", "Uniforms"});
        setSelectedIndexSafe(resourceCombo, resourceIndex());
        resourceCombo.repaint();
        resourceCombo.addItemListener(e -> {
            int nextIndex = resourceCombo.getSelectedIndex();
            resourceListener.accept(nextIndex);
            //Returns combo to initial state
            setSelectedIndexSafe(resourceCombo, resourceIndex());
        });
        resourceChangePanel.add(resourceCombo);
        JButton nextResourceButton = new JButton(">>");
        nextResourceButton.addActionListener(e -> nextResourceListener.run());
        resourceChangePanel.add(nextResourceButton);
    }

    private void setSelectedIndexSafe(JComboBox combo, int newIndex) {
        ArrayList<ItemListener> listeners = newArrayList(combo.getItemListeners());
        listeners.forEach(combo::removeItemListener);
        resourceCombo.setSelectedIndex(newIndex);
        listeners.forEach(combo::addItemListener);
    }

    public JPanel getPanel() {
        return panel;
    }


    public void setTeamIndex(int teamIndex) {
        if(teamCombo.getSelectedIndex() != teamIndex)  {
            this.teamCombo.setSelectedIndex(teamIndex);
            teamCombo.repaint();
        }
    }

    protected abstract int resourceIndex();

    protected abstract Component innerPannel();
}

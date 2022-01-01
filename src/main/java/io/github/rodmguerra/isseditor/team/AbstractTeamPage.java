package io.github.rodmguerra.isseditor.team;

import io.github.rodmguerra.isseditor.AbstractRomPage;
import io.github.rodmguerra.issparser.commons.RomHandler;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.rodmguerra.isseditor.Texts.string;
import static io.github.rodmguerra.isseditor.Texts.strings;

public abstract class AbstractTeamPage<T> extends AbstractRomPage<T> implements TeamView<T> {

    protected Consumer<T> saveListener;
    protected Runnable nextTeamListener;
    protected Runnable previousTeamListener;
    protected Runnable readListener;
    protected Consumer<RomHandler.Team> teamListener;

    protected JComboBox<String> teamCombo;


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

    public void setTeamListener(Consumer<RomHandler.Team> teamListener) {
        this.teamListener = teamListener;
    }

    protected RomHandler.Team getCurrentTeam() {
        return RomHandler.Team.at(teamCombo.getSelectedIndex());
    }

    public AbstractTeamPage() {
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



        /* Change team */
        JPanel teamChangePanel = new JPanel(new GridLayout(1,2));
        JButton previousTeamButton = new JButton("<<");
        previousTeamButton.addActionListener(e -> previousTeamListener.run());
        teamChangePanel.add(previousTeamButton);
        teamCombo = new JComboBox<>(teamNames());
        teamCombo.addItemListener(e -> {
            teamListener.accept(RomHandler.Team.at(teamCombo.getSelectedIndex()));
        });
        teamChangePanel.add(teamCombo);
        JButton nextTeamButton = new JButton(">>");
        nextTeamButton.addActionListener(e -> nextTeamListener.run());
        teamChangePanel.add(nextTeamButton);
        panel.add(teamChangePanel);

        /* Reload */
        JButton reloadButton = new JButton(string("rom", "reload", "action"));
        reloadButton.addActionListener(e -> readListener.run());
        JPanel reloadPanel = new JPanel(new GridLayout(1,1));
        reloadPanel.add(reloadButton);
        panel.add(reloadPanel);
        JPanel savePanel = new JPanel(new GridLayout(1,1));


        panel.add(innerPannel());
        panel.add(savePanel);

          /* Save */
        JButton saveButton = new JButton(string("rom", "save", "action"));
        saveButton.addActionListener(e -> saveListener.accept(getData()));
        savePanel.add(saveButton);
    }

    protected String[] teamNames() {
        String[] names = strings("rom", "team", "values");
        String[] output = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            output[i] = String.format("%02d", i + 1) + ". " + names[i];
        }
        return output;
    }
    /*
    private String[] teamNames() {
        return ;
        /*if(teamNames.length != RomHandler.Team.values().length) {
            teamNames = Stream.of(RomHandler.Team.values()).map(RomHandler.Team::toString).toArray(String[]::new);
        }*/
    //}


    public JPanel getPanel() {
        return panel;
    }


    public void setTeam(RomHandler.Team team) {
        int teamIndex = team.ordinal();
        if(teamCombo.getSelectedIndex() != teamIndex)  {
            this.teamCombo.setSelectedIndex(teamIndex);
            teamCombo.repaint();
        }
    }

    protected abstract Component innerPannel();
}

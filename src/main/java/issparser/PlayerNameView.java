package issparser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class PlayerNameView extends AbstractTeamView<Iterable<String>> {

    private JPanel panel;
    private java.util.List<JTextField> fields = new ArrayList<>();
    private JComboBox<String> teamCombo;


    public PlayerNameView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        BoxLayout grid = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(grid);

        /* Change team */
        JPanel teamChangePanel = new JPanel(new GridLayout(1,2));
        JButton previousTeamButton = new JButton("<<");
        previousTeamButton.addActionListener(e -> previousTeamListener.run());
        teamChangePanel.add(previousTeamButton);

        teamCombo = new JComboBox<>(TEAMS);
        teamCombo.addItemListener(e -> teamListener.accept(teamCombo.getSelectedIndex()));
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

        /* Players */
        JPanel innerPanel = new JPanel();
        FlowLayout playersLayout = new FlowLayout(FlowLayout.CENTER);
        playersLayout.setAlignOnBaseline(true);
        innerPanel.setLayout(playersLayout);
        innerPanel.add(panelForPlayerNames("Starting", 11));
        innerPanel.add(panelForPlayerNames("Substitutes", 4));
        panel.add(innerPanel);

          /* Save */
        JButton saveButton = new JButton("Save to ROM");
        saveButton.addActionListener(e -> saveListener.accept(getData()));
        JPanel savePanel = new JPanel(new GridLayout(1,1));
        savePanel.add(saveButton);
        panel.add(savePanel);
        this.panel = panel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel panelForPlayerNames(String title, int size) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        for (int i=0; i<size; i++) {
            JPanel row = new JPanel();
            row.setLayout(new FlowLayout(FlowLayout.LEADING));
            JTextField field = new JTextField(8);
            fields.add(field);
            System.out.println(fields.size());
            field.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    String text = field.getText();
                    if (text.length() >= 8 ) { // limit textfield to 3 characters
                        e.consume();
                        field.setText(text.substring(0,8));
                    }
                }
            });
            row.add(field);
            panel.add(row);
        }
        panel.setVisible(true);

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }


    public List<String> getData() {
        List<String> playerNames = new ArrayList<>();
        for (JTextField textField : fields) {
            playerNames.add(textField.getText());
        }
        return playerNames;
    }

    public void setTeamIndex(int teamIndex) {
        if(teamCombo.getSelectedIndex() != teamIndex)  {
            this.teamCombo.setSelectedIndex(teamIndex);
            teamCombo.repaint();
        }
    }

    public void setData(Iterable<String> playerNames) {
        Iterator<JTextField> fieldsIt = fields.iterator();
        for (String playerName : playerNames) {
            fieldsIt.next().setText(playerName);
        }
    }
}

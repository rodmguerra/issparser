package issparser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerNameView extends AbstractTeamView<Iterable<String>> {
    private java.util.List<JTextField> fields;

    public PlayerNameView() {
        super();
    }

    @Override
    protected int resourceIndex() {
        return 0;
    }

    protected JPanel innerPannel() {
        /* Players */
        JPanel innerPanel = new JPanel();
        FlowLayout playersLayout = new FlowLayout(FlowLayout.CENTER);
        playersLayout.setAlignOnBaseline(true);
        innerPanel.setLayout(playersLayout);
        innerPanel.add(panelForPlayerNames("Starting", 11));
        innerPanel.add(panelForPlayerNames("Substitutes", 4));
        return innerPanel;
    }


    private JPanel panelForPlayerNames(String title, int size) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        for (int i=0; i<size; i++) {
            JPanel row = new JPanel();
            row.setLayout(new FlowLayout(FlowLayout.LEADING));
            JTextField field = new JTextField(8);
            if(fields == null) fields = new ArrayList<>();
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


    public void setData(Iterable<String> playerNames) {
        Iterator<JTextField> fieldsIt = fields.iterator();
        for (String playerName : playerNames) {
            fieldsIt.next().setText(playerName);
        }
    }
}

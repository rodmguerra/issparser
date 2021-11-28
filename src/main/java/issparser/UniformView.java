package issparser;

import issparser.kits.model.Kit;
import issparser.kits.model.TeamKits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UniformView extends AbstractTeamView<TeamKits> {

    private JLabel teamKitsLabel;
    private TeamKitsView teamKitsView;

    public UniformView(){
        super();
    }

    @Override
    protected int resourceIndex() {
        return 1;
    }

    @Override
    protected Component innerPannel() {
         /* Players */
        JPanel innerPanel = new JPanel();
        FlowLayout playersLayout = new FlowLayout(FlowLayout.CENTER);
        playersLayout.setAlignOnBaseline(true);
        innerPanel.setLayout(playersLayout);
        if(teamKitsLabel == null) teamKitsLabel = new JLabel();
        if(teamKitsView == null) teamKitsView = TeamKitsView.zero();
        innerPanel.add(panelForKit("First", teamKitsView.getFirst()));
        innerPanel.add(panelForKit("Second", teamKitsView.getSecond()));
        return innerPanel;
    }


    private JPanel panelForKit(String title, KitView kit) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        addPartToPanel("Shirt", panel, kit.getShirt());
        addPartToPanel("Shorts", panel, kit.getShorts());
        addPartToPanel("Socks", panel, kit.getSocks());

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private void addPartToPanel(String partName, JPanel kitPanel, KitPartView part) {
        JPanel partPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (RGBView partRgb : part.getRgbs()) {
            JPanel rgbPanel = new JPanel();
            BoxLayout layout = new BoxLayout(rgbPanel,BoxLayout.Y_AXIS);

            JPanel colorPanel = new JPanel(new FlowLayout());
            colorPanel.add(partRgb.getDisplay());
            rgbPanel.add(colorPanel);
            rgbPanel.setLayout(layout);
            rgbPanel.add(partRgb.getRed());
            rgbPanel.add(partRgb.getGreen());
            rgbPanel.add(partRgb.getBlue());
            partPanel.add(rgbPanel);
        }

        partPanel.setBorder(BorderFactory.createTitledBorder(partName));
        partPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        partPanel.setMaximumSize( partPanel.getPreferredSize() );
        kitPanel.add(partPanel);
    }


    @Override
    public TeamKits getData() {
        return new TeamKits(null, null);
    }

    @Override
    public void setData(TeamKits teamKits) {
        //teamKitsLabel.setText(teamKits.getFirst().getShirt().getRgbs()[0].toString());
        //teamKitsLabel.repaint();
        teamKitsView.setFromModel(teamKits);
    }
}

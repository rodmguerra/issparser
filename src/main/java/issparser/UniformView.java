package issparser;

import issparser.kits.model.TeamKits;

import javax.swing.*;
import java.awt.*;

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
        innerPanel.add(panelForKit("First", teamKitsView.getFirst(), "Predominant color"));
        innerPanel.add(panelForKit("Second", teamKitsView.getSecond()));
        innerPanel.add(panelForKeeperKit("Goalkeeper", teamKitsView.getGoalkeeper()));


        return innerPanel;
    }


    private JPanel panelForKit(String title, KitView kit) {
        return panelForKit(title, kit, null);
    }
    private JPanel panelForKit(String title, KitView kit, String predominantTitle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        addPartToPanel("Shirt", panel, kit.getShirt(), predominantTitle);
        addPartToPanel("Shorts", panel, kit.getShorts(), null);
        addPartToPanel("Socks", panel, kit.getSocks(), null);


        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private JPanel panelForKeeperKit(String title, KeeperKitView kit) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);
        addPartToPanel("Shirt and Socks", panel, kit.getShirtAndSocks(), null);
        addPartToPanel("Shorts", panel, kit.getShorts(), null);

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private void addPartToPanel(String partName, JPanel kitPanel, KitPartView part, String predominantTitle) {
        JPanel box;
        if(predominantTitle != null) {
            box = new JPanel();
            BoxLayout boxLayout = new BoxLayout(box,BoxLayout.Y_AXIS);
            box.setLayout(boxLayout);

            JPanel predominantPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            predominantPanel.add(new JLabel(predominantTitle));
            predominantPanel.add(teamKitsView.getPredominantColor());
            //predominantPanel.setBorder(BorderFactory.createTitledBorder(predominantTitle));


            addPartToPanel(partName, box, part);
            box.add(predominantPanel);
            kitPanel.add(box);
        } else box = addPartToPanel(partName, kitPanel, part);

        box.setBorder(BorderFactory.createTitledBorder(partName));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setMaximumSize( box.getPreferredSize() );

    }
    private JPanel addPartToPanel(String partName, JPanel kitPanel, KitPartView part) {
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


        /*if(partName.equals("Shirt and Socks")) {
            Dimension currentMaximum = partPanel.getMaximumSize();
            Dimension newMaximum = new Dimension(currentMaximum.width /2, currentMaximum.height * 2);
            partPanel.setMaximumSize(newMaximum);
            partPanel.setSize(newMaximum);

        }   */
        kitPanel.add(partPanel);
        return partPanel;
    }


    @Override
    public TeamKits getData() {
        return teamKitsView.toModel();
    }

    @Override
    public void setData(TeamKits teamKits) {
        //teamKitsLabel.setText(teamKits.getFirst().getShirtAndSocks().getRgbs()[0].toString());
        //teamKitsLabel.repaint();
        teamKitsView.setFromModel(teamKits);
    }
}

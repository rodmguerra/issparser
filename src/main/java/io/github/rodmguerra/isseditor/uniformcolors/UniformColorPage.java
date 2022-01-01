package io.github.rodmguerra.isseditor.uniformcolors;

import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.model.colors.uniforms.TeamUniforms;

import javax.swing.*;
import java.awt.*;

import static io.github.rodmguerra.isseditor.Texts.string;

public class UniformColorPage extends AbstractTeamPage<TeamUniforms> {

    private JLabel teamKitsLabel;
    private TeamUniformsView teamKitsView;
    private static final String PAGE = "rom.feature.uniform_colors";

    public UniformColorPage(){
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
        if(teamKitsView == null) teamKitsView = TeamUniformsView.zero();
        innerPanel.add(panelForKit(string(PAGE, "first", "title"), teamKitsView.getFirst(), string(PAGE, "first", "predominant", "title")));
        innerPanel.add(panelForKit(string(PAGE, "second", "title"), teamKitsView.getSecond()));
        innerPanel.add(panelForKeeperKit(string(PAGE, "goalkeeper", "title"), teamKitsView.getGoalkeeper()));


        return innerPanel;
    }


    private JPanel panelForKit(String title, UniformView kit) {
        return panelForKit(title, kit, null);
    }
    private JPanel panelForKit(String title, UniformView kit, String predominantTitle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        addPartToPanel(string(PAGE, "shirt", "title"), panel, kit.getShirt(), predominantTitle);
        addPartToPanel(string(PAGE, "shorts", "title"), panel, kit.getShorts(), null);
        addPartToPanel(string(PAGE, "socks", "title"), panel, kit.getSocks(), null);


        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private JPanel panelForKeeperKit(String title, KeeperUniformView kit) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);
        addPartToPanel(string(PAGE, "shirt_and_socks", "title"), panel, kit.getShirtAndSocks(), null);
        addPartToPanel(string(PAGE, "shorts", "title"), panel, kit.getShorts(), null);

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private void addPartToPanel(String partName, JPanel kitPanel, ColoredPartView part, String predominantTitle) {
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
    private JPanel addPartToPanel(String partName, JPanel kitPanel, ColoredPartView part) {
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
    public TeamUniforms getData() {
        return teamKitsView.toModel();
    }

    @Override
    public void setData(TeamUniforms teamKits) {
        //teamKitsLabel.setText(teamKits.getFirst().getShirtAndSocks().getRgbs()[0].toString());
        //teamKitsLabel.repaint();
        teamKitsView.setFromModel(teamKits);
    }
}

package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.issparser.model.colors.hairandskin.TeamHairAndSkin;

import javax.swing.*;
import java.awt.*;

public class HairAndSkinColorPage extends AbstractTeamPage<TeamHairAndSkin> {
    private TeamHairAndSkinView view;

    @Override
    protected int resourceIndex() {
        return 2;
    }

    @Override
    protected Component innerPannel() {
        JPanel innerPanel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setAlignOnBaseline(true);
        innerPanel.setLayout(layout);
        if(view == null) view = TeamHairAndSkinView.zero();
        innerPanel.add(panelFor("First", view.getFirst()));
        innerPanel.add(panelFor("Second", view.getSecond()));
        innerPanel.add(panelFor("Goalkeeper", view.getGoalkeeper()));
        return innerPanel;
    }


    private JPanel panelFor(String title, HairAndSkinView hs) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        panel.add(panelForPart("Hair", hs.getHair()));
        panel.add(panelForPart("Skin", hs.getSkin()));

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }

    private JPanel panelForPart(String partName, ColoredPartView part) {
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
        return partPanel;
    }

    @Override
    public TeamHairAndSkin getData() {
        return view.toModel();
    }

    @Override
    public void setData(TeamHairAndSkin model) {
        view.setFromModel(model);
    }
}

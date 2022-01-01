package io.github.rodmguerra.isseditor.hairandskin;

import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.issparser.model.HairAndSkinCombo;
import io.github.rodmguerra.issparser.model.colors.hairandskin.NormalHairAndSkin;

import javax.swing.*;
import java.awt.*;

import static io.github.rodmguerra.isseditor.Texts.string;

public class HairAndSkinColorPage extends AbstractTeamPage<HairAndSkinCombo> {
    private TeamHairAndSkinView view;
    private static final String PAGE = "rom.feature.player_colors";

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
        innerPanel.add(panelFor(string(PAGE, "normal", "title"), view.getFirst()));
        innerPanel.add(specialPanel(string(PAGE, "special", "title")));
        //innerPanel.add(panelFor("Second", view.getSecond()));
        innerPanel.add(panelFor(string(PAGE, "goalkeeper", "title"), view.getGoalkeeper()));
        return innerPanel;
    }

    private Component specialPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        JPanel hairPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hairPanel.add(view.getSpecialHairField());
        hairPanel.setBorder(BorderFactory.createTitledBorder(string(PAGE, "hair", "title")));
        JPanel skinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        skinPanel.add(view.getSpecialSkinField());
        skinPanel.setBorder(BorderFactory.createTitledBorder(string(PAGE, "skin", "title")));

        panel.add(hairPanel);
        panel.add(skinPanel);

        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize( panel.getPreferredSize() );
        return panel;
    }


    private JPanel panelFor(String title, HairAndSkinView hs) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setVisible(true);

        panel.add(panelForPart(string(PAGE, "hair", "title"), hs.getHair()));
        panel.add(panelForPart(string(PAGE, "skin", "title"), hs.getSkin()));

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
    public HairAndSkinCombo getData() {
        return view.toModel();
    }

    @Override
    public void setData(HairAndSkinCombo model) {
        view.setFromModel(model);
    }
}

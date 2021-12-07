package io.github.rodmguerra.isseditor.flagdesign;

import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.colors.RGB;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.rodmguerra.isseditor.ColorUtils.colorFromSnesRGB;

public class FlagDesignPage extends AbstractTeamPage<Flag> {
    private FlagDesignView design;
    private ColoredPartView colors;

    @Override
    protected int resourceIndex() {
        return 4;
    }

    @Override
    protected Component innerPannel() {
        if (design == null) design = new FlagDesignView();
        if (colors == null) colors = ColoredPartView.zero(4);
        JPanel outer = new JPanel();
        BoxLayout layout = new BoxLayout(outer, BoxLayout.Y_AXIS);
        outer.setLayout(layout);

        JPanel[][] panelMatrix = design.getMatrix();
        GridLayout layout1 = new GridLayout(16,24);

        JPanel outputPanel = new JPanel(layout1);
        //outputPanel.setSize(160,240);
        //GridBagConstraints gbc = new GridLayout(16,24);
        outputPanel.setLayout(layout1);
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                //gbc.gridx = j;
                //gbc.gridy = i;
                Dimension preferredSize = new Dimension(22, 22);
                panelMatrix[i][j].setPreferredSize(preferredSize);
                panelMatrix[i][j].setMaximumSize(preferredSize);
                panelMatrix[i][j].setMinimumSize(preferredSize);
                panelMatrix[i][j].setSize(preferredSize);
                outputPanel.add(panelMatrix[i][j]);
            }
        }

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        JToggleButton[] selectors = design.getColorSelectors();
        java.util.List<JPanel> rgbPanels = new ArrayList<>();
        for (int i = 0; i < selectors.length; i++) {


            JPanel rgbPanel = new JPanel();
            JLabel comp = new JLabel(i==0?"Player number":" ");
            comp.setAlignmentX(Component.CENTER_ALIGNMENT);

            rgbPanels.add(rgbPanel);
            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            BoxLayout vlayout = new BoxLayout(rgbPanel, BoxLayout.Y_AXIS);
            //JPanel colorPanel = new JPanel(new FlowLayout());
            //rgbPanel.add(colorPanel);
            rgbPanel.setLayout(vlayout);
            rgbPanel.add(selectors[i]);
            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            selectors[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            RGBView[] rgbs = colors.getRgbs();
            if (i < rgbs.length) {
                rgbPanel.add(rgbs[i].getRed());
                rgbPanel.add(rgbs[i].getGreen());
                rgbPanel.add(rgbs[i].getBlue());
                rgbPanel.setBorder(BorderFactory.createTitledBorder("Color " + (i+1)));
            } else rgbPanel.setBorder(BorderFactory.createTitledBorder("Transparent " + (i + 1)));
            rgbPanel.add(comp);
            radioPanel.add(rgbPanel);

        }
        int maxHeight = 0;
        int maxWidth = 0;
        for (JPanel rgbPanel : rgbPanels) {
            if (rgbPanel.getPreferredSize().height > maxHeight)
                maxHeight = rgbPanel.getPreferredSize().height;
            if (rgbPanel.getPreferredSize().width > maxWidth)
                maxWidth = rgbPanel.getPreferredSize().width;
        }
        Dimension max = new Dimension(maxWidth, maxHeight);
        int i=0;
        for (JPanel rgbPanel : rgbPanels) {
            rgbPanel.setPreferredSize(max);
            rgbPanel.setMaximumSize(max);
            rgbPanel.setMinimumSize(max);

        }

        outer.add(outputPanel);
        outer.add(radioPanel);
        //outer.add(colorPanel(colors));
        return outer;
    }


    @Override
    public Flag getData() {
        return new Flag(design.toModel(), colors.toModel());
    }

    @Override
    public void setData(Flag model) {
        this.colors.setFromModel(model.getColors());
        this.design.setFromModel(model.getDesign());
        Map<FlagDesign.Color, Color> colors = new HashMap<>();
        FlagDesign.Color[] colorValues = FlagDesign.Color.values();
        for (int i=0; i< colorValues.length - 1; i++) {
            colors.put(colorValues[i], colorFromSnesRGB(model.getColors().getRgbs()[i]));
        }
        Color[] colors1 = Stream.<RGB>of(this.colors.toModel().getRgbs()).map(rgb -> colorFromSnesRGB(rgb)).toArray(Color[]::new);
        this.design.setColors(colors);
        this.colors.onColorChange(e -> design.setColor(FlagDesign.Color.at(e.getColorIndex()), colorFromSnesRGB(e.getRgb())));
    }

    public FlagDesignView getDesign() {
        return design;
    }

    public ColoredPartView getColors() {
        return colors;
    }
}

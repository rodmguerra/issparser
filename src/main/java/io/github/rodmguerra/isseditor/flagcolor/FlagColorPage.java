package io.github.rodmguerra.isseditor.flagcolor;

import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import javax.swing.*;
import java.awt.*;

import static io.github.rodmguerra.isseditor.Texts.string;

public class FlagColorPage extends AbstractTeamPage<ColoredPart> {
    private ColoredPartView view;
    private static final String PAGE = "rom.feature.flag_colors";

    @Override
    protected int resourceIndex() {
        return 3;
    }

    @Override
    protected Component innerPannel() {
        JPanel innerPanel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setAlignOnBaseline(true);
        innerPanel.setLayout(layout);
        if(view == null) view = ColoredPartView.zero(4);
        innerPanel.add(panelForPart(string(PAGE, "title"), view));
        return innerPanel;
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
            partPanel.setToolTipText(string(PAGE, "first", "hint"));
        }

        partPanel.setBorder(BorderFactory.createTitledBorder(partName));
        return partPanel;
    }

    @Override
    public ColoredPart getData() {
        return view.toModel();
    }

    @Override
    public void setData(ColoredPart model) {
        view.setFromModel(model);
    }
}

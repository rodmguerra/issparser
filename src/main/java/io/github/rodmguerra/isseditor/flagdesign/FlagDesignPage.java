package io.github.rodmguerra.isseditor.flagdesign;

import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;

import javax.swing.*;
import java.awt.*;

public class FlagDesignPage extends AbstractTeamPage<Flag> {
    private FlagView view;

    @Override
    protected int resourceIndex() {
        return 4;
    }

    @Override
    protected Component innerPannel() {
        if(view == null) view = new FlagView();
        JPanel[][] panelMatrix = view.getMatrix();
        JPanel outputPanel = new JPanel(new GridLayout(panelMatrix.length, panelMatrix[0].length));
        for (JPanel[] panels : panelMatrix) {
            for (JPanel panel : panels) {
                outputPanel.add(panel);
            }
        }
        return outputPanel;
    }

    @Override
    public Flag getData() {
        return view.toModel();
    }

    @Override
    public void setData(Flag model) {
        view.setFromModel(model);
    }
}

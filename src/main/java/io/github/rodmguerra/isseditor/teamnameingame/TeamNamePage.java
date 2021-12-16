package io.github.rodmguerra.isseditor.teamnameingame;

import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.handlers.texts.TeamName;
import io.github.rodmguerra.issparser.model.TeamNameInMenu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamNamePage extends AbstractTeamPage<TeamName> {
    private TeamNameInGameView inGameView;
    private TeamNameInMenu inMenuModel;
    private JTextField inMenuView;

    @Override
    protected int resourceIndex() {
        return 5;
    }

    @Override
    protected Component innerPannel() {
        if (inGameView == null) inGameView = new TeamNameInGameView();
        JPanel outer = new JPanel();
        BoxLayout layout = new BoxLayout(outer, BoxLayout.Y_AXIS);
        outer.setLayout(layout);

        JPanel[][] panelMatrix = inGameView.getMatrix();
        GridLayout layout1 = new GridLayout(8, 32);

        JPanel outputPanel = new JPanel(layout1);
        //outputPanel.setSize(16 *15, 24*15);
        //GridBagConstraints gbc = new GridLayout(16,24);
        outputPanel.setLayout(layout1);
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                //gbc.gridx = j;
                //gbc.gridy = i;
                Dimension preferredSize = new Dimension(13, 13);
                panelMatrix[i][j].setPreferredSize(preferredSize);
                panelMatrix[i][j].setMaximumSize(preferredSize);
                panelMatrix[i][j].setMinimumSize(preferredSize);
                panelMatrix[i][j].setSize(preferredSize);
                outputPanel.add(panelMatrix[i][j]);
            }
        }

        Box selectorsPanel = new Box(BoxLayout.X_AXIS);
        selectorsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        JToggleButton[] selectors = inGameView.getColorSelectors();
        List<Box> rgbPanels = new ArrayList<>();
        for (int i = 0; i < selectors.length; i++) {


            Box rgbPanel = new Box(BoxLayout.Y_AXIS);

            rgbPanels.add(rgbPanel);

            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            rgbPanel.add(selectors[i]);
            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            selectors[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            selectorsPanel.add(rgbPanel);
            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        }
        rgbPanels.get(0).setBorder(BorderFactory.createTitledBorder("Fill"));
        rgbPanels.get(1).setBorder(BorderFactory.createTitledBorder("Corner"));
        rgbPanels.get(2).setBorder(BorderFactory.createTitledBorder("Shadow"));
        rgbPanels.get(3).setBorder(BorderFactory.createTitledBorder("Transparent"));



        outputPanel.setBorder(BorderFactory.createTitledBorder("Design in game"));
        Box inMenu = new Box(BoxLayout.X_AXIS);
        inMenu.setBorder(BorderFactory.createTitledBorder("Text in Menu"));
        inMenuView = new JTextField("GERMANYGERMANY");
        inMenuView.setAlignmentX(Component.CENTER_ALIGNMENT);
        setSize(inMenuView, inMenuView.getPreferredSize());
        inMenu.add(inMenuView);


        outer.add(inMenu);
        outer.add(outputPanel);
        outer.add(selectorsPanel);
        setSize(inMenu, new Dimension((int)outputPanel.getPreferredSize().getWidth(), inMenu.getHeight()));
        return outer;
    }

    private void setItalic(JLabel label) {
        Font f = label.getFont();
        label.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
    }

    public static void setSize(Component button, Dimension size) {
        button.setSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setSize(size);
        button.setSize(size);
    }


    @Override
    public TeamName getData() {
        if(!inMenuModel.getText().equals(inMenuView.getText())) {
            inMenuModel = TeamNameInMenu.forText(inMenuView.getText());
        }
        return new TeamName(inMenuModel, inGameView.toModel());
    }

    @Override
    public void setData(TeamName model) {
        this.inGameView.setFromModel(model.getInGame());
        this.inMenuModel = model.getInMenu();
        inMenuView.setText(inMenuModel.getText());
    }

    private List<String> truncate(List<String> stringList, int size) {
        if (stringList.size() < size) return new ArrayList<>(stringList);
        return stringList.subList(0, size);
    }
}

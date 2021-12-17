package io.github.rodmguerra.isseditor.teamname;

import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.handlers.texts.TeamName;
import io.github.rodmguerra.issparser.model.TeamNameText;
import io.github.rodmguerra.issparser.model.tiles.TeamNameTiles;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TeamNamePage extends AbstractTeamPage<TeamName> {
    private TeamNameTilesView inGameView;
    private TeamNameText inMenuModel;
    private JTextField inMenuView;

    @Override
    protected int resourceIndex() {
        return 5;
    }

    @Override
    protected Component innerPannel() {
        if (inGameView == null) inGameView = new TeamNameTilesView();
        JPanel outer = new JPanel();
        BoxLayout layout = new BoxLayout(outer, BoxLayout.Y_AXIS);
        outer.setLayout(layout);

        JPanel[][] panelMatrix = inGameView.getMatrix();
        GridLayout layout1 = new GridLayout(8, 32);
        JPanel grid = new JPanel(layout1);
        //outputPanel.setSize(16 *15, 24*15);
        //GridBagConstraints gbc = new GridLayout(16,24);
        grid.setLayout(layout1);
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                //gbc.gridx = j;
                //gbc.gridy = i;
                Dimension preferredSize = new Dimension(13, 13);
                panelMatrix[i][j].setPreferredSize(preferredSize);
                panelMatrix[i][j].setMaximumSize(preferredSize);
                panelMatrix[i][j].setMinimumSize(preferredSize);
                panelMatrix[i][j].setSize(preferredSize);
                grid.add(panelMatrix[i][j]);
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


        Box inMenu = new Box(BoxLayout.X_AXIS);
        inMenu.setBorder(BorderFactory.createTitledBorder("Text"));
        inMenuView = new JTextField("GERMANYGERMANY");
        inMenuView.setAlignmentX(Component.CENTER_ALIGNMENT);
        setSize(inMenuView, inMenuView.getPreferredSize());
        inMenu.add(inMenuView);


        outer.add(inMenu);
        Box gridBox = new Box(BoxLayout.Y_AXIS);

        JButton generateButton = new JButton("Generate...");
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridBox.add(generateButton);
        generateButton.addActionListener(e -> new GenerateModal(panel).setVisible(true));
        gridBox.add(Box.createRigidArea(new Dimension(0, 10)));
        gridBox.add(grid);
        gridBox.setBorder(BorderFactory.createTitledBorder("Tiles"));
        outer.add(gridBox);


        outer.add(selectorsPanel);
        setSize(inMenu, new Dimension((int) grid.getPreferredSize().getWidth(), inMenu.getHeight()));
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
        inMenuModel = TeamNameText.forText(inMenuView.getText());
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

    private class GenerateModal extends JDialog {

        public GenerateModal(Component component) {
            setTitle("Generate tiles from text");
            setLocationRelativeTo(component);
            getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
            JTextField generateField = new JTextField(inMenuView.getText());
            JButton generateButton = new JButton("Generate");
            Component that = this;
            generateButton.addActionListener(e -> {
                inGameView.setFromModel(TeamNameTiles.forText(generateField.getText()));
                that.setVisible(false);
            });

            /*Dimension fieldSize = generateField.getPreferredSize();
            fieldSize = new Dimension(max(fieldSize.width, 500), fieldSize.height);
            TeamNamePage.setSize(generateField, fieldSize); */
            add(generateField);
            generateField.setColumns(15);
            add(generateButton);
            Dimension preferredSize = getPreferredSize();



            TeamNamePage.setSize(this, new Dimension(preferredSize.width + 30, 80));
            setModal(true);


                        /*
            getContentPane().setLayout(new BorderLayout());

            Box checkPanel = new Box(BoxLayout.X_AXIS);
            checkBoxes = new ArrayList<>();
            teams = newArrayList(RomHandler.Team.values());
            //teams.removeAll(teamsUsingThisFlag);
            teams.removeAll(Arrays.asList(getCurrentTeam()));
            int cols = (int) Math.round(Math.sqrt(teams.size()));
            int rows = (teams.size() / cols);
            int remainder = teams.size() % cols;
            Iterator<RomHandler.Team> iterator = teams.iterator();
            for (int i = 0; i < cols; i++) {
                JPanel panel = new JPanel();
                BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(layout);
                panel.setAlignmentY(Component.TOP_ALIGNMENT);
                int rowsInCol = remainder-- > 0 ? rows + 1 : rows;
                for (int j = 0; j < rowsInCol; j++) {
                    RomHandler.Team team = iterator.next();
                    JCheckBox checkBox = new JCheckBox(team.toString());
                    panel.add(checkBox);
                    checkBoxes.add(checkBox);
                    checkBox.setSelected(teamsUsingThisFlag.contains(team));
                }
                checkPanel.add(panel);
            }
            JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            saveButton = new JButton("OK");
            GenerateModal that = this;
            saveButton.addActionListener(e -> {
                List<RomHandler.Team> selectedTeams =
                        checkBoxes.stream()
                                .filter(t -> t.isSelected())
                                .map(c -> teams.get((checkBoxes.indexOf(c))))
                                .collect(toList());
                Set<RomHandler.Team> setTeams = new LinkedHashSet<>();
                setTeams.addAll(selectedTeams);
                setTeams.add(getCurrentTeam());
                setTeamsUsing(setTeams);
                that.setVisible(false);
                panel.repaint();
            });
            JButton cancelButton = new JButton("Cancel");

            cancelButton.addActionListener(e -> that.setVisible(false));
            buttonBox.add(cancelButton);
            buttonBox.add(saveButton);
            buttonBox.setAlignmentX(RIGHT_ALIGNMENT);

            setModal(true);
            add(checkPanel, BorderLayout.PAGE_START);
            add(buttonBox, BorderLayout.PAGE_END);

            Dimension preferredSize = getPreferredSize();
            FlagDesignPage.setSize(this, new Dimension(preferredSize.width + 50, preferredSize.height + 50));
            setLocationRelativeTo(component);
            setResizable(false);

            */


        }

    }

}

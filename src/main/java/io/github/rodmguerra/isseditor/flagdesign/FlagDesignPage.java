package io.github.rodmguerra.isseditor.flagdesign;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.github.rodmguerra.isseditor.color.views.ColoredPartView;
import io.github.rodmguerra.isseditor.color.views.RGBView;
import io.github.rodmguerra.isseditor.team.AbstractTeamPage;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.FlagDesign;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

import static io.github.rodmguerra.isseditor.ColorUtils.colorFromSnesRGB;
import static java.util.stream.Collectors.toList;

public class FlagDesignPage extends AbstractTeamPage<Flag> {
    private FlagDesignView design;
    private ColoredPartView colors;
    private JLabel teamsUsing;
    private List<RomHandler.Team> teamsUsingThisFlag = new ArrayList<>();
    private JButton moveButton;
    private Runnable moveOutConsumer = () -> {
    };
    private Consumer<Iterable<RomHandler.Team>> moveInConsumer = t -> {
    };

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
        GridLayout layout1 = new GridLayout(16, 24);

        JPanel outputPanel = new JPanel(layout1);
        //outputPanel.setSize(16 *15, 24*15);
        //GridBagConstraints gbc = new GridLayout(16,24);
        outputPanel.setLayout(layout1);
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                //gbc.gridx = j;
                //gbc.gridy = i;
                Dimension preferredSize = new Dimension(18, 18);
                panelMatrix[i][j].setPreferredSize(preferredSize);
                panelMatrix[i][j].setMaximumSize(preferredSize);
                panelMatrix[i][j].setMinimumSize(preferredSize);
                panelMatrix[i][j].setSize(preferredSize);
                outputPanel.add(panelMatrix[i][j]);
            }
        }

        Box radioPanel = new Box(BoxLayout.X_AXIS);
        radioPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        JToggleButton[] selectors = design.getColorSelectors();
        java.util.List<Box> rgbPanels = new ArrayList<>();
        for (int i = 0; i < selectors.length; i++) {


            Box rgbPanel = new Box(BoxLayout.Y_AXIS);

            rgbPanels.add(rgbPanel);

            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            //BoxLayout vlayout = new BoxLayout(rgbPanel, BoxLayout.Y_AXIS);
            //JPanel colorPanel = new JPanel(new FlowLayout());
            //rgbPanel.add(colorPanel);
            //rgbPanel.setLayout(vlayout);
            rgbPanel.add(selectors[i]);
            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            selectors[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            RGBView[] rgbs = colors.getRgbs();
            if (i < rgbs.length) {
                rgbPanel.add(rgbs[i].getRed());
                rgbPanel.add(rgbs[i].getGreen());
                rgbPanel.add(rgbs[i].getBlue());
                String title = "Color " + (i + 1);
                rgbPanel.setBorder(BorderFactory.createTitledBorder(title));
                if(i == 0) rgbPanel.setToolTipText("Color 1 is also used for player number on shirt");
            } else rgbPanel.setBorder(BorderFactory.createTitledBorder("Transparent " + (i + 1)));
            radioPanel.add(rgbPanel);

            rgbPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        }

        /*
        int maxHeight = 0;
        int maxWidth = 0;
        for (JPanel rgbPanel : rgbPanels) {
            if (rgbPanel.getPreferredSize().height > maxHeight)
                maxHeight = rgbPanel.getPreferredSize().height;
            if (rgbPanel.getPreferredSize().width > maxWidth)
                maxWidth = rgbPanel.getPreferredSize().width;
        } 000
        Dimension max = new Dimension(maxWidth, maxHeight);
        int i = 0;
        for (JPanel rgbPanel : rgbPanels) {
            rgbPanel.setPreferredSize(max);
            rgbPanel.setMaximumSize(max);
            rgbPanel.setMinimumSize(max);

        }
             */
        JPanel pointerPanel = new JPanel(new BorderLayout());
        //teamsUsingLabel.setForeground(Color.BLUE);
        RomHandler.Team team = getCurrentTeam();
        moveButton = new JButton("Make flag exclusive of " + team );
        moveButton.addActionListener(e -> moveOutConsumer.run());
        JButton addButton = new JButton("Add another team to this flag...");
        addButton.addActionListener(e -> new AddTeamModal(pointerPanel).setVisible(true));

        //saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel teamsUsing = new JLabel("Prototype");
        setSize(teamsUsing, teamsUsing.getPreferredSize());

        //this.teamsUsing = teamsUsing;
        leftPanel.add(teamsUsing);
        pointerPanel.add(leftPanel, BorderLayout.LINE_START);
        this.teamsUsing = teamsUsing;


        JPanel rightPanel = new JPanel();
        BoxLayout rightPanelLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(rightPanelLayout);
        rightPanel.add(moveButton);
        rightPanel.add(addButton);
        pointerPanel.add(rightPanel, BorderLayout.LINE_END);
        outputPanel.setBorder(BorderFactory.createTitledBorder("Design"));
        radioPanel.setBorder(BorderFactory.createTitledBorder("Color"));
        pointerPanel.setBorder(BorderFactory.createTitledBorder("Teams using this flag"));
        outer.add(pointerPanel);
        outer.add(outputPanel);
        outer.add(radioPanel);
        Dimension size = new Dimension(outputPanel.getPreferredSize().width, pointerPanel.getPreferredSize().height);
        setSize(pointerPanel, size);

        //outer.add(colorPanel(colors));
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
    public Flag getData() {
        return new Flag(design.toModel(), colors.toModel(), null);
    }

    @Override
    public void setData(Flag model) {
        this.colors.setFromModel(model.getColors());
        this.design.setFromModel(model.getDesign());
        Map<FlagDesign.Color, Color> colors = new HashMap<>();
        FlagDesign.Color[] colorValues = FlagDesign.Color.values();
        for (int i = 0; i < colorValues.length - 1; i++) {
            colors.put(colorValues[i], colorFromSnesRGB(model.getColors().getRgbs()[i]));
        }
        this.design.setColors(colors);
        this.colors.onColorChange(e -> design.setColor(FlagDesign.Color.at(e.getColorIndex()), colorFromSnesRGB(e.getRgb())));
        //this.design.setTeamsUsingThisFlag(model.getTeamsUsing());
        List<RomHandler.Team> teams = model.getTeamsUsing();
        List<String> stringList = teams.stream().map(RomHandler.Team::toString).collect(toList());
        List<String> truncate = truncate(stringList, 5);
        String labelText = Joiner.on(", ").join(truncate);
        if(truncate.size() < stringList.size()) {
            labelText += ", ...";
            this.teamsUsing.setToolTipText(Joiner.on(", ").join(stringList));
        }
        this.teamsUsing.setText(labelText);
        this.teamsUsingThisFlag = teams;
        this.moveButton.setText("Move " + getCurrentTeam() + " to a new address");
        this.moveButton.setVisible(teams.size() > 1);
    }

    private List<String> truncate(List<String> stringList, int size) {
        if(stringList.size() < size) return new ArrayList<>(stringList);
        return stringList.subList(0, size);
    }

    public FlagDesignView getDesign() {
        return design;
    }

    public ColoredPartView getColors() {
        return colors;
    }

    public void onMoveOut(Runnable moveOutConsumer) {
        this.moveOutConsumer = moveOutConsumer;
    }

    public void onMoveIn(Consumer<Iterable<RomHandler.Team>> moveInConsumer) {
        this.moveInConsumer = moveInConsumer;
    }

    private class AddTeamModal extends JDialog {
        private final List<JCheckBox> checkBoxes;
        private final JButton saveButton;
        private final List<RomHandler.Team> teams;

        public AddTeamModal(Component component) {
            setTitle("Add another team to this flag");


            getContentPane().setLayout(new BorderLayout());

            Box checkPanel = new Box(BoxLayout.X_AXIS);
            checkBoxes = new ArrayList<>();
            teams = Lists.newArrayList(RomHandler.Team.values());
            teams.removeAll(teamsUsingThisFlag);
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
                    JCheckBox checkBox = new JCheckBox(iterator.next().toString());
                    panel.add(checkBox);
                    checkBoxes.add(checkBox);
                }
                checkPanel.add(panel);
            }
            JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            saveButton = new JButton("Save");
            AddTeamModal that = this;
            saveButton.addActionListener(e -> {
                List<RomHandler.Team> selectedTeams =
                        checkBoxes.stream()
                                .filter(t -> t.isSelected())
                                .map(c -> teams.get((checkBoxes.indexOf(c))))
                                .collect(toList());
                moveInConsumer.accept(selectedTeams);
                that.setVisible(false);
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


        }
    }

}

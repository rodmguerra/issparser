package io.github.rodmguerra.isseditor.teamnameingame;


import com.google.common.collect.ImmutableMap;
import io.github.rodmguerra.issparser.model.tiles.TeamNameInGame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TeamNameInGameView {
    private Map<TeamNameInGame.Color, Color> colorMap = ImmutableMap.of(
            TeamNameInGame.Color.COLOR_1, new Color(247,255,247),
            TeamNameInGame.Color.COLOR_2, new Color(132,166,239),
            TeamNameInGame.Color.COLOR_3, new Color(0,81,247),
            TeamNameInGame.Color.TRANSPARENT, new Color(148,174,90));

    private final JPanel[][] matrix = new JPanel[8][32];
    private final JToggleButton[] buttons = new JToggleButton[TeamNameInGame.Color.values().length];
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<ButtonModel, TeamNameInGame.Color> colorSelectors = new HashMap<>();


    private TeamNameInGame model = new TeamNameInGame(new TeamNameInGame.Color[8][32]);


    public TeamNameInGameView() {
        TeamNameInGame.Color[] colorValues = TeamNameInGame.Color.values();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new JPanel();
                matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                final int row = i;
                final int col = j;
                matrix[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        paint(row, col);
                    }
                });

                matrix[i][j].addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        Point location = MouseInfo.getPointerInfo().getLocation();
                        JPanel parent = (JPanel) e.getComponent().getParent();
                        SwingUtilities.convertPointFromScreen(location, parent);
                        JPanel component = (JPanel) parent.getComponentAt(location);
                        //System.out.println(component.getBackground());

                        if (component != null) {
                            Coordinate coordinates = getPanelPosition(component);
                            if (coordinates != null) {
                                paint(coordinates.getI(), coordinates.getJ());
                            }
                        }
                    }
                });

            }
        }

        int i = 0;

        for (TeamNameInGame.Color color : colorValues) {
            JToggleButton button = new JToggleButton(imageIcon(color));
            buttonGroup.add(button);
            colorSelectors.put(button.getModel(), color);
            buttons[i++] = button;
        }
        buttons[0].setSelected(true);
    }

    private void paint(int row, int col) {
        JComponent component = matrix[row][col];
        TeamNameInGame.Color selectedColor = getSelectedColor();
        component.setForeground(colorMap.get(selectedColor));
        component.setBackground(colorMap.get(selectedColor));
        model.getMatrix()[row][col] = selectedColor;
        if (selectedColor == TeamNameInGame.Color.TRANSPARENT) {
            component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        } else {
            component.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }

    public void setFromModel(TeamNameInGame model) {
        this.model = model;
        updateGrid();
    }

    private void updateGrid() {
        TeamNameInGame.Color[][] modelMatrix = model.getMatrix();
        for (int i = 0; i < modelMatrix.length; i++) {
            for (int j = 0; j < modelMatrix[i].length; j++) {
                Color bg = colorMap.get(modelMatrix[i][j]);
                matrix[i][j].setBackground(bg);
                if (modelMatrix[i][j] == TeamNameInGame.Color.TRANSPARENT) {
                    matrix[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                } else {
                    matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            TeamNameInGame.Color color = TeamNameInGame.Color.at(i);
            if (color != TeamNameInGame.Color.TRANSPARENT) {
                buttons[i].setIcon(imageIcon(color));
            }
        }
    }

    private TeamNameInGame.Color getSelectedColor() {
        return colorSelectors.get(buttonGroup.getSelection());
    }

    public JPanel[][] getMatrix() {
        return matrix;
    }

    public TeamNameInGame toModel() {
        return model;
    }

    public JToggleButton[] getColorSelectors() {
        return buttons;
    }

    private ImageIcon imageIcon(TeamNameInGame.Color color) {
        return imageIcon(colorMap.get(color), color == TeamNameInGame.Color.TRANSPARENT? Color.GRAY : Color.BLACK);
    }

    private ImageIcon imageIcon(Color color, Color rect) {
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setPaint(rect);
        graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        return new ImageIcon(image);
    }

    private ImageIcon emptyImageIcon() {
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.GRAY);

        graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        return new ImageIcon(image);
    }

    private Coordinate getPanelPosition(JPanel panel) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].equals(panel)) return new Coordinate(i, j);
            }
        }
        return null;
    }

    private static class Coordinate {
        private final int i;
        private final int j;

        private Coordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }

        private int getI() {
            return i;
        }

        private int getJ() {
            return j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (i != that.i) return false;
            if (j != that.j) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }
    }
}

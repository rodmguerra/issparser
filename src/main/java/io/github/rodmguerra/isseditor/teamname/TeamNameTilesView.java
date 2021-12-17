package io.github.rodmguerra.isseditor.teamname;


import com.google.common.collect.ImmutableMap;
import io.github.rodmguerra.issparser.model.tiles.TeamNameTiles;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TeamNameTilesView {
    private Map<TeamNameTiles.Color, Color> colorMap = ImmutableMap.of(
            TeamNameTiles.Color.COLOR_1, new Color(247,255,247),
            TeamNameTiles.Color.COLOR_2, new Color(132,166,239),
            TeamNameTiles.Color.COLOR_3, new Color(0,81,247),
            TeamNameTiles.Color.TRANSPARENT, new Color(148,174,90));

    private final JPanel[][] matrix = new JPanel[8][32];
    private final JToggleButton[] buttons = new JToggleButton[TeamNameTiles.Color.values().length];
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<ButtonModel, TeamNameTiles.Color> colorSelectors = new HashMap<>();


    private TeamNameTiles model = new TeamNameTiles(new TeamNameTiles.Color[8][32]);


    public TeamNameTilesView() {
        TeamNameTiles.Color[] colorValues = TeamNameTiles.Color.values();

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

        for (TeamNameTiles.Color color : colorValues) {
            JToggleButton button = new JToggleButton(imageIcon(color));
            buttonGroup.add(button);
            colorSelectors.put(button.getModel(), color);
            buttons[i++] = button;
        }
        buttons[0].setSelected(true);
    }

    private void paint(int row, int col) {
        JComponent component = matrix[row][col];
        TeamNameTiles.Color selectedColor = getSelectedColor();
        component.setForeground(colorMap.get(selectedColor));
        component.setBackground(colorMap.get(selectedColor));
        model.getMatrix()[row][col] = selectedColor;
        if (selectedColor == TeamNameTiles.Color.TRANSPARENT) {
            component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        } else {
            component.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
    }

    public void setFromModel(TeamNameTiles model) {
        this.model = model;
        updateGrid();
    }

    private void updateGrid() {
        TeamNameTiles.Color[][] modelMatrix = model.getMatrix();
        for (int i = 0; i < modelMatrix.length; i++) {
            for (int j = 0; j < modelMatrix[i].length; j++) {
                Color bg = colorMap.get(modelMatrix[i][j]);
                matrix[i][j].setBackground(bg);
                if (modelMatrix[i][j] == TeamNameTiles.Color.TRANSPARENT) {
                    matrix[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                } else {
                    matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            TeamNameTiles.Color color = TeamNameTiles.Color.at(i);
            if (color != TeamNameTiles.Color.TRANSPARENT) {
                buttons[i].setIcon(imageIcon(color));
            }
        }
    }

    private TeamNameTiles.Color getSelectedColor() {
        return colorSelectors.get(buttonGroup.getSelection());
    }

    public JPanel[][] getMatrix() {
        return matrix;
    }

    public TeamNameTiles toModel() {
        return model;
    }

    public JToggleButton[] getColorSelectors() {
        return buttons;
    }

    private ImageIcon imageIcon(TeamNameTiles.Color color) {
        return imageIcon(colorMap.get(color), color == TeamNameTiles.Color.TRANSPARENT? Color.GRAY : Color.BLACK);
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

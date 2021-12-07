package io.github.rodmguerra.isseditor.flagdesign;


import com.google.common.collect.Maps;
import io.github.rodmguerra.isseditor.ColorUtils;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.colors.RGB;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FlagDesignView {
    private Map<FlagDesign.Color, Color> colors;

    private final JPanel[][] matrix = new JPanel[16][24];
    private final JToggleButton[] buttons = new JToggleButton[FlagDesign.Color.values().length];
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<ButtonModel, FlagDesign.Color> colorSelectors = new HashMap<>();


    private FlagDesign model = new FlagDesign(new FlagDesign.Color[16][24]);

    public FlagDesignView() {
        FlagDesign.Color[] colorValues = FlagDesign.Color.values();
        colors = Maps.newEnumMap(FlagDesign.Color.class);

        for (FlagDesign.Color color : colorValues) {
            colors.put(color, UIManager.getColor("Panel.background"));
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new JPanel();
                matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                final int row = i;
                final int col = j;
                matrix[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        FlagDesign.Color selectedColor = getSelectedColor();
                        matrix[row][col].setForeground(colors.get(selectedColor));
                        matrix[row][col].setBackground(colors.get(selectedColor));
                        model.getMatrix()[row][col] = selectedColor;

                        if (selectedColor == FlagDesign.Color.TRANSPARENT) {
                            matrix[row][col].setBorder(null);
                        } else {
                            matrix[row][col].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                        }
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
                            int row = coordinates.getI();
                            int col = coordinates.getJ();
                            FlagDesign.Color selectedColor = getSelectedColor();
                            component.setForeground(colors.get(selectedColor));
                            component.setBackground(colors.get(selectedColor));
                            model.getMatrix()[row][col] = selectedColor;
                            if (selectedColor == FlagDesign.Color.TRANSPARENT) {
                                component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                            } else {
                                component.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                            }
                        }
                    }
                });

            }
        }

        int i = 0;

        for (FlagDesign.Color color : colorValues) {
            JToggleButton button = new JToggleButton(color == FlagDesign.Color.TRANSPARENT ?
                    emptyImageIcon() : imageIcon(colors.get(color)));
            buttonGroup.add(button);
            colorSelectors.put(button.getModel(), color);
            buttons[i++] = button;
        }
        buttons[0].setSelected(true);
    }

    public void setFromModel(FlagDesign model) {
        this.model = model;
        updateGrid();
    }

    public void setColor(FlagDesign.Color designColor, Color uiColor) {
        this.colors.put(designColor, uiColor);
        updateGrid();
    }

    public void setColors(Map<FlagDesign.Color, Color> colors) {
        this.colors.putAll(colors);
        updateGrid();
    }

    private void updateGrid() {
        FlagDesign.Color[][] modelMatrix = model.getMatrix();
        for (int i = 0; i < modelMatrix.length; i++) {
            for (int j = 0; j < modelMatrix[i].length; j++) {
                matrix[i][j].setBackground(colors.get(modelMatrix[i][j]));
                if (modelMatrix[i][j] == FlagDesign.Color.TRANSPARENT) {
                    matrix[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                } else {
                    matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            FlagDesign.Color color = FlagDesign.Color.at(i);
            if (color != FlagDesign.Color.TRANSPARENT) {
                buttons[i].setIcon(imageIcon(colors.get(color)));
            }
        }
    }

    private FlagDesign.Color getSelectedColor() {
        return colorSelectors.get(buttonGroup.getSelection());
    }

    public JPanel[][] getMatrix() {
        return matrix;
    }

    public FlagDesign toModel() {
        return model;
    }

    public JToggleButton[] getColorSelectors() {
        return buttons;
    }

    private ImageIcon imageIcon(Color color) {
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setPaint(Color.BLACK);
        graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        return new ImageIcon(image);
    }

    private ImageIcon emptyImageIcon() {
        BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        //
        //
        graphics.setPaint(Color.GRAY);
        //graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        return new ImageIcon(image);
    }

    private Coordinate getPanelPosition(JPanel panel) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j].equals(panel)) return new Coordinate(i,j);
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

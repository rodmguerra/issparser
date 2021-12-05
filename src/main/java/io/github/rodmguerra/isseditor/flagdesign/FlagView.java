package io.github.rodmguerra.isseditor.flagdesign;


import io.github.rodmguerra.isseditor.ColorUtils;
import io.github.rodmguerra.issparser.model.Flag;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.colors.ColoredPart;
import io.github.rodmguerra.issparser.model.colors.RGB;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class FlagView {
   JPanel[][] matrix = new JPanel[16][24];

    public FlagView() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new JPanel();
                matrix[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            }
        }
    }

    public void setFromModel(Flag model) {
        RGB[] rgbs = model.getColors().getRgbs();
        FlagDesign.Color[][] matrix = model.getDesign().getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                RGB rgb = null;
                switch(matrix[i][j]) {
                    case COLOR_1: rgb = rgbs[0]; break;
                    case COLOR_2: rgb = rgbs[1]; break;
                    case COLOR_3: rgb = rgbs[2]; break;
                    case COLOR_4: rgb = rgbs[3]; break;
                }

                if(rgb == null) {
                    this.matrix[i][j].setOpaque(false);
                } else {
                    this.matrix[i][j].setOpaque(true);
                    this.matrix[i][j].setForeground(ColorUtils.colorFromSnesRGB(rgb));
                    this.matrix[i][j].setBackground(ColorUtils.colorFromSnesRGB(rgb));
                }
            }
        }
    }

    public JPanel[][] getMatrix() {
        return matrix;
    }

    public Flag toModel() {
        //return new Flag(new FlagDesign())
        return null;
    }
}

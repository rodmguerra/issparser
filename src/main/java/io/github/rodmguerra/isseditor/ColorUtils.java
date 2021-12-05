package io.github.rodmguerra.isseditor;

import io.github.rodmguerra.issparser.model.colors.RGB;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 03/12/21
 * Time: 04:41
 * To change this template use File | Settings | File Templates.
 */
public class ColorUtils {
    public static Color colorFromSnesRGB(RGB rgb) {
        int redLevel = rgb.getRed();
        int greenLevel = rgb.getGreen();
        int blueLevel = rgb.getBlue();
        int systemRed = redLevel > 15 ? (redLevel + 1) * 8 - 1 : redLevel * 8;
        int systemGreen = greenLevel > 15 ? (greenLevel + 1) * 8 - 1 : greenLevel * 8;
        int systemBlue = blueLevel > 15 ? (blueLevel + 1) * 8 - 1 : blueLevel * 8;
        return new Color(systemRed, systemGreen, systemBlue);
    }
}

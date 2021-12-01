package io.github.rodmguerra.isseditor.color.views;

import io.github.rodmguerra.issparser.model.colors.RGB;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class RGBView {
    private final JSpinner red;
    private final JSpinner green;
    private final JSpinner blue;
    private final JPanel display;


    /*
    public static RGBView fromModel(RGB rgb) {
        JSpinner red = new JSpinner(new SpinnerNumberModel(rgb.getRed(), 0,31,1));
        JSpinner green = new JSpinner(new SpinnerNumberModel(rgb.getGreen(), 0,31,1));
        JSpinner blue = new JSpinner(new SpinnerNumberModel(rgb.getBlue(), 0,31,1));
        return new RGBView(red, green, blue, null);
    }
    */


    public RGBView(JSpinner red, JSpinner green, JSpinner blue, JPanel display) {

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.display = display;
    }

    public JSpinner getRed() {
        return red;
    }

    public JSpinner getGreen() {
        return green;
    }

    public JSpinner getBlue() {
        return blue;
    }

    public Component getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return "io.github.rodmguerra.issparser.color.model.RGB{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    public static RGBView zero() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        JSpinner red = new JSpinner(new SpinnerNumberModel(0, 0, 31, 1));
        JSpinner green = new JSpinner(new SpinnerNumberModel(0, 0, 31, 1));
        JSpinner blue = new JSpinner(new SpinnerNumberModel(0, 0, 31, 1));

        Dimension size = new Dimension(50, 50);
        JPanel display = new JPanel();
        display.setMinimumSize(size);
        display.setMaximumSize(size);
        display.setPreferredSize(size);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.BLACK);
        RGBView view = new RGBView(red, green, blue, display);
        ChangeListener listener = e -> {
            Color color = view.getColor();
            display.setBackground(color);
            display.setForeground(color);
            display.repaint();
        };
        red.addChangeListener(listener);
        green.addChangeListener(listener);
        blue.addChangeListener(listener);


        return view;
    }

    public void setFromModel(RGB rgb) {
        int redLevel = rgb.getRed();
        red.setValue(redLevel);
        int greenLevel = rgb.getGreen();
        green.setValue(greenLevel);
        int blueLevel = rgb.getBlue();
        blue.setValue(blueLevel);
        int systemRed = redLevel > 15 ? (redLevel + 1) * 8 - 1 : redLevel * 8;
        int systemGreen = greenLevel > 15 ? (greenLevel + 1) * 8 - 1 : greenLevel * 8;
        int systemBlue = blueLevel > 15 ? (blueLevel + 1) * 8 - 1 : blueLevel * 8;
        Color color = new Color(systemRed, systemGreen, systemBlue);
        display.setForeground(color);
        display.setBackground(color);
        display.repaint();
    }

    public Color getColor() {
        Color color = new Color((int) red.getValue() * 8, (int) green.getValue() * 8, (int) blue.getValue() * 8);
        return color;
    }

    public void setColor(Color color) {
        red.setValue(color.getRed() / 8);
        green.setValue(color.getGreen() / 8);
        blue.setValue(color.getBlue() / 8);
        display.setForeground(color);
        display.setBackground(color);
        display.repaint();
    }

    public RGB toModel() {
        return new RGB((int) red.getValue(), (int) green.getValue(), (int) blue.getValue());
    }
}

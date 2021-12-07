package io.github.rodmguerra.isseditor.color.views;

import io.github.rodmguerra.isseditor.ColorUtils;
import io.github.rodmguerra.issparser.model.colors.RGB;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.function.Consumer;

public class RGBView {
    private final JSpinner red;
    private final JSpinner green;
    private final JSpinner blue;
    private final JPanel display;
    private Consumer<RGB> colorChangeConsumer = new Consumer<RGB>() {
        @Override
        public void accept(RGB rgbView) {
        }
    };


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
        return "RGBView{" +
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
            view.colorChangeConsumer.accept(view.toModel());
        };
        red.addChangeListener(listener);
        green.addChangeListener(listener);
        blue.addChangeListener(listener);


        return view;
    }

    public void setFromModel(RGB rgb) {
        red.setValue(rgb.getRed());
        green.setValue(rgb.getGreen());
        blue.setValue(rgb.getBlue());
        Color color = ColorUtils.colorFromSnesRGB(rgb);
        display.setForeground(color);
        display.setBackground(color);
        display.repaint();
    }


    public Color getColor() {
        RGB rgb = new RGB((int)red.getValue(), (int)green.getValue(), (int)blue.getValue());
        return ColorUtils.colorFromSnesRGB(rgb);
    }

    public void setColor(Color color) {
        red.setValue(color.getRed() / 8);
        green.setValue(color.getGreen() / 8);
        blue.setValue(color.getBlue() / 8);
        display.setForeground(color);
        display.setBackground(color);
        display.repaint();
    }

    public void onColorChange(Consumer<RGB> consumer) {
        this.colorChangeConsumer = consumer;
    }

    public RGB toModel() {
        return new RGB((int) red.getValue(), (int) green.getValue(), (int) blue.getValue());
    }

}

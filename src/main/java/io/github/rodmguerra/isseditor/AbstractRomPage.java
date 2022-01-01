package io.github.rodmguerra.isseditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.rodmguerra.isseditor.Texts.string;

public abstract class AbstractRomPage<T>  {
    public static final String[] RESOURCES = new String[]{
            featureTitle("player"),
            featureTitle("uniform_colors"),
            featureTitle("player_colors"),
            featureTitle("flag_colors"),
            featureTitle("flag_design"),
            featureTitle("team_name")
    };
    protected Consumer<T> saveListener;
    protected Runnable nextResourceListener;
    protected Runnable previousResourceListener;
    protected Runnable readListener;
    protected Consumer<Integer> resourceListener;
    protected JPanel panel;
    private JComboBox<String> resourceCombo;


    public void setSaveListener(Consumer<T> saveListener) {
        this.saveListener = saveListener;
    }

    public void setReadListener(Runnable readListener) {
        this.readListener = readListener;
    }

    public void setNextResourceListener(Runnable nextResourceListener) {
        this.nextResourceListener = nextResourceListener;
    }

    public void setPreviousResourceListener(Runnable previousResourceListener) {
        this.previousResourceListener = previousResourceListener;
    }

    public void setResourceListener(Consumer<Integer> resourceListener) {
        this.resourceListener = resourceListener;
    }

    public AbstractRomPage() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        BoxLayout grid = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(grid);


         /* Change action */
        JPanel resourceChangePanel = new JPanel(new GridLayout(1,2));
        loadResourcePanel(resourceChangePanel);
        panel.add(resourceChangePanel);



    }

    private void loadResourcePanel(JPanel resourceChangePanel) {
        JButton previousActionButton = new JButton("<<");
        previousActionButton.addActionListener(e -> previousResourceListener.run());
        resourceChangePanel.add(previousActionButton);
        resourceCombo = new JComboBox<>(RESOURCES);
        setSelectedIndexSafe(resourceCombo, resourceIndex());
        resourceCombo.repaint();
        resourceCombo.addItemListener(e -> {
            int nextIndex = resourceCombo.getSelectedIndex();
            resourceListener.accept(nextIndex);
            //Returns combo to initial state
            setSelectedIndexSafe(resourceCombo, resourceIndex());
        });
        resourceChangePanel.add(resourceCombo);
        JButton nextResourceButton = new JButton(">>");
        nextResourceButton.addActionListener(e -> nextResourceListener.run());
        resourceChangePanel.add(nextResourceButton);
    }

    private void setSelectedIndexSafe(JComboBox combo, int newIndex) {
        ArrayList<ItemListener> listeners = newArrayList(combo.getItemListeners());
        listeners.forEach(combo::removeItemListener);
        combo.setSelectedIndex(newIndex);
        listeners.forEach(combo::addItemListener);
    }

    protected abstract int resourceIndex();

    private static String featureTitle(String feature) {
        return string("rom", "feature", feature, "title");
    }
}

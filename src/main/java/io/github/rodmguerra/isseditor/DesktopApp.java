package io.github.rodmguerra.isseditor;

import com.google.common.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DesktopApp {

    private Router router;

    private DesktopApp() {




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

        JFrame frame = new JFrame();//creatinginstance of JFrame
        frame.setIconImage(new ImageIcon(Resources.getResource("logo.png")).getImage());

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Exception handling: ");
            JOptionPane.showMessageDialog(frame, e.getMessage());
            e.printStackTrace();
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        router = new Router(frame);
        JMenuBar menuBar = new JMenuBar();
        JMenu file = subMenu(menuBar, "File");
        JMenuItem open = subMenuItem(file, Texts.OPEN_ROM);
        open.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File rom = fileChooser.getSelectedFile();
                router.navigate(Router.Route.PLAYER_NAMES, router.getState().withRom(rom));
            }
            ;
        });

        frame.setJMenuBar(menuBar);
        BoxLayout grid = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(grid);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setTitle(Texts.TITLE);
        router.navigate(Router.Route.HOME, new State());
    }

    public static void main(String[] args) {
        DesktopApp app = new DesktopApp();
    }

    private static final JMenuItem subMenuItem(JComponent component, String name) {
        JMenuItem submenu = new JMenuItem(name);
        component.add(submenu);
        return submenu;
    }

    private static final JMenu subMenu(JComponent component, String name) {
        JMenu submenu = new JMenu(name);
        component.add(submenu);
        return submenu;
    }


}

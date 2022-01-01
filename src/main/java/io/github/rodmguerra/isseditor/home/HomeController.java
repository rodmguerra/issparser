package io.github.rodmguerra.isseditor.home;

import io.github.rodmguerra.isseditor.AbstractController;
import io.github.rodmguerra.isseditor.Router;
import io.github.rodmguerra.isseditor.State;
import io.github.rodmguerra.isseditor.Texts;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static io.github.rodmguerra.isseditor.Texts.string;

public class HomeController extends AbstractController {

    @Override
    public void setState(State state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public HomeController(Router router) {
        super(router);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel innerPanel = new JPanel();
        BoxLayout layout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
        innerPanel.setLayout(layout);
        panel.add(innerPanel);

        JButton openRomButton = new JButton(string("open_rom.action"));
        openRomButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                File rom = fileChooser.getSelectedFile();
                State state = router.getState().withRom(rom);
                //if(state.getTeam() < 0) state = state.withTeam(0);
                router.navigate(Router.Route.PLAYER_NAMES, state);
            }
        });
        innerPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        innerPanel.add(new JLabel(string("title")));
        innerPanel.add(Box.createRigidArea(new Dimension(50, 10)));
        innerPanel.add(openRomButton);
        innerPanel.add(Box.createRigidArea(new Dimension(50, 50)));
    }
}

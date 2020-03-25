package tv.spielemeister.mpg.client.graphics;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends Canvas {

    private JFrame frame;

    int gameState = 0; // 0=Menu 1=Game

    public GameWindow(Rectangle bounds){
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBounds(bounds);
        frame.setResizable(false);
        frame.setTitle("Game");
        frame.setVisible(true);
    }

    

}
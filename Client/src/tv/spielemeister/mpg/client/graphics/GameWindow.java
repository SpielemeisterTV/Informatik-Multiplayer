package tv.spielemeister.mpg.client.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameWindow extends Canvas implements Runnable {

    private JFrame frame;

    int gameState = 0; // 0=Menu 1=Game

    BufferedImage image;

    public GameWindow(Rectangle bounds){
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBounds(bounds);
        frame.setResizable(false);
        frame.setTitle("Game");
        frame.setVisible(true);
    }

    @Override
    public void run() {
        BufferStrategy strategy;
        while(true){
            strategy = this.getBufferStrategy();
            if(strategy==null) {
                this.createBufferStrategy(3);
                continue;
            }

        }
    }
}
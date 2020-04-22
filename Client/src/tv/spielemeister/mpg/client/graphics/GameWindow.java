package tv.spielemeister.mpg.client.graphics;

import tv.spielemeister.mpg.client.graphics.ui.UIComponent;
import tv.spielemeister.mpg.client.input.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameWindow extends Canvas implements Runnable {

    private static final Color BACKGROUND = new Color(140, 200, 255);

    private JFrame frame;
    private InputHandler inputHandler;

    public UIComponent UI = null;

    private Rectangle bounds;

    public GameWindow(Rectangle bounds){
        this.bounds = bounds;
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(bounds);
        frame.setResizable(false);
        frame.setTitle("Game");
        frame.add(this);

        inputHandler = new InputHandler(this);

        frame.setVisible(true);

        Thread renderThread = new Thread(this);
        renderThread.setDaemon(true);
        renderThread.start();
    }

    private void renderWorld(Graphics2D graphics2D){

    }

    private void render(Graphics2D graphics){
        graphics.setColor(BACKGROUND);
        graphics.fillRect(0, 0, bounds.width, bounds.height);
        if(UI != null){
            UI.render(graphics);
            if(UI.transparent)
                renderWorld(graphics);
        } else
            renderWorld(graphics);
    }

    @Override
    public void run() {
        BufferStrategy strategy;
        while(true){
            strategy = this.getBufferStrategy();
            if(strategy==null) {
                this.createBufferStrategy(2);
                continue;
            }
            Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
            render(graphics);
            strategy.show();
        }
    }
}
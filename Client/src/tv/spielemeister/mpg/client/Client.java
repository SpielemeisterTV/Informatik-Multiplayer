package tv.spielemeister.mpg.client;

import tv.spielemeister.mpg.client.graphics.GameWindow;

import java.awt.*;

public class Client {

    private static GameWindow gameWindow;

    public static void main(String[] args){
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        gameWindow = new GameWindow(bounds);

    }

}
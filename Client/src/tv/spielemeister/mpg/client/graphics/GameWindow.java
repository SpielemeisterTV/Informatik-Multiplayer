package tv.spielemeister.mpg.client.graphics;

import tv.spielemeister.mpg.client.Client;
import tv.spielemeister.mpg.client.graphics.ui.UIComponent;
import tv.spielemeister.mpg.client.input.InputHandler;
import tv.spielemeister.mpg.engine.world.Block;
import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class GameWindow extends Canvas implements Runnable {

    // Indexed textures
    public static final BufferedImage[] textures = new BufferedImage[511]; // 511 because 0=non textured

    public static final int tileSize = 32;

    private static final Color BACKGROUND = new Color(15, 0, 30);

    private final JFrame frame;
    private final InputHandler inputHandler;

    public UIComponent UI = null;

    private final Rectangle bounds;

    public static Vector renderLocation = new Vector(10, 10);

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

        this.setBackground(BACKGROUND);

        Thread renderThread = new Thread(this);
        renderThread.setDaemon(true);
        renderThread.start();
    }

    private void renderWorld(Graphics2D graphics){
        if(renderLocation != null){
            Iterator<Block> iterator = Client.world.getBlockIterator();
            while(iterator.hasNext()){
                Block block = iterator.next();
                char[] data = block.getBlockData();
                for(int i = 0; i < 1024; i++){
                    char tile = data[i];
                    int type = Block.getType(tile);
                    if(type != 0){
                        int y = ((i & 0b1111) + 16 * block.getY()) << 5; // Add player y and y offset
                        if(y > -tileSize && y < bounds.height){
                            int x = (((i >> 4) & 0b1111) + 16 * block.getX()) << 5; // Add player x and x offset
                            if(x > -tileSize  && x < bounds.width){
                                BufferedImage texture = textures[type-1];
                                if(texture!=null)
                                    graphics.drawImage(texture, x, y, null);
                            }
                        }

                    }
                }
            }
        }
    }

    private void render(Graphics2D graphics){
        graphics.clearRect(0, 0, bounds.width, bounds.height);
        if(UI != null){
            UI.render(graphics);
            if(UI.transparent)
                renderWorld(graphics);
        } else
            renderWorld(graphics);
    }

    @Override
    public void run() {
        BufferStrategy strategy = null;
        try {
            this.createBufferStrategy(2,
                    new BufferCapabilities(new ImageCapabilities(true),
                            new ImageCapabilities(true), BufferCapabilities.FlipContents.BACKGROUND));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        int c = 0, s = 0;
        while(strategy == null) {
            strategy = this.getBufferStrategy();
        }
        Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();

        // Rendering hints to increase rendering speed
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        while(true){
            long start = System.nanoTime();
            render(graphics);
            strategy.show();
            s+= 1000000000 / (System.nanoTime()-start);
            c++;
            if(c % 300 == 0){
                System.out.println("FPS:" + (s/c));
                c = 0;
                s = 0;
            }
        }
    }
}
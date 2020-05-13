package tv.spielemeister.mpg.client.graphics;

import tv.spielemeister.mpg.client.Client;
import tv.spielemeister.mpg.client.graphics.ui.UIComponent;
import tv.spielemeister.mpg.client.input.InputHandler;
import tv.spielemeister.mpg.client.resources.ResourceManager;
import tv.spielemeister.mpg.client.resources.Texture;
import tv.spielemeister.mpg.engine.world.Block;
import tv.spielemeister.mpg.engine.world.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;

public class GameWindow extends Canvas implements Runnable {

    private static final Color BACKGROUND = new Color(140, 200, 255);

    private JFrame frame;
    private InputHandler inputHandler;

    public UIComponent UI = null;

    private Rectangle bounds;

    private int vBlocks, hBlocks;

    private ResourceManager resourceManager;

    private Location renderLocation = new Location(0, 0, 0, 0);

    public GameWindow(Rectangle bounds){
        this.bounds = bounds;
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(bounds);
        frame.setResizable(false);
        frame.setTitle("Game");
        frame.add(this);

        resourceManager = new ResourceManager();
        resourceManager.loadAllResources(new File("C:/Users/flori/Desktop/Resources"));

        inputHandler = new InputHandler(this);

        vBlocks = bounds.width / (resourceManager.tileSize * 16) + 1;
        hBlocks = bounds.height / (resourceManager.tileSize * 16) + 1;

        frame.setVisible(true);

        Thread renderThread = new Thread(this);
        renderThread.setDaemon(true);
        renderThread.start();
    }

    private void renderWorld(Graphics2D graphics2D){
        if(renderLocation != null){
            int blockX = renderLocation.getBlockX() - vBlocks << 1;
            int blockY = renderLocation.getBlockY() - hBlocks << 1;
            for(int z = 0; z < 4; z++)
                for(int x = blockX; x < vBlocks+blockX; x++)
                    for(int y = blockY; y < hBlocks+blockY; y++){
                        Block block = Client.world.getBlock(x, y);
                        if (block != null){
                            for(int tX = 0; tX <16; tX++)
                                for(int tY = 0; tY <16; tY++){
                                    char tile = block.getTile(tX, tY, z);
                                    Texture texture = resourceManager.retrieveTexture(Block.getId(tile));
                                    if(texture != null) {
                                        int drawX = ((x - blockX) * 16 + tX) * resourceManager.tileSize;
                                        int drawY = ((y - blockY) * 16 + tY) * resourceManager.tileSize;
                                        graphics2D.drawImage(texture.getImage(), drawX, drawY, null);
                                    }
                                }
                        }
                    }
        }
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
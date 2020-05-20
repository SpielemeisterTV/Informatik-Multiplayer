package tv.spielemeister.mpg.client.graphics;

import tv.spielemeister.mpg.client.Client;
import tv.spielemeister.mpg.client.graphics.ui.UIComponent;
import tv.spielemeister.mpg.client.input.InputHandler;
import tv.spielemeister.mpg.client.resources.ResourceManager;
import tv.spielemeister.mpg.client.resources.Texture;
import tv.spielemeister.mpg.engine.world.Block;
import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;

public class GameWindow extends Canvas implements Runnable {

    private static final Color BACKGROUND = new Color(15, 0, 30);

    private JFrame frame;
    private InputHandler inputHandler;

    public UIComponent UI = null;

    private Rectangle bounds;

    private final int vBlocks, hBlocks, vSub, hSub;

    private ResourceManager resourceManager;

    public static Vector renderLocation = new Location(0, 0, 0, 0);

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

        final double hB = bounds.width / (resourceManager.tileSize * 16d);
        final double vB = bounds.height / (resourceManager.tileSize * 16d);

        hSub = (int) Math.round((1 - (( hB - 1 ) /2d) %1) * resourceManager.tileSize * 16);
        vSub = (int) Math.round((1 - (( vB - 1 ) /2d) %1) * resourceManager.tileSize * 16);

        hBlocks = (int) Math.ceil(hB);
        vBlocks = (int) Math.ceil(vB);

        frame.setVisible(true);

        this.setBackground(BACKGROUND);

        Thread renderThread = new Thread(this);
        renderThread.setDaemon(true);
        renderThread.start();
    }

    private void renderWorld(Graphics2D graphics2D){
        if(renderLocation != null){
            int blockX = renderLocation.getBlockX() - (hBlocks >> 1);
            int blockY = renderLocation.getBlockY() - (vBlocks >> 1);
            blockX = Math.min(Math.max(blockX, 0), 65536-hBlocks);
            blockY = Math.min(Math.max(blockY, 0), 65536-vBlocks);
            for(int x = 0; x < hBlocks; x++) {
                int sX = x * 16 * resourceManager.tileSize;
                for (int y = 0; y < vBlocks; y++) {
                    Block block = Client.world.getBlock(x + blockX, y + blockY);
                    if (block != null) {
                        int sY = y * 16 * resourceManager.tileSize;
                        for (int tX = 0; tX < 16; tX++) {
                            int drawX = tX * resourceManager.tileSize + sX - hSub;
                            for (int tY = 0; tY < 16; tY++) {
                                int drawY = tY * resourceManager.tileSize + sY - vSub;
                                for (int tZ = 0; tZ < 4; tZ++) {
                                    char tile = block.getTile(tX, tY, tZ);
                                    if(tile!=1) {
                                        Texture texture = resourceManager.retrieveTexture(Block.getId(tile));
                                        if (texture != null) {
                                            graphics2D.drawImage(texture.getImage(), drawX, drawY, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            graphics2D.setColor(Color.RED);
        graphics2D.fillRect((renderLocation.getX()-(blockX*16))*resourceManager.tileSize-hSub,
                    (renderLocation.getY()-(blockY*16))*resourceManager.tileSize-vSub,
                    resourceManager.tileSize, resourceManager.tileSize);
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
        this.createBufferStrategy(2);
        int c = 0, s = 0;
        while(strategy == null) {
            strategy = this.getBufferStrategy();
        }
        Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
        while(true){
            long start = System.nanoTime();
            render(graphics);
            strategy.show();
            s+= 1000000000 / (System.nanoTime()-start);
            c++;
            if(c % 200 == 0){
                System.out.println(s/c);
                c = 0;
                s = 0;
            }
        }
    }
}
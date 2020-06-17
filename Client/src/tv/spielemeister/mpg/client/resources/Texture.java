package tv.spielemeister.mpg.client.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture extends Resource {

    private BufferedImage image;

    public Texture(File file, int id){
        super(id);
        try {
            BufferedImage img = ImageIO.read(file);
            image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration()
                    .createCompatibleImage(img.getWidth(), img.getHeight(), BufferedImage.OPAQUE);
            Graphics2D gfx = image.createGraphics();
            gfx.drawImage(img, 0, 0, null);
            gfx.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        return image;
    }

}

package tv.spielemeister.mpg.client.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture extends Resource{

    private BufferedImage image;

    public Texture(File file, int id){
        super(id);
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

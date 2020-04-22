package tv.spielemeister.mpg.client.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureMap {

    private static BufferedImage[] textures;
    /*
        0-25:   a-z
        26-51:  A-Z
        52-62:  0-9
     */

    public static void initTextures() {
        try {
            File textureDirectory = new File("");
            File[] files = textureDirectory.listFiles();
            if(files == null)
                return;
            textures = new BufferedImage[files.length];
            for(int i = 0; i < files.length; i++){
                File textureFile = new File(textureDirectory, i + ".png");
                if(textureFile.exists() && textureFile.isFile()){
                    BufferedImage texture = ImageIO.read(textureFile);
                    textures[i] = texture;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getTexture(int texture){
        if(texture < textures.length)
            return textures[texture];
        return null;
    }

}

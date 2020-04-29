package tv.spielemeister.mpg.engine.config;

import java.io.*;
import java.util.Properties;

public class Config extends Properties{

    public Config(File file, Properties defaults){
        super(defaults);
        try {
            if(!file.exists()) {
                file.createNewFile();
                defaults.store(new FileOutputStream(file), null);
            }
            this.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

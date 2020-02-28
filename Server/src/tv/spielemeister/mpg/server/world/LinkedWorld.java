package tv.spielemeister.mpg.server.world;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.engine.world.World;

import java.io.File;
import java.util.Map;
import java.util.Properties;

class LinkedWorld extends World {

    private File worldDir;

    private Config config;

    private File dataDir;

    public LinkedWorld(File worldDir){
        this.worldDir = worldDir;
    }

    private void init(){
        Properties configDefaults = new Properties();
        configDefaults.putAll(Map.ofEntries(

        ));
        config = new Config(new File(worldDir, "world.conf"), configDefaults);
        dataDir = new File(worldDir, "data");
        if(!dataDir.exists())
            dataDir.mkdirs();
        if(!dataDir.isDirectory()){
            System.err.println("World data directory is not a directory!");
            System.exit(0);
        }
    }

    public void unload(){

    }

}

package tv.spielemeister.mpg.server;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.server.world.WorldManager;

import java.io.File;
import java.util.Map;
import java.util.Properties;

public class Server {

    private Config config;

    File worldsDirectory;

    WorldManager worldManager;

    public Server(){
        initConfig();
        setupFiles();
        worldManager = new WorldManager(worldsDirectory, config.getProperty("Overworld world name"));
    }

    private void initConfig(){
        Properties configDefaults = new Properties();
        configDefaults.putAll(Map.ofEntries(
                Map.entry("Worlds directory", "worlds"),
                Map.entry("Overworld world name", "Overworld")
        ));
        config = new Config(new File("server.config"), configDefaults);
    }

    private void setupFiles(){
        worldsDirectory = new File(config.getProperty("Worlds directory"));
        if(!worldsDirectory.exists())
            if(!worldsDirectory.mkdirs()) {
                System.err.println("Worlds directory could not be created!");
                System.exit(0);
            }
        if(!worldsDirectory.isDirectory()){
            System.err.println("Worlds directory is not a directory!");
            System.exit(0);
        }
    }

}

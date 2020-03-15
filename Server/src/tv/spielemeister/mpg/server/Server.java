package tv.spielemeister.mpg.server;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.server.world.WorldManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Server {

    private static Server instance;

    private Config config;

    private File worldsDirectory;
    private RandomAccessFile globalData;

    private WorldManager worldManager;

    // Global data:
    private long entityCount = 0; // At position 0 in global data file

    public Server(){
        instance = this;
        initConfig();
        setupFiles();
        initData();
        worldManager = new WorldManager(worldsDirectory, config.getProperty("Overworld world name"));
    }

    private void tick(){ // Time

    }

    private void initConfig(){
        Properties configDefaults = new Properties();
        configDefaults.putAll(Map.ofEntries(
                Map.entry("Worlds directory", "worlds"),
                Map.entry("Overworld world name", "Overworld")
        ));
        config = new Config(new File("server.config"), configDefaults);
    }

    private void initData(){
        try {
            globalData.seek(0);
            entityCount = globalData.readLong();
        } catch (IOException e) {
            entityCount = 0;
            e.printStackTrace();
        }
    }

    public void incrementEntityCount(){
        try {
            globalData.seek(0);
            globalData.writeLong(entityCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try{
            File data = new File(worldsDirectory, "global_data");
            if(!data.exists())
                data.createNewFile();
            globalData = new RandomAccessFile(data, "rw");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean saveAll(){
        boolean ret = worldManager.saveAll();

        return ret;
    }

    public static Server getInstance(){
        return instance;
    }

}

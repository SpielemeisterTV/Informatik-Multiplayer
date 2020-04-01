package tv.spielemeister.mpg.server;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.entity.Entity;
import tv.spielemeister.mpg.server.world.WorldManager;

import java.io.*;
import java.util.*;

public class Server {

    private static Server instance;

    private Config config;

    private File worldsDirectory, entityDirectory;
    private RandomAccessFile globalData;

    private WorldManager worldManager;

    private HashMap<Entity, File> loadedEntities = new HashMap<>(); // Entity & Last file (for saving)

    // Global data:
    private long entityCount = 0; // At position 0 in global data file

    public Server(){
        instance = this;
        initConfig();
        setupFiles();
        initData();
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

    private void initData(){
        try {
            globalData.seek(0);
            entityCount = globalData.readLong();
        } catch (IOException e) {
            entityCount = 0;
            e.printStackTrace();
        }
    }

    public long getNewEntityID(){
        incrementEntityCount();
        return entityCount-1;
    }

    private void incrementEntityCount(){
        try {
            globalData.seek(0);
            globalData.writeLong(++entityCount);
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
            entityDirectory = new File(worldsDirectory, "entity_data");
            if(!entityDirectory.exists())
                entityDirectory.mkdir();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean saveAll(){
        for(Entity entity : loadedEntities.keySet())
            saveEntity(entity);
        return worldManager.saveAll();
    }

    private void loadEntities(int world, int blockX, int blockY){
        String dir = world + "/" + blockX + "/" + blockY;
        File directory = new File(entityDirectory, dir);
        if(directory.exists()){
            for(File entityFile : Objects.requireNonNull(directory.listFiles())){
                Entity entity = Entity.load(entityFile);
                if(entity != null)
                    loadedEntities.put(entity, entityFile);
            }
        }
    }

    private void saveEntity(Entity entity){
        File last = loadedEntities.get(entity);
        last.delete();
        Location loc = entity.getLocation();
        File newFile = new File(entityDirectory, loc.getWorld() + "/" +
                loc.getBlockX() + "/" + loc.getBlockY() + "/" + entity.getId());
        entity.save(newFile);
        loadedEntities.put(entity, newFile);
    }

    public static Server getInstance(){
        return instance;
    }
}

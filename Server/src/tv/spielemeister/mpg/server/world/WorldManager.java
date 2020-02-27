package tv.spielemeister.mpg.server.world;

import tv.spielemeister.mpg.engine.world.World;

import java.io.File;
import java.util.HashMap;

public class WorldManager {

    private File worldsDirectory;

    private HashMap<String, World> loadedWorlds = new HashMap<>();

    public WorldManager(File worldsDirectory){
        this.worldsDirectory = worldsDirectory;
    }

    public boolean loadWorld(String name){
        File worldDirectory = new File(worldsDirectory, name);
        if(worldDirectory.exists() && worldDirectory.isDirectory()){

        }
        return false;
    }

}

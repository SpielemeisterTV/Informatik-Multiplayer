package tv.spielemeister.mpg.server.world;

import java.io.File;
import java.util.HashMap;

public class WorldManager {

    private File worldsDirectory;

    private String overworldName;

    private HashMap<String, LinkedWorld> loadedWorlds = new HashMap<>();

    public WorldManager(File worldsDirectory, String overworldName){
        this.worldsDirectory = worldsDirectory;
        this.overworldName = overworldName;
    }

    public boolean loadWorld(String name){
        File worldDirectory = new File(worldsDirectory, name);
        if(worldDirectory.exists()){
            if(worldDirectory.isDirectory()) {
                loadedWorlds.put(name, new LinkedWorld(worldDirectory));
                return false;
            }
        }
        if(worldDirectory.mkdirs()){
            loadedWorlds.put(name, new LinkedWorld(worldDirectory));
            return true;
        }
        return false;
    }

    public boolean unloadWorld(String name){
        if(!name.equals(overworldName)) {
            if(loadedWorlds.containsKey(name)){
                loadedWorlds.get(name).unload();
                loadedWorlds.remove(name);
            }
        }
        return false;
    }

}

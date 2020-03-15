package tv.spielemeister.mpg.server.world;

import java.io.File;
import java.util.HashMap;

public class WorldManager {

    private File worldsDirectory;

    private String overworldName;

    private int id_counter = 0;

    private HashMap<String, LinkedWorld> loadedWorlds = new HashMap<>();

    public WorldManager(File worldsDirectory, String overworldName){
        this.worldsDirectory = worldsDirectory;
        this.overworldName = overworldName;
    }

    public boolean loadWorld(String name){
        File worldDirectory = new File(worldsDirectory, name);
        if(worldDirectory.exists()){
            if(worldDirectory.isDirectory()) {
                LinkedWorld world = new LinkedWorld(worldDirectory, id_counter);
                if(world.init()) {
                    loadedWorlds.put(name, world);
                    id_counter++;
                    return true;
                }
            }
        }
        if(worldDirectory.mkdirs()){
            LinkedWorld world = new LinkedWorld(worldDirectory, id_counter);
            if(world.init()) {
                loadedWorlds.put(name, world);
                id_counter++;
                return true;
            }
        }
        return false;
    }

    public boolean unloadWorld(String name){
        if(!name.equals(overworldName)) {
            if(loadedWorlds.containsKey(name)){
                if (loadedWorlds.get(name).save()) {
                    loadedWorlds.remove(name);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean saveWorld(String name){
        if(loadedWorlds.containsKey(name)){
            if (loadedWorlds.get(name).save()) {
                return true;
            }
        }
        return false;
    }

    public boolean saveAll(){
        boolean ret = true;
        for(LinkedWorld world : loadedWorlds.values())
            if(!world.save())
                ret = false;
        return ret;
    }

}

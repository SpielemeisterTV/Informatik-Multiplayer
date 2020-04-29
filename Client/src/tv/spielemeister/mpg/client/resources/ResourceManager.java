package tv.spielemeister.mpg.client.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceManager {

    private Texture[] textures;
    private Sound[] sounds;

    public void loadAllResources(File directory){
        if(directory.exists() && directory.isDirectory())
            loadAll(directory);
    }

    private void loadAll(File dir){
        for(File file : Objects.requireNonNull(dir.listFiles())){
            if(file.isDirectory())
                loadAll(file);
            else
                loadResource(file);
        }
    }

    public Texture retrieveTexture(int id){
        if(id < textures.length && id >= 0)
            return textures[id];
        return null;
    }

    public Sound retrieveSound(int id){
        if(id < sounds.length && id >= 0)
            return sounds[id];
        return null;
    }

    public void loadResource(File resource){
        String[] parse_info = resource.getName().split("\\.");
        int id = Integer.parseInt(parse_info[0]);
        List<Resource> resourceList = new ArrayList<>();
        int texture_count = 0;
        if(parse_info[1].equalsIgnoreCase("png")){ // Texture file
            Texture texture = new Texture(resource, id);
            texture_count++;
            resourceList.add(texture);
        }else{ // Sound file
            Sound sound = new Sound(resource, id);
            resourceList.add(sound);
        } // Storing it in arrays for faster data access
        this.textures = new Texture[texture_count];
        this.sounds = new Sound[resourceList.size() - texture_count];
        for(Resource rsc : resourceList)
            if(rsc instanceof Texture)
                this.textures[rsc.id] = (Texture) rsc;
            else
                this.sounds[rsc.id] = (Sound) rsc;
    }

}

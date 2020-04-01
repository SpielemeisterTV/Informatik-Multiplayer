package tv.spielemeister.mpg.engine.world.entity;

import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.Vector;

import java.io.*;
import java.util.HashMap;

public class Entity {

    private final static String split = "#&%";

    private HashMap<String, String> tags = new HashMap<>();

    private String displayName = "";
    private int entityType;
    private long id;

    private Location location;
    private Vector velocity;

    // A player can only see what other Entities have in their hand, not their whole hotbar
    private int handSlot = 0; // 0-7
    ItemStack[] hotbar = new ItemStack[8]; // 0-7
    // A player only knows about his own inventory
    ItemStack[] inventory = new ItemStack[24]; // 3 * 8§

    public Entity(int entityType, long id, Location location){
        this.entityType = entityType;
        this.id = id;
        this.location = location;
    }

    public static Entity load(File file){
        try {
            if(!file.exists()){
                return null;
            }
            Entity entity = new Entity(0, Long.parseLong(file.getName()), null);
            Location location = new Location(0, 0, 0, 0);
            Vector velocity = new Vector(0, 0);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            for(int x = 0; x < 10 && (line = reader.readLine()) != null; x++){
                switch(x){
                    case 0:
                        entity.setDisplayName(line);
                        break;
                    case 1:
                        entity.entityType = Integer.parseInt(line);
                        break;
                    case 2:
                        location.setWorld(Integer.parseInt(line));
                        break;
                    case 3:
                        location.setX(Double.parseDouble(line));
                        break;
                    case 4:
                        location.setY(Double.parseDouble(line));
                        break;
                    case 5:
                        location.setRotation(Float.parseFloat(line));
                        break;
                    case 6:
                        velocity.setX(Double.parseDouble(line));
                        break;
                    case 7:
                        velocity.setY(Double.parseDouble(line));
                        break;
                    case 8:
                        entity.parseTags(line);
                        break;
                    case 9:
                        entity.handSlot = Integer.parseInt(line);
                        break;
                }
            }
            entity.setLocation(location);
            entity.setVelocity(velocity);
            ItemStack stack = null;
            int slot = 0;
            for(int i = 0; (line = reader.readLine()) != null; i++){
                switch(i % 6){
                    case 0:
                        slot = Integer.parseInt(line);
                        break;
                    case 1:
                        stack = new ItemStack(Integer.parseInt(line));
                        break;
                    case 2:
                        stack.setAmount(Integer.parseInt(line));
                        break;
                    case 3:
                        stack.setItemName(line);
                        break;
                    case 4:
                        stack.setLore(line);
                        break;
                    case 5:
                        stack.parseAttributes(line);
                        if(slot < 0)
                            entity.hotbar[-slot-1] = stack;
                        else
                            entity.inventory[slot] = stack;
                        break;
                }
            }
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(File file){
        try {
            PrintStream out = new PrintStream(file);
            out.println(displayName);
            out.println(entityType);
            out.println(location.getWorld());
            out.println(location.getX());
            out.println(location.getY());
            out.println(location.getRotation());
            out.println(velocity.getX());
            out.println(velocity.getY());
            out.println(serializeTags());
            out.println(handSlot);
            for(int i = 0; i < 9; i++){
                ItemStack x = hotbar[i];
                if(x == null)
                    continue;
                out.println(-i-1);
                out.println(x.getItemType());
                out.println(x.getAmount());
                out.println(x.getItemName());
                out.println(x.getLore());
                out.println(x.serializeAttributes());
            }

            for(int i = 0; i < 25; i++){
                ItemStack x = inventory[i];
                if(x == null)
                    continue;
                out.println(i);
                out.println(x.getItemType());
                out.println(x.getAmount());
                out.println(x.getItemName());
                out.println(x.getLore());
                out.println(x.serializeAttributes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public int getEntityType(){
        return entityType;
    }

    public long getId(){
        return id;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Vector getVelocity(){
        return velocity;
    }

    public void setVelocity(Vector velocity){
        this.velocity = velocity;
    }

    public int getHandSlot(){
        return handSlot;
    }

    public void setHandSlot(int handSlot){
        this.handSlot = handSlot;
    }

    public String getTag(String key){
        return tags.get(key.replaceAll(split, ""));
    }

    public void setTag(String key, String value){
        tags.put(key.replaceAll(split, "").replaceAll("\n", ""),
                value.replaceAll(split, "").replaceAll("\n", ""));
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public String serializeTags(){
        StringBuilder ret = new StringBuilder();
        for(String key : tags.keySet())
            ret.append(key).append(split).append(tags.get(key)).append(split);
        return ret.toString();
    }

    public void parseTags(String tags){
        String[] arr = tags.split(split);
        this.tags.clear();
        for(int i = 0; i < arr.length-1; i+=2){
            this.tags.put(arr[i], arr[i+1]);
        }
    }
}

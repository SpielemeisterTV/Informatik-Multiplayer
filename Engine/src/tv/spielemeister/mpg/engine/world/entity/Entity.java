package tv.spielemeister.mpg.engine.world.entity;

import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.Vector;

import java.io.*;
import java.util.HashMap;

public class Entity {

    private HashMap<String, String> tags = new HashMap<>();

    private String displayName = "";
    private int entityType;
    private long id;

    private Location location;
    private Vector velocity;

    // A player can only see what other Entities have in their hand, not their whole hotbar
    private int handSlot = 0; // 0-7
    ItemStack[] hotbar = new ItemStack[8]; // 0-7
    ItemStack[] inventory = new ItemStack[24]; // 3 * 8

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
            Entity entity = new Entity(0, Long.valueOf(file.getName()), null);
            Location location = new Location(0, 0, 0, 0);
            Vector velocity = new Vector(0, 0);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            for(int x = 0; x < 9 && (line = reader.readLine()) != null; x++){
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
                        entity.handSlot = Integer.parseInt(line);
                        break;
                }
            }
            entity.setLocation(location);
            entity.setVelocity(velocity);
            for(int x = 0; x < 9)
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
            out.println(handSlot);
            for(int i = 0; i < 9; i++){
                ItemStack x = hotbar[i];
                if(x == null){
                    continue;
                }
                out.println(i);
                out.println(x.getItemType());
                out.println(x.getAmount());
                out.println(x.getItemName());
                out.println(x.getLore());
            }
            // TODO: Add Inventory & hotbar

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
        return tags.get(key);
    }

    public void setTag(String key, String value){
        tags.put(key, value);
    }

}

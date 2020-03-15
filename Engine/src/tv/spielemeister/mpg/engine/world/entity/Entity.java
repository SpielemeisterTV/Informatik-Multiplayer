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

    private int handSlot = 0; // 0-7



    public Entity(int entityType, long id, Location location){
        this.entityType = entityType;
        this.id = id;
        this.location = location;
    }

    public static Entity load(File file){
        try {
            FileReader reader = new FileReader(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(File file){
        try {
            PrintStream out = new PrintStream(file);
            out.println(displayName);
            out.println(entityType);
            out.println(id);
            out.println(location.getWorld());
            out.println(location.getX());
            out.println(location.getY());
            out.println(location.getRotation());
            out.println(velocity.getX());
            out.println(velocity.getY());
            out.println(handSlot);

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

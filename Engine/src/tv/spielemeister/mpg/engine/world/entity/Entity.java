package tv.spielemeister.mpg.engine.world.entity;

import tv.spielemeister.mpg.engine.world.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class Entity {

    private final static String linesplit = "§§", split = "§";

    private HashMap<String, String> tags = new HashMap<>();

    private String displayName = "";
    private int entityType;
    private final long id;

    private Location location;

    // A player can only see what other Entities have in their hand, not their whole hotbar
    private int handSlot = 0; // 0-7
    ItemStack[] hotbar = new ItemStack[8]; // 0-7
    // A player only knows about his own inventory
    ItemStack[] inventory = new ItemStack[24]; // 3 * 8

    public Entity(int entityType, long id, Location location){
        this.entityType = entityType;
        this.id = id;
        this.location = location;
    }

    public static Entity load(String entityString){
        String[] entityData = entityString.split(linesplit);
        Location location = new Location(0, 0, 0, 0);

        Entity entity = new Entity(0, Long.parseLong(entityData[0]), null);
        entity.setDisplayName(entityData[1]);
        entity.entityType = Integer.parseInt(entityData[2]);
        location.setWorld(Integer.parseInt(entityData[3]));
        location.setX(Integer.parseInt(entityData[4]));
        location.setY(Integer.parseInt(entityData[5]));
        location.setRotation(Float.parseFloat(entityData[6]));
        entity.parseTags(entityData[7]);
        entity.handSlot = Integer.parseInt(entityData[8]);

        entity.setLocation(location);

        for(int i = 0; i < (entityData.length-9)/6; i++){
            int index = (entityData.length) + (i * 6);
            int slot = Integer.parseInt(entityData[index++]);
            ItemStack itemStack = new ItemStack(Integer.parseInt(entityData[index++]));
            itemStack.setAmount(Integer.parseInt(entityData[index++]));
            itemStack.setItemName(entityData[index++]);
            itemStack.setLore(entityData[index++]);
            itemStack.parseAttributes(entityData[index]);
            if(slot < 0)
                entity.hotbar[-slot-1] = itemStack;
            else
                entity.inventory[slot] = itemStack;
        }
        return entity;
    }

    public String save(){
        StringBuilder builder = new StringBuilder();
        builder.append(displayName).append(linesplit);
        builder.append(entityType).append(linesplit);
        builder.append(location.getWorld()).append(linesplit);
        builder.append(location.getX()).append(linesplit);
        builder.append(location.getY()).append(linesplit);
        builder.append(location.getRotation()).append(linesplit);
        builder.append(serializeTags()).append(linesplit);
        builder.append(handSlot);
        for(int i = 0; i < 9; i++){
            ItemStack x = hotbar[i];
            if(x == null)
                continue;
            builder.append(linesplit).append(-i-1).append(linesplit);
            builder.append(x.getItemType()).append(linesplit);
            builder.append(x.getAmount()).append(linesplit);
            builder.append(x.getItemName()).append(linesplit);
            builder.append(x.getLore()).append(linesplit);
            builder.append(x.serializeAttributes());
        }

        for(int i = 0; i < 25; i++){
            ItemStack x = inventory[i];
            if(x == null)
                continue;
            builder.append(linesplit).append(i).append(linesplit);
            builder.append(x.getItemType()).append(linesplit);
            builder.append(x.getAmount()).append(linesplit);
            builder.append(x.getItemName()).append(linesplit);
            builder.append(x.getLore()).append(linesplit);
            builder.append(x.serializeAttributes());
        }
        return builder.toString();
    }

    public static Entity load(File file){
        try {
            return load(Files.readString(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(File file){
        try {
            Files.writeString(file.toPath(), save(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
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
        if(key != null && !key.equals("") && value != null && !value.equals("")
                && !key.contains(split) && ! value.contains(split))
            tags.put(key, value);
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

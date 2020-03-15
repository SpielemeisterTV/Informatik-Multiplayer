package tv.spielemeister.mpg.engine.world.entity;

import java.util.HashMap;

public class ItemStack {

    private int itemType, amount;
    private HashMap<Byte, Byte> attributes = new HashMap<>(8);

    public ItemStack(int type){
        this.itemType = type;
    }

    public HashMap<Byte, Byte> getAttributes(){
        return attributes;
    }

    public void removeAttribute(byte key){
        attributes.remove(key);
    }

    public void setAttribute(byte key, byte value){
        attributes.put(key, value);
    }

}

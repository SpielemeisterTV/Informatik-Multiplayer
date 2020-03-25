package tv.spielemeister.mpg.engine.world.entity;

import java.util.HashMap;

import static tv.spielemeister.mpg.engine.Global.split;

public class ItemStack {

    private int itemType, amount = 1;

    private String itemName = null, lore = null;

    private HashMap<Byte, Byte> attributes = new HashMap<>();

    public ItemStack(int type){
        this.itemType = type;
    }

    public int getItemType(){
        return itemType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public HashMap<Byte, Byte> getAttributes(){
        return attributes;
    }

    public void setAttribute(byte attr, byte val){
        attributes.put(attr, val);
    }

    public void unsetAttribute(byte attr) {
        attributes.remove(attr);
    }

    public String serializeAttributes(){
        StringBuilder ret = new StringBuilder();
        for(byte key : attributes.keySet())
            ret.append(key).append(split).append(attributes.get(key)).append(split);
        return ret.toString();
    }

    public void parseAttributes(String attributes){
        String[] arr = attributes.split(split);
        this.attributes.clear();
        for(int i = 0; i < arr.length-1; i+=2){
            this.attributes.put(Byte.parseByte(arr[i]), Byte.parseByte(arr[i+1]));
        }
    }
}

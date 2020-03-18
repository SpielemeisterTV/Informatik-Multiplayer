package tv.spielemeister.mpg.engine.world.entity;

import java.util.HashMap;

public class ItemStack {

    private int itemType, amount = 1;

    private String itemName = null, lore = null;

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
}

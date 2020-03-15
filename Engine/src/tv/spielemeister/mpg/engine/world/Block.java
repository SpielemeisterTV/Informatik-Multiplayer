package tv.spielemeister.mpg.engine.world;

import java.util.ArrayList;

public class Block {

    /*
        Array-index: xxxxyyyyzz

        Block Data(Bits):
        4   (data value)
        2   (tile orientation)
        10  (tile id)
     */
    char[] blockData = new char[1024]; //4b: data value (power, state, color etc.), 2b: orientation, 10b: tile id
    int blockX, blockY;

    public char getTile(int x, int y, int z){ // x: 0-15, y: 0-15, z: 0-3
        return blockData[(x << 6) | (y << 2) | z];
    }

    public void setTile(int x, int y, int z, char tileData){ // x: 0-15, y: 0-15, z: 0-3
        blockData[(x << 6) | (y << 2) | z] = tileData;
    }

    public char[] getBlockData(){
        return blockData;
    }

    public boolean setBlockData(char[] blockData){
        if(blockData.length == 1024){
            this.blockData = blockData;
            return true;
        }
        return false;
    }

    public Block(int x, int y){
        this.blockX = x;
        this.blockY = y;
    }

    public int getX(){
        return blockX;
    }

    public int getY(){
        return blockY;
    }

}

package tv.spielemeister.mpg.engine.world;

public class Block {

    char[] blockData = new char[1024]; //4b: data value (power, state, color etc.), 2b: orientation, 10b: tile id

    public char getTile(int x, int y, int z){ // x: 0-15, y: 0-15, z: 0-3
        return blockData[(x << 6) + (y << 4) + z];
    }

    public void setTile(int x, int y, int z, char tileData){ // x: 0-15, y: 0-15, z: 0-3
        blockData[(x << 6) + (y << 4) + z] = tileData;
    }

    public boolean setBlockData(char[] blockData){
        if(blockData.length == 1024){
            this.blockData = blockData;
            return true;
        }
        return false;
    }

}

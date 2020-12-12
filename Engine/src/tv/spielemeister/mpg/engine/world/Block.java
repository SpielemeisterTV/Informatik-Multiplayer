package tv.spielemeister.mpg.engine.world;

public class Block {

    /*
        Array-index: zzxxxxyyyy

        Block Data(Bits):
        5   (data value)
        2   (tile orientation)
        9  (tile type)
     */
    char[] blockData = new char[1024]; //5b: data value (power, state, color etc.), 2b: orientation, 9b: tile type
    int blockX, blockY;

    public char getTile(int x, int y, int z){ // z: 0-3, x: 0-15, y: 0-15
        return blockData[(z << 8) | (x << 4) | y];
    }

    public void setTile(int x, int y, int z, char tileData){ // x: 0-15, y: 0-15, z: 0-3
        blockData[(z << 8) | (x << 4) | y] = tileData;
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

    public static int getType(char tile){
        return tile & 0b111111111;
    }

    public static int getOrientation(char tile){
        return (tile >>> 9) & 0b11;
    }

    public static int getData(char tile){
        return (tile >>> 11);
    }

}

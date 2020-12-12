package tv.spielemeister.mpg.engine.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class World { // 2**16 * 2**16 Blocks (65’536‬**2 = 4’294’967’296‬)

    protected ConcurrentHashMap<Integer, Block> blockCache = new ConcurrentHashMap<>(32);

    protected HashMap<Integer, Block> queue = new HashMap<>();

    public void loadBlock(Block block) {
        blockCache.put(block.getX() << 16 | block.getY(), block);
    }

    public Iterator<Block> getBlockIterator(){
        return blockCache.values().iterator();
    }

    public void loadBlock(int x, int y, char[] blockData) {
        Block block = new Block(x, y);
        if (block.setBlockData(blockData))
            blockCache.put(x << 16 | y, block);
    }

    public Block unloadBlock(Block block){
        return blockCache.remove(block.getX() << 16 | block.getY());
    }

    public Block unloadBlock(int x, int y){
        return blockCache.remove(x << 16 | y);
    }

    public boolean isLoaded(Block block){
        return blockCache.containsKey(block.getX() << 16 | block.getY());
    }

    public boolean isLoaded(int x, int y){
        return blockCache.containsKey(x << 16 | y);
    }

    public Block getBlock(int x, int y){
        return blockCache.getOrDefault(x << 16 | y, null);
    }
}
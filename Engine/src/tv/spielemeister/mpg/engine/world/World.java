package tv.spielemeister.mpg.engine.world;

import java.util.ArrayList;

public class World { // 2**16 * 2**16 Blocks (4’294’967’296‬)

    ArrayList<Block> blockCache = new ArrayList<>();

    public void loadBlock(char[] blockData) {
        Block block = new Block();
        if(block.setBlockData(blockData))
            blockCache.add(block);
    }

    public void loadBlock(Block block) {
        blockCache.add(block);
    }

    public void unloadBlock(Block block){
        if(blockCache.contains(block))
            blockCache.remove(block);
    }
}
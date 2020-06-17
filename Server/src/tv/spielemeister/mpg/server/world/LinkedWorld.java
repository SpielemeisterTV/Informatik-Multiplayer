package tv.spielemeister.mpg.server.world;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.engine.world.Block;
import tv.spielemeister.mpg.engine.world.World;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

class LinkedWorld extends World {

    private File worldDir;

    private Config config;

    private File dataDir;

    private WorldGenerator generator;

    private int id;

    public LinkedWorld(File worldDir, int id){
        this.worldDir = worldDir;
        this.id  = id;
        initConfig();
    }

    private void initConfig(){
        Properties configDefaults = new Properties();

        // Generate random seed for world generation
        Random random = new Random();
        int seed = random.nextInt();

        configDefaults.putAll(Map.of("seed", String.valueOf(seed)));
        config = new Config(new File(worldDir, "world.conf"), configDefaults);
        dataDir = new File(worldDir, "data");
        if(!dataDir.exists())
            dataDir.mkdirs();
        if(!dataDir.isDirectory()){
            System.err.println("World data directory is not a directory!");
            System.exit(0);
        }
    }

    public boolean load(int x, int y) {
        try{
            File regionDir = new File(dataDir, getRegionDirId(x, y)
                    + "/" + getRegionFileId(x, y));
            if(!regionDir.exists()) {
                regionDir.mkdirs();
            }
            File regionFile = new File(regionDir, String.valueOf(getRegionId(x, y)));
            if(!regionFile.exists()) {
                Block block = generator.generate(x, y);
                super.loadBlock(block);
                return true;
            }
            FileReader reader = new FileReader(regionFile);

            char[] data = new char[1024];
            reader.read(data);

            Block block = new Block(x, y);
            block.setBlockData(data);
            super.loadBlock(block);
            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean save(){
        try {
            for (Block block : this.blockCache.values()) {
                File regionDir = new File(dataDir, getRegionDirId(block.getX(), block.getY())
                        + "/" + getRegionFileId(block.getX(), block.getY()));
                if(!regionDir.exists()) {
                    regionDir.mkdirs();
                }
                File regionFile = new File(regionDir, String.valueOf(getRegionId(block.getX(), block.getY())));
                if(!regionFile.exists())
                    regionFile.createNewFile();
                PrintStream out = new PrintStream(regionFile);
                out.print(block.getBlockData());
            }
            return true;
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
            return false;
        }
    }

    public int getWorldID(){
        return id;
    }

    public boolean init(){
        return true;
    }

    public static int getRegionDirId(int x, int y){
        return ((x >> 8) & 0xf0) | (y >> 12);
    }

    public static int getRegionFileId(int x, int y){
        return ((x >> 4) & 0xf0) | ((y >> 8) & 0xf);
    }

    public static int getRegionId(int x, int y){
        return ((x & 0xff) << 8) | (y & 0xff);
    }

}

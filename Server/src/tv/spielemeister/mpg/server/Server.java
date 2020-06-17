package tv.spielemeister.mpg.server;

import tv.spielemeister.mpg.engine.config.Config;
import tv.spielemeister.mpg.engine.net.packets.PacketEntityTeleport;
import tv.spielemeister.mpg.engine.net.packets.PacketHandshakeRequest;
import tv.spielemeister.mpg.engine.net.packets.PacketByteInformation;
import tv.spielemeister.mpg.engine.net.packets.PacketWorldBlock;
import tv.spielemeister.mpg.engine.world.Block;
import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.entity.Entity;
import tv.spielemeister.mpg.server.net.GameServerSocket;
import tv.spielemeister.mpg.server.net.ServerSocketHandler;
import tv.spielemeister.mpg.server.world.WorldManager;

import java.io.*;
import java.util.*;

public class Server {

    private static Server instance;
    private GameServerSocket serverSocket;

    private Config config;

    private File worldsDirectory, entityDirectory, globalData;

    private WorldManager worldManager;

    private HashMap<Entity, File> loadedEntities = new HashMap<>(); // Entity & Last file (for saving)

    public static void main(String[] args){
        new Server();
    }

    // Global data
    private long entityCount = 0; // At position 0 in global data file
    private HashMap<String, String> loginData;

    public Server(){
        instance = this;
        initConfig();
        setupFiles();
        initData();
        worldManager = new WorldManager(worldsDirectory, config.getProperty("Overworld world name"));
        serverSocket = new GameServerSocket(12345, (handler, netPacket) -> {
            ServerSocketHandler socket = (ServerSocketHandler) handler;

            switch(netPacket.packetType){
                case 0: { // Handshake
                        PacketHandshakeRequest packet = (PacketHandshakeRequest) netPacket;
                        String prove = packet.username + packet.password;
                        if(prove.contains("=") || prove.contains("\n") || prove.contains(" ")
                                || packet.password.equals("") || packet.username.equals("")){
                            socket.send(new PacketByteInformation((byte) 2));
                        } else
                        if (!socket.loggedIn) {
                            String password = loginData.get(packet.username);
                            if (password == null) {
                                socket.send(new PacketByteInformation((byte) 1));
                                socket.password = packet.password;
                                socket.username = packet.username;
                            } else {
                                if (password.equals(packet.password)) {
                                    socket.loggedIn = true;

                                    for (int x = 0; x < 20; x++)
                                        for (int y = 0; y < 50; y++) {
                                            Block block = new Block(x, y);

                                            PacketWorldBlock blockPacket = new PacketWorldBlock(block);
                                            socket.send(blockPacket);
                                        }

                                } else {
                                    socket.send(new PacketByteInformation((byte) 0));
                                    socket.shutdown();
                                }
                            }
                        }
                    } break;
                case 1: { // Handle simple information
                    PacketByteInformation packet = (PacketByteInformation) netPacket;
                    switch(packet.data){
                        case 0:
                            if(socket.username != null && socket.password != null) {
                                if (loginData.containsKey(socket.username)) {
                                    socket.send(new PacketByteInformation((byte) 2));
                                } else {
                                    System.out.println("Created new account!");
                                    loginData.put(socket.username, socket.password);
                                    writeGlobalDataFile();
                                }
                            }
                            break;
                    }
                } break;
                case 5: {
                    PacketEntityTeleport packet = (PacketEntityTeleport) netPacket;
                    // TODO: Check if teleport is possible
                    serverSocket.sendAll(packet);
                } break;
            }
        });

    }

    private void initConfig(){
        Properties configDefaults = new Properties();
        configDefaults.putAll(Map.ofEntries(
                Map.entry("Worlds directory", "worlds"),
                Map.entry("Overworld world name", "Overworld")
        ));
        config = new Config(new File("server.config"), configDefaults);
    }

    private void initData(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(globalData));
            loginData = new HashMap<>();
            String line;
            for(int i = 0; (line = reader.readLine()) != null; i++){
                switch (i){
                    case 0:
                        entityCount = Long.parseLong(line);
                        break;
                    default:
                        String[] data = line.split("=");
                        loginData.put(data[0], data[1]);
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getNewEntityID(){
        entityCount++;
        writeGlobalDataFile();
        return entityCount-1;
    }

    private void writeGlobalDataFile(){
        try {
            PrintStream stream = new PrintStream(globalData);
            stream.println(entityCount);
            for(String username : loginData.keySet()){
                stream.println(username+"="+loginData.get(username));
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupFiles(){
        worldsDirectory = new File(config.getProperty("Worlds directory"));
        if(!worldsDirectory.exists())
            if(!worldsDirectory.mkdirs()) {
                System.err.println("Worlds directory could not be created!");
                System.exit(0);
            }
        if(!worldsDirectory.isDirectory()){
            System.err.println("Worlds directory is not a directory!");
            System.exit(0);
        }
        try{
            globalData = new File(worldsDirectory, "global_data.txt");
            if(!globalData.exists())
                globalData.createNewFile();
            entityDirectory = new File(worldsDirectory, "entity_data");
            if(!entityDirectory.exists())
                entityDirectory.mkdir();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean saveAll(){
        for(Entity entity : loadedEntities.keySet())
            saveEntity(entity);
        return worldManager.saveAll();
    }

    private void loadEntities(int world, int blockX, int blockY){
        String dir = world + "/" + blockX + "/" + blockY;
        File directory = new File(entityDirectory, dir);
        if(directory.exists()){
            for(File entityFile : Objects.requireNonNull(directory.listFiles())){
                Entity entity = Entity.load(entityFile);
                if(entity != null)
                    loadedEntities.put(entity, entityFile);
            }
        }
    }

    private void saveEntity(Entity entity){
        File last = loadedEntities.get(entity);
        last.delete();
        Location loc = entity.getLocation();
        File newFile = new File(entityDirectory, loc.getWorld() + "/" +
                loc.getBlockX() + "/" + loc.getBlockY() + "/" + entity.getId());
        entity.save(newFile);
        loadedEntities.put(entity, newFile);
    }

    public static Server getInstance(){
        return instance;
    }
}
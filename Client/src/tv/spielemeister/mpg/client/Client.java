package tv.spielemeister.mpg.client;

import tv.spielemeister.mpg.client.graphics.GameWindow;
import tv.spielemeister.mpg.client.net.GameSocket;
import tv.spielemeister.mpg.engine.net.packets.*;
import tv.spielemeister.mpg.engine.world.World;
import tv.spielemeister.mpg.engine.world.entity.Entity;

import java.awt.*;
import java.util.HashMap;

public class Client {

    private static final int maxDistance = 40;

    private static GameWindow gameWindow;
    private static GameSocket socket;

    public static World world = new World(); // Client only stores one world, doesn't care about others
    public static HashMap<Long, Entity> entities = new HashMap<>();
    public static Entity controlledEntity = null; // The currently controlled entity. Also contained in entities map

    public static void main(String[] args){
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        gameWindow = new GameWindow(bounds);

        // Packet handler
        socket = new GameSocket("localhost", 12345, (socket, netPacket) -> {
            switch(netPacket.packetType){
                case 1: { // Handle simple information
                    PacketByteInformation packet = (PacketByteInformation) netPacket;
                    switch(packet.data){
                        case 0:
                            System.out.println("Wrong password");
                            // TODO: Wrong password
                            break;
                        case 1:
                            System.out.println("Create new account?");
                            // TODO: Ask to create new account, send 0 if so
                            socket.send(new PacketByteInformation((byte) 0));
                            break;
                        case 2:
                            System.out.println("Invalid username/password");
                            // TODO: Not accepted username / password
                            break;
                    }
                } break;
                case 2: { // Handle Blocks
                    PacketWorldBlock packet = (PacketWorldBlock) netPacket;
                    world.loadBlock(packet.block);
                } break;
                case 3: { // Handle Entity updates
                    PacketEntity packet = (PacketEntity) netPacket;
                    entities.put(packet.entity.getId(), packet.entity);
                } break;
                case 4: { // Handle files

                } break;
                case 5: { // Teleport entity
                    PacketEntityTeleport packet = (PacketEntityTeleport) netPacket;
                    if(entities.containsKey(packet.entityId)){
                        Entity entity = entities.get(packet.entityId);
                        entity.setLocation(packet.newLoc);
                        requestEntityUnload(entity);
                    }
                } break;
                case 6: { // Load textures
                    PacketTileInformation packet = (PacketTileInformation) netPacket;
                    GameWindow.textures[packet.id] = packet.getImage();
                } break;
            }
        });


        // TODO: Input << Username & Password
        socket.socketHandler.send(new PacketHandshakeRequest("Name", "ey_dude"));

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void requestEntityUnload(Entity entity){ // Unloads an entity if it is too far away
        if(entities.containsKey(entity.getId()))
        if(controlledEntity == null
                || controlledEntity.getLocation().distance(entity.getLocation()) >= maxDistance){
            entities.remove(entity.getId());
        }
    }

}
package tv.spielemeister.mpg.client;

import tv.spielemeister.mpg.client.graphics.GameWindow;
import tv.spielemeister.mpg.client.net.GameSocket;
import tv.spielemeister.mpg.engine.net.packets.PacketByteInformation;
import tv.spielemeister.mpg.engine.net.packets.PacketEntityUpdate;
import tv.spielemeister.mpg.engine.net.packets.PacketHandshakeRequest;
import tv.spielemeister.mpg.engine.net.packets.PacketWorldBlock;
import tv.spielemeister.mpg.engine.world.World;
import tv.spielemeister.mpg.engine.world.entity.Entity;

import java.awt.*;
import java.util.HashMap;

public class Client {

    private static GameWindow gameWindow;
    private static GameSocket socket;

    public static World world = new World(); // Client only has one world, doesn't care about others
    public static HashMap<Long, Entity> entities = new HashMap<>();

    public static void main(String[] args){
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        gameWindow = new GameWindow(bounds);

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
                            socket.send(new PacketByteInformation((byte) 0));
                            // TODO: Ask to create new account, send 0 if so
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
                    PacketEntityUpdate packet = (PacketEntityUpdate) netPacket;
                    entities.put(packet.entity.getId(), packet.entity);
                } break;
            }
        });

        socket.socketHandler.send(new PacketHandshakeRequest("Name", "ey_dude"));


    }

}
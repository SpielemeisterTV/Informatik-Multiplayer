package tv.spielemeister.mpg.client;

import tv.spielemeister.mpg.client.graphics.GameWindow;
import tv.spielemeister.mpg.client.net.GameSocket;
import tv.spielemeister.mpg.engine.net.packets.PacketByteInformation;
import tv.spielemeister.mpg.engine.net.packets.PacketHandshakeRequest;

import java.awt.*;

public class Client {

    private static GameWindow gameWindow;
    private static GameSocket socket;

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
                            // TODO: Wrong password
                            break;
                        case 1:
                            System.out.println("Create new acc?");
                            // TODO: Ask to create new account, send 0 if so
                            break;
                        case 2:
                            // TODO: Not accepted username, password
                            break;
                    }
                } break;
            }
        });

        socket.socketHandler.send(new PacketHandshakeRequest("Name", "ey dude"));
    }

}
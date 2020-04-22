package tv.spielemeister.mpg.client.net;

import tv.spielemeister.mpg.engine.net.PacketHandler;
import tv.spielemeister.mpg.engine.net.SocketHandler;

import java.io.IOException;
import java.net.Socket;

public class GameSocket {

    private Socket socket;
    public SocketHandler socketHandler;

    public GameSocket(String host, int port, PacketHandler handler){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        socketHandler = new SocketHandler(socket, handler);
    }

}

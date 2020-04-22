package tv.spielemeister.mpg.server.net;

import tv.spielemeister.mpg.engine.net.PacketHandler;
import tv.spielemeister.mpg.engine.net.SocketHandler;

import java.net.Socket;

public class ServerSocketHandler extends SocketHandler {

    public long entityID = -1;
    public boolean loggedIn = false;

    public String username = null, password = null;

    public ServerSocketHandler(Socket socket, PacketHandler handler) {
        super(socket, handler);
    }

}

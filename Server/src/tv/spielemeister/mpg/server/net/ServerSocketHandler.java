package tv.spielemeister.mpg.server.net;

import tv.spielemeister.mpg.engine.net.PacketHandler;
import tv.spielemeister.mpg.engine.net.SocketHandler;

import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketHandler extends SocketHandler {

    public static ArrayList<ServerSocketHandler> handlers = new ArrayList<>();

    public long entityID = -1;
    public boolean loggedIn = false;

    public String username = null, password = null;

    public ServerSocketHandler(Socket socket, PacketHandler handler) {
        super(socket, handler);
        handlers.add(this);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if(handlers.contains(this))
            handlers.remove(this);
    }
}

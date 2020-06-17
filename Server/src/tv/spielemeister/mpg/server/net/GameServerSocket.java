package tv.spielemeister.mpg.server.net;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.net.PacketHandler;
import tv.spielemeister.mpg.engine.net.SocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServerSocket implements Runnable {

    private ServerSocket serverSocket;
    private PacketHandler packetHandler;

    public GameServerSocket(int port, PacketHandler packetHandler){
        this.packetHandler = packetHandler;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Thread listeningThread = new Thread(this);
        listeningThread.setDaemon(true);
        listeningThread.start();
        try {
            listeningThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket socket = serverSocket.accept();
                new ServerSocketHandler(socket, packetHandler);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendAll(NetPacket packet){
        for(SocketHandler handler : ServerSocketHandler.handlers)
            handler.send(packet);
    }
}

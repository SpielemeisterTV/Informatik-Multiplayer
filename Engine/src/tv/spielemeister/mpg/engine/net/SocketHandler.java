package tv.spielemeister.mpg.engine.net;

import tv.spielemeister.mpg.engine.net.packets.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class SocketHandler implements Runnable{

    private static final Class<? extends NetPacket>[] packetIndex = new Class[]{
            PacketHandshakeRequest.class,
            PacketByteInformation.class,
            PacketWorldBlock.class,
            PacketEntity.class,
            PacketFile.class,
            PacketEntityTeleport.class,
            PacketTileInformation.class,
            PacketChatMessage.class
    };

    public Socket socket;
    private PacketHandler handler;

    private BufferedInputStream inputStream = null;
    private BufferedOutputStream outputStream = null;

    private Thread listeningThread = null;

    private boolean running = false;

    public SocketHandler(Socket socket, PacketHandler handler){
        this.socket = socket;
        this.handler = handler;
        this.launch();
    }

    @Override
    public void run(){
        while(true){
            try{
                byte[] len = inputStream.readNBytes(4);
                int length = NetPacket.getInteger(len, 0);

                System.out.println("["+socket.getInetAddress().toString()+"] > " + length+"b");

                byte[] data = inputStream.readNBytes(length);
                NetPacket packet = packetIndex[data[0]].getDeclaredConstructor(byte[].class).newInstance(data);
                handler.handle(this, packet);
            }catch (Exception e){
                // e.printStackTrace();
                System.out.println("["+socket.getInetAddress().toString()+"] Connection lost.");
                break;
            }
        }
    }

    public void send(NetPacket packet){
        if(socket.isClosed()){
            shutdown();
        } else
        if(running){
            try {
                byte[] data = packet.encode();
                byte[] len = new byte[4];
                NetPacket.put(len, data.length, 0);
                outputStream.write(len);
                outputStream.write(data);
                outputStream.flush();
                System.out.println("["+socket.getInetAddress().toString()+"] < " + data.length + "b, type="+packet.packetType);
            } catch (IOException e) {
                if(e instanceof SocketException)
                    shutdown();
                else
                    e.printStackTrace();
            }
        }
    }

    private void listen(){
        if(listeningThread==null){
            listeningThread = new Thread(this);
            listeningThread.setDaemon(true);
            listeningThread.start();
        }
    }

    public void launch(){
        if(!running && !socket.isClosed() && socket.isConnected()){
            try {
                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                listen();
                running = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        if(running){
            try {
                running = false;
                if(listeningThread.isAlive())
                    listeningThread.interrupt();
                listeningThread = null;
                socket.close();
                inputStream = null;
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning(){
        return this.running;
    }

}

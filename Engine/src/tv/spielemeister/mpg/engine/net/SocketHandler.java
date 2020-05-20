package tv.spielemeister.mpg.engine.net;

import tv.spielemeister.mpg.engine.net.packets.PacketEntityUpdate;
import tv.spielemeister.mpg.engine.net.packets.PacketHandshakeRequest;
import tv.spielemeister.mpg.engine.net.packets.PacketByteInformation;
import tv.spielemeister.mpg.engine.net.packets.PacketWorldBlock;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class SocketHandler implements Runnable{

    private static final Class<? extends NetPacket>[] packetIndex = new Class[]{
            PacketHandshakeRequest.class,
            PacketByteInformation.class,
            PacketWorldBlock.class,
            PacketEntityUpdate.class
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
                int length = inputStream.readNBytes(1)[0];

                if((length & 0b10000000) == 0b10000000){ // Large packets
                    byte[] len = inputStream.readNBytes(3);
                    length = ((length & 0b01111111) << 24) | (len[0] << 16) | (len[1] << 8) | (len[2]);
                }

                System.out.println("["+socket.getInetAddress().toString()+"] < " + length);

                byte[] data = inputStream.readNBytes(length);
                if(data.length > 1) {
                    NetPacket packet = packetIndex[data[0]].getDeclaredConstructor(byte[].class).newInstance(data);
                    handler.handle(this, packet);
                }
            }catch (Exception e){
                System.out.println("["+socket.getInetAddress().toString()+"] Connection lost.");
                break;
            }
        }
    }

    public boolean send(NetPacket packet){
        if(socket.isClosed()){
            shutdown();
        } else
        if(running){
            try {
                byte[] data = packet.encode();
                int flagMask = 0b10000000;
                if(data.length < 128){ // Small packets
                    outputStream.write(new byte[]{(byte) data.length});
                }else { // Large packets
                    byte[] len = new byte[4];
                    len[0] = (byte) ((data.length >> 24) | flagMask);
                    len[1] = (byte) (data.length >> 16);
                    len[2] = (byte) (data.length >> 8);
                    len[3] = (byte) (data.length);
                    outputStream.write(len);
                }
                outputStream.write(packet.encode());
                outputStream.flush();
                return true;
            } catch (IOException e) {
                if(e instanceof SocketException)
                    shutdown();
                else
                    e.printStackTrace();
            }
        }
        return false;
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

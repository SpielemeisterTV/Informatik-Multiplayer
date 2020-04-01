package tv.spielemeister.mpg.engine.net;

import java.io.*;
import java.net.Socket;

public abstract class DataHandler implements Runnable{

    private static final Class<? extends NetPacket>[] packetIndex = new Class[]{

    };

    private Socket socket;

    private BufferedInputStream inputStream = null;
    private BufferedOutputStream outputStream = null;

    private Thread listeningThread = null;

    private boolean running = false;

    public DataHandler(Socket socket){
        this.socket = socket;
        this.start();
    }

    public abstract void handle(NetPacket packet);

    @Override
    public void run(){
        while(true){
            try{
                byte length = inputStream.readNBytes(1)[0];
                byte[] data = inputStream.readNBytes(length);
                if(data.length > 1) {
                    NetPacket packet = packetIndex[data[0]].getDeclaredConstructor(byte[].class).newInstance(data);
                    handle(packet);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean send(NetPacket packet){
        if(running){
            try {
                outputStream.write(packet.encode());
                outputStream.flush();
            } catch (IOException e) {
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

    public void start(){
        if(!running && !socket.isClosed() && socket.isConnected()){
            try {
                BufferedInputStream
                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                listen();
                running = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        if(running){
            try {
                outputStream.close();
                inputStream.close();
                if(listeningThread.isAlive())
                    listeningThread.interrupt();
                listeningThread = null;
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

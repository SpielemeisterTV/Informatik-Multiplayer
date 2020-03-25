package tv.spielemeister.mpg.engine.net;

public abstract class NetPacket {

    byte packetType;

    public NetPacket(byte[] data){
        packetType = data[0];
        parse(data);
    }

    protected abstract void parse(byte[] data);

    public abstract byte[] encode();

}

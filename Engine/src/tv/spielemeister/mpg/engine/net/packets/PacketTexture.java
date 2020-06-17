package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;

public class PacketTexture extends NetPacket {

    public PacketTexture(){
        super((byte) 4);
    }

    public PacketTexture(byte[] data) {
        super(data);
    }

    @Override
    protected void parse(byte[] data) {

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}

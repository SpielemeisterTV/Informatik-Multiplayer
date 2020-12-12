package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.net.SocketHandler;

public class PacketByteInformation extends NetPacket {

    public byte data;
    /*
        To client:
            0: Wrong password
            1: No acc found, create?
            2: Invalid password/username
        To server:
            0: Create new account
     */

    public PacketByteInformation(byte[] data){
        super(data);
    }

    public PacketByteInformation(byte data){
        super((byte)1);
        this.data = data;
    }

    @Override
    protected void parse(byte[] data) {
        this.data = data[1];
    }

    @Override
    public byte[] encode() {
        return new byte[]{packetType, data};
    }
}

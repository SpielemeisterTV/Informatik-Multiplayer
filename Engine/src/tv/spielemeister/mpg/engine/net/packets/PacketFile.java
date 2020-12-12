package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;

public class PacketFile extends NetPacket {

    String filename;
    byte[] contents = null;

    public PacketFile(byte[] contents, String filename){
        super((byte) 4);
        this.contents = contents;
        this.filename = filename;
    }

    public PacketFile(byte[] data) {
        super(data);
    }

    @Override
    protected void parse(byte[] data) {
        filename = decodeString(get(data, 2, data[1]));
        contents = getRest(data, 2+data[1]);
    }

    @Override
    public byte[] encode() {
        byte[] name = encodeString(filename);
        byte[] ret = new byte[2 + name.length + contents.length];
        ret[0] = packetType;
        ret[1] = (byte)name.length;
        put(ret, name, 2);
        put(ret, contents, 2+name.length);
        return ret;
    }
}

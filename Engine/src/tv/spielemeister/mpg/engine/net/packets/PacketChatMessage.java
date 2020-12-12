package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;

public class PacketChatMessage extends NetPacket {

    String message;
    byte chatId;

    public PacketChatMessage(){
        super((byte) 7);

    }

    public PacketChatMessage(byte[] data) {
        super(data);
    }

    @Override
    protected void parse(byte[] data) {
        chatId = data[1];
        message = decodeString(getRest(data, 2));
    }

    @Override
    public byte[] encode() {
        byte[] text = encodeString(message);
        byte[] data = new byte[text.length + 2];
        data[0] = 7;
        data[1] = chatId;
        put(data, text, 2);
        return data;
    }
}

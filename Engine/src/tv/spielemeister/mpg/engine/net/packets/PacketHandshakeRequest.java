package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;

public class PacketHandshakeRequest extends NetPacket {

    public String username, password;

    public PacketHandshakeRequest(byte[] data){
        super(data);
    }

    public PacketHandshakeRequest(String username, String password){
        super((byte)0);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void parse(byte[] data) {
        byte usernameLength = data[1];
        username = decodeString(get(data, 2, usernameLength));
        password = decodeString(getRest(data, 2 + usernameLength));
    }

    @Override
    public byte[] encode() {
        byte[] usernameBytes = encodeString(username), passwordBytes = encodeString(password);
        byte[] ret = new byte[2 + usernameBytes.length + passwordBytes.length];
        ret[0] = 0;
        ret[1] = (byte) (usernameBytes.length & 0xff);
        put(ret, usernameBytes, 2);
        put(ret, passwordBytes, 2 + usernameBytes.length);
        return ret;
    }

}

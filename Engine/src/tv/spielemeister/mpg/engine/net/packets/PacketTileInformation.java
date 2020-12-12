package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.world.Physics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PacketTileInformation extends NetPacket {

    public int id;

    byte[] texture;
    Physics.PhysicsProperties physicsProperties;

    public PacketTileInformation(InputStream textureStream, Physics.PhysicsProperties properties, int id) {
        super((byte) 6);
        this.id = id;
        this.physicsProperties = properties;
        try {
            texture = textureStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        try {
            return ImageIO.read(new ByteArrayInputStream(texture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PacketTileInformation(byte[] data) {
        super(data);
    }

    @Override
    protected void parse(byte[] data) {
        id = getInteger(data, 1);
        texture = getRest(data, 5);
    }

    @Override
    public byte[] encode() {
        byte[] ret = new byte[5 + texture.length];
        ret[0] = packetType;
        put(ret, id, 1);
        put(ret, texture, 5);
        return ret;
    }
}

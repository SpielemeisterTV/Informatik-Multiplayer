package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.world.entity.Entity;

import java.nio.charset.StandardCharsets;

public class PacketEntity extends NetPacket {

    public Entity entity;

    public PacketEntity(byte[] data) {
        super(data);
    }

    public PacketEntity(Entity entity){
        super((byte) 3);
        this.entity = entity;
    }

    @Override
    protected void parse(byte[] data) {
        entity = Entity.load(new String(getRest(data, 1), StandardCharsets.UTF_8));
    }

    @Override
    public byte[] encode() {
        byte[] data = entity.save().getBytes(StandardCharsets.UTF_8);
        byte[] ret = new byte[data.length+1];
        ret[0] = packetType;
        put(ret, data, 1);
        return ret;
    }

}

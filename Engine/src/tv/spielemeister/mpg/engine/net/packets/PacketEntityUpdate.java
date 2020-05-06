package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.world.entity.Entity;

import java.nio.charset.StandardCharsets;

public class PacketEntityUpdate extends NetPacket {

    public Entity entity;

    public PacketEntityUpdate(byte[] data) {
        super(data);
    }

    public PacketEntityUpdate(Entity entity){
        super((byte) 3);
        this.entity = entity;
    }

    @Override
    protected void parse(byte[] data) {
        entity = Entity.load(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] encode() {
        return entity.save().getBytes(StandardCharsets.UTF_8);
    }

}

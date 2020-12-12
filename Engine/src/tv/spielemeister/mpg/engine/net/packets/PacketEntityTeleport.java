package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.world.Location;

public class PacketEntityTeleport extends NetPacket {

    public long entityId;
    public Location newLoc;

    public PacketEntityTeleport(long entityId, Location newLoc){
        super((byte)5);
        this.entityId = entityId;
        this.newLoc = newLoc;
    }

    public PacketEntityTeleport(byte[] data) {
        super(data);
    }

    @Override
    protected void parse(byte[] data) {
        entityId = getInteger(data, 0);
        newLoc = getLocation(data, 8);
    }

    @Override
    public byte[] encode() {
        byte[] arr = new byte[17];
        arr[0] = packetType;
        put(arr, entityId, 1);
        put(arr, newLoc, 9);
        return arr;
    }
}

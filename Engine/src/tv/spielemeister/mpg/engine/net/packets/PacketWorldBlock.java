package tv.spielemeister.mpg.engine.net.packets;

import tv.spielemeister.mpg.engine.net.NetPacket;
import tv.spielemeister.mpg.engine.world.Block;

public class PacketWorldBlock extends NetPacket {

    public Block block;

    public PacketWorldBlock(byte[] data) {
        super(data);
    }

    public PacketWorldBlock(Block block){
        super((byte) 2);
        this.block = block;
    }

    @Override
    protected void parse(byte[] data) {
        int x = getInteger(data, 1);
        int y = getInteger(data, 5);
        block = new Block(x, y);
        block.setBlockData(getCharArray(data, 9, data.length-9));
    }

    @Override
    public byte[] encode() {
        char[] blockData = block.getBlockData();
        byte[] data = new byte[blockData.length * 2 + 9];
        data[0] = packetType;
        put(data, block.getX(), 1);
        put(data, block.getY(), 5);
        put(data, blockData, 9);
        return new byte[0];
    }
}

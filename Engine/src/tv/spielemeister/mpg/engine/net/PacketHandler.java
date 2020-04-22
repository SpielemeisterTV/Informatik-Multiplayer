package tv.spielemeister.mpg.engine.net;

public interface PacketHandler {

    void handle(SocketHandler socket, NetPacket packet);

}

package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

public class ServerBoundCancelReadyPacket extends ServerBoundPacket {

    public static ServerBoundCancelReadyPacket INSTANCE = new ServerBoundCancelReadyPacket();

    private ServerBoundCancelReadyPacket() {
        // Singleton instance
    }
    
    @Override
    public void decode(ByteBuf buf) {
        // No data to decode for this packet
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

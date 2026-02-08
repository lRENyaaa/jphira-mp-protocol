package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

public class ServerBoundReadyPacket extends ServerBoundPacket {

    public static ServerBoundReadyPacket INSTANCE = new ServerBoundReadyPacket();

    private ServerBoundReadyPacket() {
        // Singleton instance
    }

    @Override
    public void encode(ByteBuf buf) {
        // Do nothing here
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

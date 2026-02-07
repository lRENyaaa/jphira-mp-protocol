package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

public class ServerBoundRequestStartPacket extends ServerBoundPacket {

    public static ServerBoundRequestStartPacket INSTANCE = new ServerBoundRequestStartPacket();

    private ServerBoundRequestStartPacket() {
        // Singleton instance
    }

    @Override
    public void encode(ByteBuf buf) {
        // Do nothing here
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

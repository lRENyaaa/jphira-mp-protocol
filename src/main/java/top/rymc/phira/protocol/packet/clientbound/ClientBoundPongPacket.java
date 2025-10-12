package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.packet.ClientBoundPacket;

public class ClientBoundPongPacket extends ClientBoundPacket {

    public static final ClientBoundPacket INSTANCE = new ClientBoundPongPacket();

    private ClientBoundPongPacket() {
        // Singleton instance
    }

    @Override
    public void encode(ByteBuf buf) {
        // Just pong, do nothing here
    }
}

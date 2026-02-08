package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundChangeHostPacket extends ClientBoundPacket {

    private final boolean isHost;

    public static ClientBoundChangeHostPacket create(boolean isHost) {
        return new ClientBoundChangeHostPacket(isHost);
    }

    public static ClientBoundChangeHostPacket decode(ByteBuf buf) {
        return new ClientBoundChangeHostPacket(buf.readBoolean());
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, isHost);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}

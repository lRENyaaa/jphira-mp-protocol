package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundChangeHostPacket extends ClientBoundPacket {

    private final boolean host;

    public static ClientBoundChangeHostPacket create(boolean isHost) {
        return new ClientBoundChangeHostPacket(isHost);
    }

    public static ClientBoundChangeHostPacket create(boolean isHost, byte[] trailer) {
        return create(isHost).setTrailer(trailer, ClientBoundChangeHostPacket.class);
    }

    public static ClientBoundChangeHostPacket decode(ByteBuf buf) {
        return new ClientBoundChangeHostPacket(buf.readBoolean()).setTrailer(buf, ClientBoundChangeHostPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, host);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}

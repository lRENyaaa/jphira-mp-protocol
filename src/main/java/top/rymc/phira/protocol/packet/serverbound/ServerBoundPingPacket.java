package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundPingPacket extends ServerBoundPacket implements Singletonizable {

    public static final ServerBoundPingPacket INSTANCE = new ServerBoundPingPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ServerBoundPingPacket create(byte[] trailer) {
        return new ServerBoundPingPacket().setTrailer(trailer, ServerBoundPingPacket.class);
    }

    public static ServerBoundPingPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ServerBoundPingPacket().setTrailer(buf, ServerBoundPingPacket.class) : INSTANCE;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

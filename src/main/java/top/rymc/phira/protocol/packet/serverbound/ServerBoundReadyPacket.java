package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundReadyPacket extends ServerBoundPacket implements Singletonizable {

    public static final ServerBoundReadyPacket INSTANCE = new ServerBoundReadyPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ServerBoundReadyPacket create(byte[] trailer) {
        return new ServerBoundReadyPacket().setTrailer(trailer, ServerBoundReadyPacket.class);
    }

    public static ServerBoundReadyPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ServerBoundReadyPacket().setTrailer(buf, ServerBoundReadyPacket.class) : INSTANCE;
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

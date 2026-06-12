package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundAbortPacket extends ServerBoundPacket implements Singletonizable {

    public static final ServerBoundAbortPacket INSTANCE = new ServerBoundAbortPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ServerBoundAbortPacket create(byte[] trailer) {
        return new ServerBoundAbortPacket().setTrailer(trailer, ServerBoundAbortPacket.class);
    }

    public static ServerBoundAbortPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ServerBoundAbortPacket().setTrailer(buf, ServerBoundAbortPacket.class) : INSTANCE;
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

package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundRequestStartPacket extends ServerBoundPacket implements Singletonizable {

    public static final ServerBoundRequestStartPacket INSTANCE = new ServerBoundRequestStartPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ServerBoundRequestStartPacket create(byte[] trailer) {
        return new ServerBoundRequestStartPacket().setTrailer(trailer, ServerBoundRequestStartPacket.class);
    }

    public static ServerBoundRequestStartPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ServerBoundRequestStartPacket().setTrailer(buf, ServerBoundRequestStartPacket.class) : INSTANCE;
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

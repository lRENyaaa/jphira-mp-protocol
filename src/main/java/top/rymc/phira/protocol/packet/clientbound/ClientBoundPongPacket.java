package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundPongPacket extends ClientBoundPacket implements Singletonizable {

    public static final ClientBoundPongPacket INSTANCE = new ClientBoundPongPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ClientBoundPongPacket create(byte[] trailer) {
        return new ClientBoundPongPacket().setTrailer(trailer, ClientBoundPongPacket.class);
    }

    public static ClientBoundPongPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ClientBoundPongPacket().setTrailer(buf, ClientBoundPongPacket.class) : INSTANCE;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}

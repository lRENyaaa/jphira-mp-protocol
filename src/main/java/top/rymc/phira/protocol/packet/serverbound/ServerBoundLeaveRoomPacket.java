package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Singletonizable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundLeaveRoomPacket extends ServerBoundPacket implements Singletonizable {

    public static final ServerBoundLeaveRoomPacket INSTANCE = new ServerBoundLeaveRoomPacket();

    @Override
    public boolean isSingleton() {
        return this == INSTANCE;
    }

    public static ServerBoundLeaveRoomPacket create(byte[] trailer) {
        return new ServerBoundLeaveRoomPacket().setTrailer(trailer, ServerBoundLeaveRoomPacket.class);
    }

    public static ServerBoundLeaveRoomPacket decode(ByteBuf buf) {
        return buf.isReadable() ? new ServerBoundLeaveRoomPacket().setTrailer(buf, ServerBoundLeaveRoomPacket.class) : INSTANCE;
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

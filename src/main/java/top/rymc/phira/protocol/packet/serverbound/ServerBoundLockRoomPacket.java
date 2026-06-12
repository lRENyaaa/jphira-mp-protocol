package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundLockRoomPacket extends ServerBoundPacket {

    private final boolean lock;

    public static ServerBoundLockRoomPacket create(boolean lock) {
        return new ServerBoundLockRoomPacket(lock);
    }

    public static ServerBoundLockRoomPacket create(boolean lock, byte[] trailer) {
        return create(lock).setTrailer(trailer, ServerBoundLockRoomPacket.class);
    }

    public static ServerBoundLockRoomPacket decode(ByteBuf buf) {
        return new ServerBoundLockRoomPacket(buf.readBoolean()).setTrailer(buf, ServerBoundLockRoomPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, lock);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

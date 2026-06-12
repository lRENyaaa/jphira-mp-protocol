package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundJoinRoomPacket extends ServerBoundPacket {

    private final String roomId;
    private final boolean monitor;

    public static ServerBoundJoinRoomPacket create(String roomId, boolean monitor) {
        return new ServerBoundJoinRoomPacket(roomId, monitor);
    }

    public static ServerBoundJoinRoomPacket create(String roomId, boolean monitor, byte[] trailer) {
        return create(roomId, monitor).setTrailer(trailer, ServerBoundJoinRoomPacket.class);
    }

    public static ServerBoundJoinRoomPacket decode(ByteBuf buf) {
        return new ServerBoundJoinRoomPacket(
                NettyPacketUtil.decodeString(buf, 20),
                buf.readBoolean()
        ).setTrailer(buf, ServerBoundJoinRoomPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, roomId);
        PacketWriter.write(buf, monitor);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

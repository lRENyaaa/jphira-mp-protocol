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
public class ServerBoundCreateRoomPacket extends ServerBoundPacket {

    private final String roomId;

    public static ServerBoundCreateRoomPacket create(String roomId) {
        return new ServerBoundCreateRoomPacket(roomId);
    }

    public static ServerBoundCreateRoomPacket create(String roomId, byte[] trailer) {
        return create(roomId).setTrailer(trailer, ServerBoundCreateRoomPacket.class);
    }

    public static ServerBoundCreateRoomPacket decode(ByteBuf buf) {
        return new ServerBoundCreateRoomPacket(NettyPacketUtil.decodeString(buf, 20)).setTrailer(buf, ServerBoundCreateRoomPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, roomId);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundCreateRoomPacket extends ServerBoundPacket {

    private final String roomId;

    public static ServerBoundCreateRoomPacket decode(ByteBuf buf) {
        return new ServerBoundCreateRoomPacket(NettyPacketUtil.decodeString(buf, 20));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, roomId);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

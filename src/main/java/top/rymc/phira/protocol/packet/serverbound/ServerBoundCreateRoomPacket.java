package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

@Getter
public class ServerBoundCreateRoomPacket extends ServerBoundPacket {

    private String roomId;

    @Override
    public void decode(ByteBuf buf) {
        roomId = NettyPacketUtil.readString(buf, 20);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

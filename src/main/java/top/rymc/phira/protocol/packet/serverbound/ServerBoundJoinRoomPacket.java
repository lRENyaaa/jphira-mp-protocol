package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

@Getter
public class ServerBoundJoinRoomPacket extends ServerBoundPacket {

    private String roomId;
    private boolean monitor;

    @Override
    public void decode(ByteBuf buf) {
        roomId = NettyPacketUtil.decodeString(buf, 20);
        monitor = buf.readBoolean();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

@Getter
public class ServerBoundChatPacket extends ServerBoundPacket {

    private String message;

    @Override
    public void decode(ByteBuf buf) {
        message = NettyPacketUtil.readString(buf, 200);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

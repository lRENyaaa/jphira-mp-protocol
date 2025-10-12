package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

@Getter
public class ServerBoundAuthenticatePacket extends ServerBoundPacket {

    private String token;

    @Override
    public void decode(ByteBuf buf) {
        this.token = NettyPacketUtil.readString(buf, 32);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }

}

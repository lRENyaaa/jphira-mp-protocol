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
public class ServerBoundChatPacket extends ServerBoundPacket {

    private final String message;

    public static ServerBoundChatPacket decode(ByteBuf buf) {
        return new ServerBoundChatPacket(NettyPacketUtil.decodeString(buf, 200));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, message);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

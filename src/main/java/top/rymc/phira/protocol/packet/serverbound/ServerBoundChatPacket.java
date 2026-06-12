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
public class ServerBoundChatPacket extends ServerBoundPacket {

    private final String message;

    public static ServerBoundChatPacket create(String message) {
        return new ServerBoundChatPacket(message);
    }

    public static ServerBoundChatPacket create(String message, byte[] trailer) {
        return create(message).setTrailer(trailer, ServerBoundChatPacket.class);
    }

    public static ServerBoundChatPacket decode(ByteBuf buf) {
        return new ServerBoundChatPacket(NettyPacketUtil.decodeString(buf, 200)).setTrailer(buf, ServerBoundChatPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, message);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

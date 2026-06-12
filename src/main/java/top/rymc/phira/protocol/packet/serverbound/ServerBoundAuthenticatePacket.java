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
public class ServerBoundAuthenticatePacket extends ServerBoundPacket {

    private final String token;

    public static ServerBoundAuthenticatePacket create(String token) {
        return new ServerBoundAuthenticatePacket(token);
    }

    public static ServerBoundAuthenticatePacket create(String token, byte[] trailer) {
        return create(token).setTrailer(trailer, ServerBoundAuthenticatePacket.class);
    }

    public static ServerBoundAuthenticatePacket decode(ByteBuf buf) {
        return new ServerBoundAuthenticatePacket(NettyPacketUtil.decodeString(buf, 32)).setTrailer(buf, ServerBoundAuthenticatePacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, token);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }

}

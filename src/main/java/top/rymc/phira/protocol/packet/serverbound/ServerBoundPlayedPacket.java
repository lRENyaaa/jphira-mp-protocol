package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundPlayedPacket extends ServerBoundPacket {

    private final int id;

    public static ServerBoundPlayedPacket decode(ByteBuf buf) {
        return new ServerBoundPlayedPacket(buf.readIntLE());
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

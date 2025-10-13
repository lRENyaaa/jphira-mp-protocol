package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

@Getter
public class ServerBoundPlayedPacket extends ServerBoundPacket {

    private Integer id;

    @Override
    public void decode(ByteBuf buf) {
        id = buf.readIntLE();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

@Getter
public class ServerBoundCycleRoomPacket extends ServerBoundPacket {

    private Boolean cycle;

    @Override
    public void decode(ByteBuf buf) {
        cycle = buf.readBoolean();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

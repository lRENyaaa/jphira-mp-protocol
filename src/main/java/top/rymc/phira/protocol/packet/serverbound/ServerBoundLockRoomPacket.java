package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

@Getter
public class ServerBoundLockRoomPacket extends ServerBoundPacket {

    private Boolean lock;

    @Override
    public void decode(ByteBuf buf) {
        lock = buf.readBoolean();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

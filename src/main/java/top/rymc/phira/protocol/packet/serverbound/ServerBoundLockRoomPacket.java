package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundLockRoomPacket extends ServerBoundPacket {

    private final boolean lock;

    public static ServerBoundLockRoomPacket decode(ByteBuf buf) {
        return new ServerBoundLockRoomPacket(buf.readBoolean());
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, lock);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}

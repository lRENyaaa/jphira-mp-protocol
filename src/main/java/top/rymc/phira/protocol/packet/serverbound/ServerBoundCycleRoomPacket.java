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
public class ServerBoundCycleRoomPacket extends ServerBoundPacket {

    private final boolean cycle;

    public static ServerBoundCycleRoomPacket create(boolean cycle) {
        return new ServerBoundCycleRoomPacket(cycle);
    }

    public static ServerBoundCycleRoomPacket create(boolean cycle, byte[] trailer) {
        return create(cycle).setTrailer(trailer, ServerBoundCycleRoomPacket.class);
    }

    public static ServerBoundCycleRoomPacket decode(ByteBuf buf) {
        return new ServerBoundCycleRoomPacket(buf.readBoolean()).setTrailer(buf, ServerBoundCycleRoomPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, cycle);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}

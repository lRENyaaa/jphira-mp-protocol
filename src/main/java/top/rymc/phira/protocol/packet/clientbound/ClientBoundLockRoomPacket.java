package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundLockRoomPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundLockRoomPacket success() {
        return new ClientBoundLockRoomPacket(PacketResult.success(null));
    }

    public static ClientBoundLockRoomPacket failed(String failedMessage) {
        return new ClientBoundLockRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundLockRoomPacket decode(ByteBuf buf) {
        return new ClientBoundLockRoomPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}

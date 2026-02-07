package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundCycleRoomPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundCycleRoomPacket success() {
        return new ClientBoundCycleRoomPacket(PacketResult.success(null));
    }

    public static ClientBoundCycleRoomPacket failed(String failedMessage) {
        return new ClientBoundCycleRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundCycleRoomPacket decode(ByteBuf buf) {
        return new ClientBoundCycleRoomPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

}

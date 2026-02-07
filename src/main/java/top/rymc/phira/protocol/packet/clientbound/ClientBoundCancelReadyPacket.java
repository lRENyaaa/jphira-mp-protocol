package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundCancelReadyPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundCancelReadyPacket success() {
        return new ClientBoundCancelReadyPacket(PacketResult.success(null));
    }

    public static ClientBoundCancelReadyPacket failed(String failedMessage) {
        return new ClientBoundCancelReadyPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundCancelReadyPacket decode(ByteBuf buf) {
        return new ClientBoundCancelReadyPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }
}

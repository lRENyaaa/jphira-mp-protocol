package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundReadyPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundReadyPacket success() {
        return new ClientBoundReadyPacket(PacketResult.success(null));
    }

    public static ClientBoundReadyPacket failed(String failedMessage) {
        return new ClientBoundReadyPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundReadyPacket decode(ByteBuf buf) {
        return new ClientBoundReadyPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

}

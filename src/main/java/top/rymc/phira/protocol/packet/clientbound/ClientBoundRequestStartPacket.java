package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundRequestStartPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundRequestStartPacket success() {
        return new ClientBoundRequestStartPacket(PacketResult.success(null));
    }

    public static ClientBoundRequestStartPacket failed(String failedMessage) {
        return new ClientBoundRequestStartPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundRequestStartPacket decode(ByteBuf buf) {
        return new ClientBoundRequestStartPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

}

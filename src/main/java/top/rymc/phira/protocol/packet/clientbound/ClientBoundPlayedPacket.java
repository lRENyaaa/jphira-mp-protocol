package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundPlayedPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundPlayedPacket success() {
        return new ClientBoundPlayedPacket(PacketResult.success(null));
    }

    public static ClientBoundPlayedPacket failed(String failedMessage) {
        return new ClientBoundPlayedPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundPlayedPacket decode(ByteBuf buf) {
        return new ClientBoundPlayedPacket(PacketResult.decodeVoid(buf));
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
